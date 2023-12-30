package com.newwind.nwtweaks.util;

import com.github.wolfiewaffle.hardcore_torches.init.ItemInit;
import com.illusivesoulworks.diet.api.DietApi;
import com.illusivesoulworks.diet.api.type.IDietGroup;
import com.illusivesoulworks.diet.api.type.IDietResult;
import com.illusivesoulworks.diet.api.type.IDietTracker;
import com.illusivesoulworks.diet.common.capability.DietCapability;
import com.illusivesoulworks.diet.common.data.group.DietGroups;
import com.mojang.datafixers.util.Pair;
import com.newwind.nwtweaks.NWConfig;
import fuzs.thinair.advancements.AirSource;
import fuzs.thinair.helper.AirHelper;
import fuzs.thinair.helper.AirQualityLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;
import se.mickelus.tetra.items.modular.impl.shield.ModularShieldItem;
import top.theillusivec4.curios.api.CuriosApi;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Predicate;

public class MixinExternalFunctions {

	public static boolean hasAnimationDisablerStack(Player player) {
		List<? extends String> items = NWConfig.Client.PARCOOL_NOT_ANIMATE.get();
		Item stack = player.getMainHandItem().getItem();
		for (String item : items) {
			if (item.equals("minecraft:air")) {
				if (!stack.equals(Items.AIR))
					return true;
			} else //noinspection DataFlowIssue
				if (ForgeRegistries.ITEMS.getValue(new ResourceLocation(item)).equals(stack))
					return true;
		}
		return false;
	}

	public static Item isStackShield(ItemStack stack) {
		if (stack.getItem() instanceof ModularShieldItem)
			return Items.SHIELD;
		else
			return stack.getItem();
	}

	public static class CaveLantern {

		private static final Item[] validLightItems = new Item[]{
						Items.TORCH,
						Items.SOUL_TORCH,
						Items.LANTERN,
						Items.SOUL_LANTERN,
						ItemInit.LIT_TORCH.get(),
						ItemInit.LIT_LANTERN.get(),
						ItemInit.LIT_SOUL_LANTERN.get()
		};

		private static final Predicate<ItemStack> validLightSources = itemStack -> isValidLightSource(itemStack.getItem());

		private static boolean isValidLightSource(Item item) {
			for (Item validLightItem : validLightItems) {
				if (item.equals(validLightItem))
					return true;
			}
			return false;
		}

		public static boolean hasValidLightSource(Player player) {
			return isValidLightSource(player.getMainHandItem().getItem())
							|| isValidLightSource(player.getOffhandItem().getItem())
							|| CuriosApi.getCuriosHelper().findFirstCurio(player, validLightSources).isPresent();
		}

	}

	public static class CaveOxygen {

		private static final Item[] validSoulItems = new Item[]{
						Items.SOUL_TORCH,
						Items.SOUL_LANTERN,
						ItemInit.LIT_SOUL_LANTERN.get()
		};
		private static final Predicate<ItemStack> validSoulSources = itemStack -> isValidSoulSource(itemStack.getItem());

		public static Pair<AirQualityLevel, AirSource> getO2FromEntity(Entity entity) {
			Level world = entity.getLevel();
			Vec3 position = entity.getEyePosition();
			Pair<AirQualityLevel, AirSource> o2 = AirHelper.getO2LevelFromLocation(position, world);
			if ((o2.getSecond() == AirSource.DIMENSION || o2.getSecond() == AirSource.LAVA)
							&& !CommonUtils.isUnderground(entity)) {
				o2 = new Pair<>(AirQualityLevel.GREEN, AirSource.DIMENSION);
			}

			return o2;
		}

		public static Pair<AirQualityLevel, AirSource> getO2FromLantern(Entity entity) {
			Level world = entity.getLevel();
			Vec3 position = entity.getEyePosition();
			boolean isUnderground;
			if (entity instanceof LocalPlayer)
				isUnderground = CommonUtils.isUnderground(entity);
			else
				isUnderground = CommonUtils.isUnderground(world, new BlockPos(position));

			Pair<AirQualityLevel, AirSource> o2 = AirHelper.getO2LevelFromLocation(position, world);
			if ((o2.getSecond() == AirSource.DIMENSION || o2.getSecond() == AirSource.LAVA)
							&& !isUnderground) {
				o2 = new Pair<>(AirQualityLevel.GREEN, AirSource.DIMENSION);
			}

			return o2;
		}

