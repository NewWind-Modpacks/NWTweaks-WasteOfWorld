package com.newwind.nwtweaks.event;

import com.elenai.feathers.capability.PlayerFeathersProvider;
import com.elenai.feathers.networking.FeathersMessages;
import com.elenai.feathers.networking.packet.ColdSyncSTCPacket;
import com.mrcrayfish.backpacked.item.BackpackItem;
import com.newwind.nwtweaks.NWConfig;
import com.newwind.nwtweaks.NWTweaks;
import com.newwind.nwtweaks.util.BreakChecks;
import com.newwind.nwtweaks.util.CommonUtils;
import com.newwind.nwtweaks.util.RadUtil;
import com.newwind.nwtweaks.world.items.ProtectiveMask;
import com.stereowalker.survive.Survive;
import com.stereowalker.survive.core.SurviveEntityStats;
import com.stereowalker.survive.world.entity.ai.attributes.SAttributes;
import dev.compactmods.machines.dimension.Dimension;
import dev.compactmods.machines.util.PlayerUtil;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.event.TickEvent.PlayerTickEvent;
import net.minecraftforge.event.enchanting.EnchantmentLevelSetEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import nuparu.tinyinv.utils.Utils;
import top.theillusivec4.curios.api.event.CurioEquipEvent;
import top.theillusivec4.curios.api.event.CurioUnequipEvent;

@Mod.EventBusSubscriber(modid = NWTweaks.MODID)
public class CommonEvents {

	@SubscribeEvent
	public static void onPlayerTick(PlayerTickEvent event) {
		handleRadiation(event.player);
		ServerPlayer player;
		if (event.player instanceof ServerPlayer) {
			player = (ServerPlayer) event.player;
		} else return;

		handleCompactKick(player);
		handleFrostFeathers(player);

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
	//public static void onEntityDamage(LivingAttackEvent event) {
	//	if ((event.getSource().getEntity() instanceof ServerPlayer player)
	//					&& (event.getSource() instanceof EntityDamageSource) && !(event.getSource() instanceof IndirectEntityDamageSource)
	//					&& ProfileConfig.shouldStop(player, AoSAction.ATTACKING))
	//		event.setCanceled(true);
	//}

	@SubscribeEvent
	public static void onCurioEquip(CurioEquipEvent event) {
		if (event.getEntity() instanceof Player player) {
			if (event.getStack().getItem() instanceof BackpackItem)
				Utils.fixContainer(player.containerMenu, player);
			else if (event.getStack().getItem() instanceof ProtectiveMask) {
				ItemStack mask = event.getStack();
				if (!mask.getOrCreateTag().contains("Air"))
					ProtectiveMask.setAir(mask, 0);
			}
		}
	}

	@SubscribeEvent
	public static void onCurioUnequip(CurioUnequipEvent event) {
		if (event.getEntity() instanceof Player player)
			if (event.getStack().getItem() instanceof BackpackItem) {
				if (isBackpackUnused(player))
					Utils.fixContainer(player.containerMenu, player);
				else
					event.setCanceled(true);
			} else if (event.getStack().getItem() instanceof ProtectiveMask) {
				ProtectiveMask.setAir(event.getStack(), 0);
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
	public static void onEnchantmentlevel(EnchantmentLevelSetEvent event) {
		if (event.getItem().getItem() instanceof BackpackItem && event.getEnchantLevel() < NWConfig.Common.BACKPACK_MIN_POWER.get()) {
			if (event.getEnchantRow() == 2)
				event.setEnchantLevel(1);
			else
				event.setEnchantLevel(0);
		}
	}

	@SubscribeEvent
	public static void onBlockDig(PlayerEvent.BreakSpeed event) {
		if (NWConfig.Common.ENABLE_BLOCK_BREAK_CHECK.get() && !BreakChecks.check(event.getEntity(), event.getState()))
			event.setCanceled(true);
	}

}
