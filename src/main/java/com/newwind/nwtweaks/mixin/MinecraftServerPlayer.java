package com.newwind.nwtweaks.mixin;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.GameRules;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ServerPlayer.class)
public class MinecraftServerPlayer {

	@Redirect(
					method = "restoreFrom",
					at = @At(
									value = "INVOKE",
									target = "Lnet/minecraft/world/level/GameRules;getBoolean(Lnet/minecraft/world/level/GameRules$Key;)Z"
					)
	)
	private boolean forceKeepInventoryCodeBlock(GameRules instance, GameRules.Key<GameRules.BooleanValue> p_46208_) {
		return true;
	}

}
