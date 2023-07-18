package com.newwind.nwtweaks.util;

import com.mrcrayfish.backpacked.Backpacked;
import com.newwind.nwtweaks.NWConfig;
import com.newwind.nwtweaks.registries.Enchantments;
import mcjty.lostcities.api.ILostChunkInfo;
import mcjty.lostcities.api.ILostCities;
import mcjty.lostcities.api.ILostCityInformation;
import net.minecraft.core.BlockPos;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.ModList;
import nuparu.tinyinv.config.ServerConfig;
import nuparu.tinyinv.utils.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class CommonUtils {
	private static boolean registered = false;
	private static ILostCities lostCities;

	public static void register() {
		if (ModList.get().isLoaded("lostcities")) {
			registerInternal();
		}
	}

	private static void registerInternal() {
		if (registered) {
			return;
		}
		registered = true;
		InterModComms.sendTo("lostcities", "getLostCities", GetLostCities::new);
	}

	public static boolean areCMUnavailable(MinecraftServer server, ServerPlayer player) {
		if (!player.isCreative() && !player.isSpectator()) {
			final int start = NWConfig.Common.CM_TIMEOUT_START.get();
			final int end = NWConfig.Common.CM_TIMEOUT_END.get();
			long time = server.overworld().getDayTime() % 24000;
			if (end <= start)
				return (time < end || time >= start);
			else return (time >= start && time < end);
		} else return false;
	}

	public static int getAddedSlots(Player player) {
		ItemStack backpack = Backpacked.getBackpackStack(player);
		if (!backpack.isEmpty()) {
			int level = backpack.getEnchantmentLevel(Enchantments.BACKPACK_SIZE_PLUS.get());
			List<? extends Integer> levelList = NWConfig.Common.BACKPACK_SIZES.get();
			return levelList.get(Math.max(Math.min(levelList.size() - 1, level), 0));
		}
		return 0;
	}

	public static boolean tinyInvShouldBeRemoved(int id, Player player, Object container, boolean accountBackpack) {
		if (player.isCreative() && ServerConfig.excludeCreativeModePlayers.get()) {
			return false;
		} else if (ServerConfig.disableOffhand.get() && Utils.isOffhandSlot(id, player, container)) {
			return true;
		} else {
			int addedSlots = accountBackpack ? CommonUtils.getAddedSlots(player) : 0;
			if (ServerConfig.countSlotsFromStart.get()) {
				return id >= ServerConfig.inventorySlots.get() + addedSlots && id < ServerConfig.armorStartID.get();
			} else {
				return id < ServerConfig.armorStartID.get()
				&& (
						id < 9
								&& id >= (ServerConfig.inventorySlots.get() + (accountBackpack ? CommonUtils.getAddedSlots(player) : 0))
								|| id >= 9
								&& id <= ServerConfig.armorStartID.get() - 1 - Math.max((ServerConfig.inventorySlots.get() + addedSlots) - 9, 0)
				);
			}
		}
	}

	public static boolean tinyInvShouldBeRemoved(Slot slot, Player player, Object container, boolean accountBackpack) {
		return slot.container == player.getInventory() && tinyInvShouldBeRemoved(slot.getSlotIndex(), player, container, accountBackpack);
	}

	public static boolean isInCave(Player player) {
		Level level = player.getLevel();
		BlockPos blockPos = new BlockPos(player.getEyePosition());
		double y = blockPos.getY();
		double y_offset = y + NWConfig.Common.CAVE_DAMAGE_HEIGHT_OFFSET.get();

		return (
				level.getHeight(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, blockPos.getX(), blockPos.getZ()) > y_offset
						&& level.getHeight(Heightmap.Types.OCEAN_FLOOR, blockPos.getX(), blockPos.getZ()) > y_offset
						&& level.getHeight(Heightmap.Types.WORLD_SURFACE, blockPos.getX(), blockPos.getZ()) > y_offset
						|| level.getBlockState(blockPos).getBlock().equals(Blocks.CAVE_AIR)
						&& level.getBrightness(LightLayer.SKY, blockPos) < 1
						|| y < NWConfig.Common.CAVE_DAMAGE_HEIGHT.get()
		) && !isInLostCityBuilding(player);
	}

	public static float getCaveAirPercentAroundPlayer(Player player) { // Default radius
		return getCaveAirPercentAroundPlayer(player, 10);
	}

	public static float getCaveAirPercentAroundPlayer(Player player, int radius) {
		Level level = player.getLevel();
		ArrayList<BlockState> blocks = new ArrayList<>();

		int totalBlocks = 0;
		int caveAirBlocks = 0;

		for (int x = -radius; x < radius; x++) {
			for (int y = -radius; y < radius; y++) {
				for (int z = -radius; z < radius; z++) {
					blocks.add(level.getBlockState(new BlockPos(
							player.getEyePosition().x + x,
							player.getEyePosition().y + y,
							player.getEyePosition().z + z
					)));
				}
			}
		}

		for (BlockState block : blocks) {
			if (!(block.getBlock() == Blocks.AIR || block.getBlock() == Blocks.CAVE_AIR || block.getBlock() == Blocks.VOID_AIR)) continue;

			totalBlocks++;
			if (block.getBlock() == Blocks.CAVE_AIR) {
				caveAirBlocks++;
			}
		}

		if (totalBlocks == 0) { // Don't divide by 0
			return 0;
		}

		return ((float) caveAirBlocks / totalBlocks) * 100;
	}

	public static boolean isInLostCityBuilding(Player player) {
		Level level = player.getLevel();
		ILostCityInformation info = lostCities.getLostInfo(level);

		if (info == null) return false;
		BlockPos blockPos = new BlockPos(player.getEyePosition());
		ILostChunkInfo chunkInfo = info.getChunkInfo(blockPos.getX() >> 4, blockPos.getZ() >> 4);

		return chunkInfo.isCity() && chunkInfo.getBuildingType() != null;
	}


	public static class GetLostCities implements Function<ILostCities, Void> {
		@Override
		public Void apply(ILostCities lc) {
			lostCities = lc;
			return null;
		}
	}
}
