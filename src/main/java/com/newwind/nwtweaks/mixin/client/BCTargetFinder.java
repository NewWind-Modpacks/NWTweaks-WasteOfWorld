package com.newwind.nwtweaks.mixin.client;

import com.newwind.nwtweaks.registries.Attributes;
import net.bettercombat.client.collision.TargetFinder;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(TargetFinder.class)
public class BCTargetFinder {

	@Unique
	private static Player nWTweaks$capturedPlayer;

	@ModifyVariable(
					method = "findAttackTargetResult",
					at = @At(
									value = "HEAD"
					),
					argsOnly = true,
					remap = false
	)
	private static Player getPlayer(Player player) {
		nWTweaks$capturedPlayer = player;
		return player;
	}

	@ModifyVariable(
					method = "findAttackTargetResult",
					at = @At(
									value = "HEAD"
					),
					argsOnly = true,
					remap = false
	)
	private static double applyRangeAttribute(double attackRange) {
		return attackRange * nWTweaks$capturedPlayer.getAttributeValue(Attributes.BC_ATTACK_RANGE.get());
	}

}
