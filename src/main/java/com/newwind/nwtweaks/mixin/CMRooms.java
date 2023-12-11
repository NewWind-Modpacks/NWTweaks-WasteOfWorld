package com.newwind.nwtweaks.mixin;

import dev.compactmods.machines.api.room.RoomSize;
import dev.compactmods.machines.room.Rooms;
import dev.compactmods.machines.room.data.CompactRoomData;
import dev.compactmods.machines.upgrade.MachineRoomUpgrades;
import dev.compactmods.machines.upgrade.RoomUpgradeManager;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.ChunkPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.UUID;

@Mixin(Rooms.class)
public class CMRooms {

	@Inject(
					method = "createNew",
					at = @At("TAIL"),
					locals = LocalCapture.CAPTURE_FAILHARD,
					remap = false
	)
	private static void forceChunkloading(MinecraftServer serv, RoomSize size, UUID owner, CallbackInfoReturnable<ChunkPos> cir, ServerLevel compactWorld, CompactRoomData rooms, int nextPosition, Vec3i location, int centerY, BlockPos newCenter, ChunkPos machineChunk) {
		RoomUpgradeManager.get(compactWorld).addUpgrade(MachineRoomUpgrades.CHUNKLOAD.get(), machineChunk);
	}

}
