package com.newwind.nwtweaks.mixin.client;

import net.minecraft.client.player.LocalPlayer;
import net.minecraft.sounds.SoundEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import roon.hitmarker.HitMarkerClient;

@Mixin(HitMarkerClient.class)
public class HitMarkerClientConstructor {

	@Shadow(remap = false)
	public static boolean kill;

	@Inject(
					method = "receiveHit",
					at = @At(
									"HEAD"
					),
					remap = false
	)
	private static void updateKillEarly(boolean killed, CallbackInfo ci) {
		kill = killed;
	}

	@Redirect(
					method = "receiveHit",
					at = @At(
									value = "INVOKE",
									target = "Lnet/minecraft/client/player/LocalPlayer;playSound(Lnet/minecraft/sounds/SoundEvent;FF)V"
					),
					remap = false
	)
	private static void playOnKill(LocalPlayer instance, SoundEvent soundEvent, float volume, float pitch) {
		if (kill)
			instance.playSound(soundEvent, volume, pitch);
	}
}
