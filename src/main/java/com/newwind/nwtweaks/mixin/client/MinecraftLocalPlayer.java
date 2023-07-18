package com.newwind.nwtweaks.mixin.client;

import com.ccr4ft3r.actionsofstamina.config.AoSAction;
import com.ccr4ft3r.actionsofstamina.config.ProfileConfig;
import net.minecraft.client.player.LocalPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = LocalPlayer.class)
public abstract class MinecraftLocalPlayer {

	@Inject(
					method = "isHandsBusy",
					at = @At("HEAD"),
					cancellable = true
	)
	private void swingFeathers(CallbackInfoReturnable<Boolean> cir) {
		String caller = Thread.currentThread().getStackTrace()[3].getMethodName();
		if (caller.equals("startUpswing") && ProfileConfig.shouldStop(AoSAction.ATTACKING)) {
			cir.setReturnValue(true);
			cir.cancel();
		}
	}

}
