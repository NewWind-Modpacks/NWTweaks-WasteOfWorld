package com.newwind.nwtweaks.mixin;

import com.newwind.nwtweaks.NWConfig;
import com.newwind.nwtweaks.capability.IsUndergroundProvider;
import com.newwind.nwtweaks.capability.RedDwellerProvider;
import com.newwind.nwtweaks.networking.ModMessages;
import com.newwind.nwtweaks.networking.packet.S2CRedDweller;
import de.cadentem.cave_dweller.util.Utils;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.SpawnUtil;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.network.PacketDistributor;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.Optional;

@Mixin(Utils.class)
public class CaveDwellerUtils {

	@Inject(
					method = "trySpawnMob",
					at = @At(
									value = "INVOKE",
									target = "Lnet/minecraft/server/level/ServerLevel;addFreshEntityWithPassengers(Lnet/minecraft/world/entity/Entity;)V"
					),
					remap = false,
					locals = LocalCapture.CAPTURE_FAILHARD
	)
	private static void onNewDweller(@NotNull Entity currentVictim, EntityType<? extends Mob> entityType, MobSpawnType spawnType, ServerLevel level, BlockPos blockPosition, int attempts, int xzOffset, int yOffset, SpawnUtil.Strategy strategy, CallbackInfoReturnable<Optional<? extends Mob>> cir, BlockPos.MutableBlockPos mutableBlockPosition, int i, int xOffset, int zOffset, Mob dweller, boolean isValidSpawn) {
		if (currentVictim instanceof Player player) {
			player.getCapability(IsUndergroundProvider.CAPABILITY).ifPresent(isUnderground -> {
				if (isUnderground.getTime() > NWConfig.Common.RED_DWELLER_SPAWN_TIME.get()) {
					dweller.getCapability(RedDwellerProvider.CAPABILITY).ifPresent(redDweller -> {
						redDweller.setRedDweller(true);
						ModMessages.INSTANCE.send(PacketDistributor.TRACKING_ENTITY.with(() -> dweller), new S2CRedDweller(dweller.getId(), redDweller.isRedDweller()));
					});
				}
			});
		}
	}

}
