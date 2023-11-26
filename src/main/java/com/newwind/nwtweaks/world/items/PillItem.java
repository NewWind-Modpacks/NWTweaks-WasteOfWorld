package com.newwind.nwtweaks.world.items;

import com.newwind.nwtweaks.NWConfig;
import com.newwind.nwtweaks.capability.ServerPlayerDataProvider;
import com.newwind.nwtweaks.client.NWClient;
import com.newwind.nwtweaks.networking.ModMessages;
import com.newwind.nwtweaks.networking.packet.S2CDiscoveredPills;
import croissantnova.sanitydim.SanityProcessor;
import croissantnova.sanitydim.capability.SanityProvider;
import croissantnova.sanitydim.config.ConfigProxy;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

import static com.newwind.nwtweaks.world.items.PillItem.PILL_EFFECT.*;
import static com.newwind.nwtweaks.world.items.PillItem.PillEffectPool.PoolEntry;
import static com.newwind.nwtweaks.world.items.PillItem.PillEffectPoolSized.PoolEntrySized;

public class PillItem extends Item {


	public static final String SLOT_TAG = "pill_type";
	private static final PillEffectPoolSized fixedPillPool = new PillEffectPoolSized(
					new PoolEntrySized(SANITY_UP_5, 3f, 3, 7),
					new PoolEntrySized(SANITY_UP_10, 2f, 2, 5),
					new PoolEntrySized(SANITY_UP_15, 1f, 1, 2),
					new PoolEntrySized(SANITY_DOWN_10, 5f, 3, 6),
					new PoolEntrySized(SANITY_DOWN_20, 3f, 2, 4),
					new PoolEntrySized(SANITY_DOWN_30, 1.5f, 1, 2),
					new PoolEntrySized(POISON_1MIN, 1f, 2, 3),
					new PoolEntrySized(POISON_10MINS, 0.5f, 1, 2)
	);
	private static final PillEffectPool randomPillPool = new PillEffectPool(
					new PoolEntry(SANITY_UP_5, 5.2f),
					new PoolEntry(SANITY_UP_10, 2.6f),
					new PoolEntry(SANITY_UP_15, 0.65f),
					new PoolEntry(SANITY_DOWN_10, 2f),
					new PoolEntry(SANITY_DOWN_20, 1f),
					new PoolEntry(SANITY_DOWN_30, 0.25f),
					new PoolEntry(POISON_1MIN, 0.1f)
	);
	private static PILL_EFFECT[] pillSlots;
	private final Random rng = new Random();

	public PillItem(Properties p_41383_) {
		super(p_41383_);
	}

	public static void randomizePills(long worldSeed) {
		Random rng = new Random(worldSeed);

		ArrayList<PILL_EFFECT> slots = new ArrayList<>();
		PillEffectPoolSized pool = fixedPillPool.createUsable();
		ArrayList<PoolEntrySized> entries = pool.getEntries();

		for (int i = 0; i < entries.size(); i++) {
			PoolEntrySized entry = entries.get(i);
			for (int n = 0; n < entry.getMin() && n <= entry.getMax(); n++) {
				slots.add(entry.getEffect());
				entry.increase();
			}
			pool.checkForRemoval(i);
		}

		int minSize = NWConfig.Common.PILL_POOL_MIN_SIZE.get();
		int maxSize = NWConfig.Common.PILL_POOL_MAX_SIZE.get();
		int additional = rng.nextInt(minSize, maxSize + 1);
		for (int i = 0; !entries.isEmpty() && i < additional; i++)
			slots.add(pool.pickAndRemove(rng));

		Collections.shuffle(slots, rng);
		pillSlots = new PILL_EFFECT[slots.size()];
		pillSlots = slots.toArray(pillSlots);
	}

	public static int getSlotSize() {
		return pillSlots.length;
	}

	public static int[] getIntSlots(ServerPlayer player) {
		int[] intArray = new int[pillSlots.length];
		Arrays.fill(intArray, -1);
		player.getCapability(ServerPlayerDataProvider.CAPABILITY).ifPresent(serverPlayerData -> {
			for (int i = 0; i < pillSlots.length; i++)
				if (serverPlayerData.getDiscoveredPill(i))
					intArray[i] = pillSlots[i].ordinal();
		});
		return intArray;
	}