		private static boolean isValidSoulSource(Item item) {
			for (Item validLightItem : validSoulItems) {
				if (item.equals(validLightItem))
					return true;
			}
			return false;
		}

		public static boolean hasValidSoulSource(Player player) {
			return isValidSoulSource(player.getMainHandItem().getItem())
							|| isValidSoulSource(player.getOffhandItem().getItem())
							|| CuriosApi.getCuriosHelper().findFirstCurio(player, validSoulSources).isPresent();
		}

		public static Pair<AirQualityLevel, AirSource> soulifyO2Pair(LivingEntity entity, Pair<AirQualityLevel, AirSource> o2Pair) {

			boolean hasSoulSource = false;
			if (entity instanceof Player player)
				hasSoulSource = MixinExternalFunctions.CaveOxygen.hasValidSoulSource(player);

			if (hasSoulSource &&
							(o2Pair.getFirst() == AirQualityLevel.YELLOW || o2Pair.getFirst() == AirQualityLevel.RED)
							&& o2Pair.getSecond() == AirSource.DIMENSION)
				return new Pair<>(AirQualityLevel.BLUE, AirSource.SOUL);
			else
				return o2Pair;
		}
	}

	public static float getFoodModifier(Player player, ItemStack stack) {
		AtomicReference<Float> nutritionModifier = new AtomicReference<>(1f);
		DietCapability.get(player).ifPresent(tracker -> {
			DietApi dietInstance = DietApi.getInstance();

			IDietResult stackGroups = dietInstance.get(player, stack);
			if (!stackGroups.get().isEmpty()) {
				Set<IDietGroup> foodGroups = DietGroups.getGroups(player.getLevel());
				HashMap<String, Float> groupMods = getFoodGroupMods(tracker, foodGroups);

				AtomicReference<Float> totalStackValues = new AtomicReference<>(0f);
				stackGroups.get().forEach((group, value) -> totalStackValues.updateAndGet(v -> v + value));

				AtomicReference<Float> difference = new AtomicReference<>((float) 0);
				stackGroups.get().forEach((group, value) -> {
					float stackPercent = value / totalStackValues.get();
					difference.updateAndGet(v -> v + (stackPercent * groupMods.get(group.getName())));
				});

				float minBound = NWConfig.Common.NUTRITION_MIN_DIFFERENCE.get().floatValue();
				float maxBound = NWConfig.Common.NUTRITION_MAX_DIFFERENCE.get().floatValue();

				difference.getAndUpdate(v -> Mth.clamp(
								( v - minBound) / (maxBound - minBound),
								0f, 1f));

				nutritionModifier.set(1f - difference.get());
			}
		});
		return nutritionModifier.get();
	}

	@NotNull
	private static HashMap<String, Float> getFoodGroupMods(IDietTracker tracker, Set<IDietGroup> foodGroups) {
		Map<String, Float> playerGroups = tracker.getValues();

		AtomicReference<Float> atomicPlayerAverage = new AtomicReference<>(0f);
		AtomicInteger atomicBeneficialSize = new AtomicInteger();
		playerGroups.forEach((group, value) -> {
			for (IDietGroup foodGroup : foodGroups)
				if (foodGroup.getName().equals(group)) {
					if (foodGroup.isBeneficial()) {
						atomicPlayerAverage.updateAndGet(v -> v + value);
						atomicBeneficialSize.getAndIncrement();
					}
					return;
				}
		});
		float playerAverage = atomicPlayerAverage.get() / atomicBeneficialSize.get();

		HashMap<String, Float> groupMods = new HashMap<>();
		playerGroups.forEach((group, value) -> groupMods.put(group, value - playerAverage));
		return groupMods;
	}

}
