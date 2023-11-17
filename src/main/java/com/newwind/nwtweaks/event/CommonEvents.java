package com.newwind.nwtweaks.event;

import com.elenai.feathers.capability.PlayerFeathersProvider;
import com.elenai.feathers.networking.FeathersMessages;
import com.elenai.feathers.networking.packet.ColdSyncSTCPacket;
import com.mrcrayfish.backpacked.item.BackpackItem;
import com.mrcrayfish.guns.item.GunItem;
import com.newwind.nwtweaks.NWConfig;
import com.newwind.nwtweaks.NWTweaks;
import com.newwind.nwtweaks.capability.BladderAir;
import com.newwind.nwtweaks.capability.BladderAirProvider;
import com.newwind.nwtweaks.capability.IsUnderground;
import com.newwind.nwtweaks.capability.IsUndergroundProvider;
import com.newwind.nwtweaks.networking.ModMessages;
import com.newwind.nwtweaks.networking.packet.S2CIsUnderground;
import com.newwind.nwtweaks.registries.Attributes;
import com.newwind.nwtweaks.util.BreakChecks;
import com.newwind.nwtweaks.util.CommonUtils;
import com.newwind.nwtweaks.util.RadUtil;
import com.stereowalker.survive.Survive;
import com.stereowalker.survive.core.SurviveEntityStats;
import com.stereowalker.survive.world.entity.ai.attributes.SAttributes;
import dev.compactmods.machines.dimension.Dimension;
import dev.compactmods.machines.util.PlayerUtil;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.CrossbowItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.TickEvent.PlayerTickEvent;
import net.minecraftforge.event.enchanting.EnchantmentLevelSetEvent;
import net.minecraftforge.event.entity.EntityAttributeModificationEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.player.ArrowLooseEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.registries.ForgeRegistries;
import nuparu.tinyinv.utils.Utils;
import se.mickelus.tetra.items.modular.impl.bow.ModularBowItem;
import se.mickelus.tetra.items.modular.impl.crossbow.ModularCrossbowItem;
import top.theillusivec4.curios.api.event.CurioEquipEvent;
import top.theillusivec4.curios.api.event.CurioUnequipEvent;

@Mod.EventBusSubscriber(modid = NWTweaks.MODID)
public class CommonEvents {

	@SubscribeEvent
	public static void onPlayerTick(final PlayerTickEvent event) {
		handleRadiation(event.player);
		ServerPlayer player;
		if (event.player instanceof ServerPlayer) {
			player = (ServerPlayer) event.player;
		} else return;

		handleCompactKick(player);
		handleFrostFeathers(player);

		updateHeldStack(player);


	}

	@SubscribeEvent
	public static void onLivingTick(LivingEvent.LivingTickEvent event) {
		Entity entity = event.getEntity();
		if(!entity.level.isClientSide) {
			entity.getCapability(IsUndergroundProvider.IS_UNDERGROUND).ifPresent(undergroundObject -> {
								undergroundObject.countDown();
								if (undergroundObject.getNextCheck() <= 0) {
									undergroundObject.resetCheck();
									undergroundObject.setUnderground(CommonUtils.checkUnderground(entity));
									if (entity instanceof ServerPlayer player)
										ModMessages.sendToClient(new S2CIsUnderground(undergroundObject.isUnderground()), player);
								}
							}
			);
		}
	}

	private static void handleCompactKick(ServerPlayer player) {
		ServerLevel level = player.getLevel();

		if (level.dimension().equals(Dimension.COMPACT_DIMENSION) && CommonUtils.areCMUnavailable(level.getServer(), player))
			PlayerUtil.teleportPlayerOutOfMachine(level, player);
	}


	/**
	 * @author Kevadroz (With code from Feathers)
	 */
	private static void handleFrostFeathers(ServerPlayer player) {
		player.getCapability(PlayerFeathersProvider.PLAYER_FEATHERS).ifPresent(feathers -> {
			if (SurviveEntityStats.getTemperatureStats(player).getTemperatureLevel() < Survive.DEFAULT_TEMP - player.getAttributeValue(SAttributes.COLD_RESISTANCE) * NWConfig.Common.FEATHERS_FREEZE_TEMP.get()) {
				if (!feathers.isCold()) {
					feathers.setCold(true);
					FeathersMessages.sendToPlayer(new ColdSyncSTCPacket(feathers.isCold()), player);
				}
			} else if (feathers.isCold()) {
				feathers.setCold(false);
				FeathersMessages.sendToPlayer(new ColdSyncSTCPacket(feathers.isCold()), player);
			}
		});
	}

	private static void handleRadiation(Player player) {
		if (NWConfig.Common.ENABLE_RADIATION.get() && player.getFeetBlockState().getBlock() == Blocks.SNOW)
			RadUtil.modifyRadiation(player, 0.005);
		else
			RadUtil.modifyRadiation(player, -0.001);
	}