	private static String getPillTypeText(int pillType) {
		return switch (NWClient.pillEffects[pillType - 1]) {
			case SANITY_UP_5 -> "sanity_plus_5";
			case SANITY_UP_10 -> "sanity_plus_10";
			case SANITY_UP_15 -> "sanity_plus_15";
			case SANITY_DOWN_10 -> "sanity_minus_10";
			case SANITY_DOWN_20 -> "sanity_minus_20";
			case SANITY_DOWN_30 -> "sanity_minus_30";
			case POISON_1MIN -> "poison_1_minute";
			case POISON_10MINS -> "poison_10_minutes";
		};
	}

	private static ChatFormatting getPillTypeColor(int pillType) {
		return switch (NWClient.pillEffects[pillType - 1]) {
			case SANITY_UP_5, SANITY_UP_10, SANITY_UP_15 -> ChatFormatting.GREEN;
			case SANITY_DOWN_10, SANITY_DOWN_20, SANITY_DOWN_30, POISON_1MIN, POISON_10MINS -> ChatFormatting.RED;
		};
	}

	@Override
	public @NotNull InteractionResultHolder<ItemStack> use(@NotNull Level level, @NotNull Player player, @NotNull InteractionHand hand) {
		ItemStack stack = player.getItemInHand(hand);

		if (!level.isClientSide()) {

			float sanityAmount = 0;

			switch (getEffect(stack)) {
				case SANITY_UP_5 -> sanityAmount = -5;
				case SANITY_UP_10 -> sanityAmount = -10;
				case SANITY_UP_15 -> sanityAmount = -15;
				case SANITY_DOWN_10 -> sanityAmount = 10;
				case SANITY_DOWN_20 -> sanityAmount = 20;
				case SANITY_DOWN_30 -> sanityAmount = 30;
				case POISON_1MIN -> player.addEffect(new MobEffectInstance(MobEffects.POISON, 1200, 0, false, false, true));
				case POISON_10MINS -> player.addEffect(new MobEffectInstance(MobEffects.POISON, 12000, 0, false, false, true));
			}

			ResourceLocation location = level.dimension().location();
			float positiveMultiplier = ConfigProxy.getPosMul(location);
			float negativeMultiplier = ConfigProxy.getNegMul(location);
			if (sanityAmount > 0)
				sanityAmount /= negativeMultiplier;
			else if (sanityAmount < 0)
				sanityAmount /= positiveMultiplier;

			float finalSanityAmount = sanityAmount / 100;
			player.getCapability(SanityProvider.CAP).ifPresent(iSanity -> SanityProcessor.addSanity(iSanity, finalSanityAmount, (ServerPlayer) player));

			int slot = stack.getOrCreateTag().getInt(SLOT_TAG) - 1;
			if (slot >= 0 && slot < pillSlots.length)
				player.getCapability(ServerPlayerDataProvider.CAPABILITY).ifPresent(serverPlayerData -> {
					serverPlayerData.setDiscoveredPill(slot, true);
					ModMessages.sendToClient(new S2CDiscoveredPills(getIntSlots((ServerPlayer) player)), ((ServerPlayer) player));
				});
		}
		if (!player.isCreative())
			stack.shrink(1);
		return InteractionResultHolder.consume(stack);
	}

	private PILL_EFFECT getEffect(ItemStack stack) {
		int pillType = stack.getOrCreateTag().getInt(SLOT_TAG);

		if (pillType <= 0 || pillType >= pillSlots.length) {
			return randomPillPool.pick(rng);
		}

		return pillSlots[pillType - 1];
	}

	@Override
	public @NotNull Component getName(@NotNull ItemStack stack) {
		String pillName = "";
		int pillType = stack.getOrCreateTag().getInt(SLOT_TAG);
		if (pillType == 0 || NWClient.pillEffects == null || pillType >= NWClient.pillEffects.length)
			pillName = ".random";
		else if (NWClient.pillEffects[pillType - 1] != null)
			pillName = ".".concat(getPillTypeText(pillType));
		return Component.translatable(this.getDescriptionId(stack).concat(pillName));
	}

