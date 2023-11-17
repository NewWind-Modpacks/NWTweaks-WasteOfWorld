package com.newwind.nwtweaks.util;

import com.mrcrayfish.backpacked.Backpacked;
import com.newwind.nwtweaks.NWConfig;
import com.newwind.nwtweaks.capability.IsUndergroundProvider;
import com.newwind.nwtweaks.client.NWClient;
import com.newwind.nwtweaks.registries.Enchantments;
import mcjty.lostcities.api.ILostChunkInfo;
import mcjty.lostcities.api.ILostCities;
import mcjty.lostcities.api.ILostCityInformation;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.ModList;
import nuparu.tinyinv.config.ServerConfig;
import nuparu.tinyinv.utils.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Function;

public class CommonUtils {
	private static boolean registered = false;
	private static ILostCities lostCities;
	public static final String AIR_BLADDER_TAG_OXYGEN_AMOUNT = "oxygen_amount";

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

	public static boolean isUnderground(LivingEntity living) {
		if (living.level.isClientSide && living instanceof LocalPlayer)
			return NWClient.isUnderground;
		AtomicBoolean isUnderground = new AtomicBoolean(false);
		living.getCapability(IsUndergroundProvider.IS_UNDERGROUND).ifPresent(undergroundObject -> {
			isUnderground.set(undergroundObject.isUnderground());
		});
		return isUnderground.get();
	}

	public static boolean isUnderground(Entity entity) {
		if (entity instanceof LivingEntity living)
			return isUnderground(living);
		else return isUnderground(entity.getLevel(), new BlockPos(entity.getEyePosition()));
	}

	public static boolean checkUnderground(Entity entity) {
		if (!(entity instanceof Player player) || !player.isSpectator())
			return isUnderground(entity.getLevel(), new BlockPos(entity.getEyePosition()));
		return false;
	}

	public static boolean isUnderground(Level level, BlockPos blockPos) {
		int y = blockPos.getY();
		if ( isInLostCityBuilding(level, blockPos) )
			return false;

		if ( y < NWConfig.Common.UNDERGROUND_INNER_HEIGHT.get() )
			return true;

		int y_top = NWConfig.Common.UNDERGROUND_OUTER_HEIGHT.get();
		if ( y > y_top || level.getBrightness(LightLayer.SKY, blockPos) > 0 )
			return false;

		int y_offset = Math.max(NWConfig.Common.UNDERGROUND_HEIGHT_OFFSET.get(), 1);
		for (BlockPos i = blockPos.above(y_offset);
		     i.getY() <= y_top || !level.canSeeSky(i);
		     i = i.above()
		)
			if (level.getBlockState(i).getBlock().equals(Blocks.STONE))
				return true;

		return false;
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
			if (!(block.getBlock() == Blocks.AIR || block.getBlock() == Blocks.CAVE_AIR || block.getBlock() == Blocks.VOID_AIR))
				continue;

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

	public static boolean isInLostCityBuilding(Level level, BlockPos blockPos) {
		if (lostCities == null) return false;
		ILostCityInformation info = lostCities.getLostInfo(level);

		if (info == null) return false;
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