	//@SubscribeEvent
	//public static void onEntityDamage(final LivingAttackEvent event) {
	//	if ((event.getSource().getEntity() instanceof ServerPlayer player)
	//					&& (event.getSource() instanceof EntityDamageSource) && !(event.getSource() instanceof IndirectEntityDamageSource)
	//					&& ProfileConfig.shouldStop(player, AoSAction.ATTACKING))
	//		event.setCanceled(true);
	//}

	private static void updateHeldStack(Player player) {
		String tag = NWConfig.Common.RELOAD_SPEED_TAG.get();

		updateTags(player.getMainHandItem(), tag, player);
		updateTags(player.getOffhandItem(), tag, player);

	}

	private static void updateTags(ItemStack stack, String tag, Player player) {
		boolean hasTag = player.getTags().contains(tag);
		CompoundTag stackTag = stack.getTag();
		Item item = stack.getItem();
		if (stackTag == null) return;
		if (item instanceof BowItem
						|| item instanceof CrossbowItem
						|| item instanceof GunItem
						|| item instanceof ModularBowItem
						|| item instanceof ModularCrossbowItem

						&& (!stackTag.contains(tag) || stackTag.getBoolean(tag) != hasTag))
			stackTag.putBoolean(tag, hasTag);
	}

	@SubscribeEvent
	public static void onCurioEquip(final CurioEquipEvent event) {
		if (event.getEntity() instanceof Player player) {
			if (event.getStack().getItem() instanceof BackpackItem)
				Utils.fixContainer(player.containerMenu, player);
		}
	}

	@SubscribeEvent
	public static void onCurioUnequip(final CurioUnequipEvent event) {
		if (event.getEntity() instanceof Player player)
			if (event.getStack().getItem() instanceof BackpackItem) {
				if (isBackpackUnused(player))
					Utils.fixContainer(player.containerMenu, player);
				else
					event.setCanceled(true);
			}
	}

	private static boolean isBackpackUnused(Player player) {
		AbstractContainerMenu container = player.containerMenu;
		for (int i = 0; i < container.slots.size(); ++i) {
			Slot slot = (Slot) container.slots.get(i);
			if (CommonUtils.tinyInvShouldBeRemoved(slot, player, container, false)
							&& !slot.getItem().isEmpty())
				return false;
		}
		return true;
	}


	@SubscribeEvent
	public static void onEnchantmentlevel(final EnchantmentLevelSetEvent event) {
		if (event.getItem().getItem() instanceof BackpackItem && event.getEnchantLevel() < NWConfig.Common.BACKPACK_MIN_POWER.get()) {
			if (event.getEnchantRow() == 2)
				event.setEnchantLevel(1);
			else
				event.setEnchantLevel(0);
		}
	}

	@SubscribeEvent
	public static void onBlockDig(final PlayerEvent.BreakSpeed event) {
		if (NWConfig.Common.ENABLE_BLOCK_BREAK_CHECK.get() && !BreakChecks.check(event.getEntity(), event.getState()))
			event.setCanceled(true);
	}

	@SubscribeEvent
	public static void onArrowLooseEvent(ArrowLooseEvent event) {
		if (event.getBow().getItem() instanceof BowItem)
			if (event.getEntity().getTags().contains(NWConfig.Common.RELOAD_SPEED_TAG.get()))
				event.setCharge((event.getCharge() + (int) (20 * NWConfig.Common.RELOAD_SPEED_MULTIPLIER.get())));
	}

	@SubscribeEvent
	public static void onAttachCapabilitiesLiving(AttachCapabilitiesEvent<Entity> event) {
		if(event.getObject() instanceof LivingEntity) {
			if(!event.getObject().getCapability(IsUndergroundProvider.IS_UNDERGROUND).isPresent()) {
				event.addCapability(new ResourceLocation(NWTweaks.MODID, "is_underground"), new IsUndergroundProvider());
			}
		}
	}

//	@SubscribeEvent
//	public static void onAttachCapabilitiesItemStack(AttachCapabilitiesEvent<ItemStack> event) {
//		ItemStack stack = event.getObject();
//		if (stack.is(ModRegistry.AIR_BLADDER_ITEM.get()))
//			event.addCapability(new ResourceLocation(NWTweaks.MODID, "air"), new BladderAirProvider());
//	}

	@Mod.EventBusSubscriber(modid = NWTweaks.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
	public static class ModBus {


		@SubscribeEvent
		public static void onEntityAttributeModificationEvent(final EntityAttributeModificationEvent event) {
			event.add(EntityType.PLAYER, Attributes.HEADSHOT_DAMAGE.get());
			event.add(EntityType.PLAYER, Attributes.BC_ATTACK_RANGE.get());
			event.add(EntityType.PLAYER, Attributes.RANGED_ARMOR_PIERCING.get());
		}

		@SubscribeEvent
		public static void onRegisterCapabilities(RegisterCapabilitiesEvent event) {
			event.register(IsUnderground.class);
			event.register(BladderAir.class);
		}

	}

}
