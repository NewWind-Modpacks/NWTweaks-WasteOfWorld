package com.newwind.nwtweaks.mixin.client;

import com.bawnorton.mixinsquared.TargetHandler;
import com.ccr4ft3r.actionsofstamina.config.AoSAction;
import com.ccr4ft3r.actionsofstamina.config.ProfileConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@SuppressWarnings({"MixinAnnotationTarget", "InvalidMemberReference", "UnresolvedMixinReference"})
@Mixin(value = Minecraft.class, priority = 1500)
public class MixinBCMinecraftClientInject {

	@TargetHandler(
					mixin = "net.bettercombat.mixin.client.MinecraftClientInject",
					name = "startUpswing"
	)
	@Redirect(
		method = "@MixinSquared:Handler",
					at = @At(
									value = "INVOKE",
									target = "Lnet/minecraft/client/player/LocalPlayer;isHandsBusy()Z"
					)
	)
	private boolean swingFeathers(LocalPlayer player) {
		return ProfileConfig.shouldStop(AoSAction.ATTACKING);
	}

}