	@Override
	public void appendHoverText(ItemStack stack, @Nullable Level level, @NotNull List<Component> components, @NotNull TooltipFlag tooltipFlag) {
		String pillDesc;
		int pillType = stack.getOrCreateTag().getInt(SLOT_TAG);
		ChatFormatting formatting;
		if (pillType == 0 || NWClient.pillEffects == null || pillType - 1 >= NWClient.pillEffects.length) {
			pillDesc = "random";
			formatting = ChatFormatting.BLUE;
		} else if (NWClient.pillEffects[pillType - 1] != null) {
			pillDesc = getPillTypeText(pillType);
			formatting = getPillTypeColor(pillType);
		} else {
			pillDesc = "unknown";
			formatting = ChatFormatting.YELLOW;
		}
		components.add(Component.translatable(this.getDescriptionId(stack).concat(".description.").concat(pillDesc)).withStyle(formatting));
	}

	public enum PILL_EFFECT {
		SANITY_UP_5, // Painkillers
		SANITY_UP_10, // Antidepressants
		SANITY_UP_15, // Antipsychotics
		SANITY_DOWN_10, // Garbage pill
		SANITY_DOWN_20, // Represents
		SANITY_DOWN_30, // Psychotics
		//POISON_10SEC, // HummingBird Poison
		POISON_1MIN, // Rat Poison
		POISON_10MINS // Dog Poison
	}


	static class PillEffectPool {

		private final PoolEntry[] entries;

		public PillEffectPool(PoolEntry... entries) {
			this.entries = entries;
		}

		public PILL_EFFECT pick(Random rng) {
			float maxWeight = 0f;
			for (PoolEntry entry : entries)
				maxWeight += entry.getWeight();
			float selection = rng.nextFloat(0.0000000001f, maxWeight);
			for (PoolEntry entry : entries)
				if (entry.getWeight() > selection)
					return entry.getEffect();
				else
					selection -= entry.getWeight();
			return null;
		}

		static class PoolEntry {

			private final PILL_EFFECT effect;
			private final float weight;

			public PoolEntry(PILL_EFFECT effect, float weight) {
				this.effect = effect;
				this.weight = weight;
			}

			public PILL_EFFECT getEffect() {
				return effect;
			}

			public float getWeight() {
				return weight;
			}
		}
	}

	static class PillEffectPoolSized {

		private final boolean canBeUsed;
		private final ArrayList<PoolEntrySized> entries;

		public PillEffectPoolSized(PoolEntrySized... entries) {
			this.entries = new ArrayList<>(List.of(entries));
			this.canBeUsed = false;
		}

		@SuppressWarnings("unchecked")
		private PillEffectPoolSized(PillEffectPoolSized ogPool) {
			this.entries = (ArrayList<PoolEntrySized>) ogPool.entries.clone();
			this.canBeUsed = true;
		}

		public PILL_EFFECT pickAndRemove(Random rng) {
			if (!this.canBeUsed) return null;
			float maxWeight = 0f;
			for (PoolEntrySized entry : entries)
				maxWeight += entry.getWeight();
			float selection = rng.nextFloat(0.0000000001f, maxWeight);
			for (int i = 0; i < entries.size(); i++) {
				PoolEntrySized entry = entries.get(i);
				if (entry.getWeight() > selection) {
					entry.increase();
					checkForRemoval(i);
					return entry.getEffect();
				} else
					selection -= entry.getWeight();
			}
			return null;
		}

		public void checkForRemoval(int index) {
			if (index < entries.size() && index >= 0) {
				PoolEntrySized entry = entries.get(index);
				if (entry.getAmount() >= entry.getMax())
					entries.remove(index);
			}
		}

		public ArrayList<PoolEntrySized> getEntries() {
			return entries;
		}

		public PillEffectPoolSized createUsable() {
			return new PillEffectPoolSized(this);
		}

		static class PoolEntrySized extends PillEffectPool.PoolEntry {

			private final int min;
			private int max;
			private int amount = 0;

			public PoolEntrySized(PILL_EFFECT effect, float weight, int min, int max) {
				super(effect, weight);
				this.min = min;
				this.max = max;
			}

			public int getMin() {
				return min;
			}

			public int getMax() {
				return max;
			}

			public void increase() {
				this.amount += 1;
			}

			public int getAmount() {
				return amount;
			}
		}
	}
}
