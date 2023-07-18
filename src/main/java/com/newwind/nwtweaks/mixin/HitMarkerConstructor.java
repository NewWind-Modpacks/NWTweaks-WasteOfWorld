package com.newwind.nwtweaks.mixin;

import com.mrcrayfish.guns.entity.DamageSourceProjectile;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import roon.hitmarker.HitMarker;

@Mixin(HitMarker.class)
public class HitMarkerConstructor {

	@Inject(
					method = "hit",
					at = @At("HEAD"),
					cancellable = true,
					remap = false
	)
	private void onHit(LivingDamageEvent event, CallbackInfo ci) {
		if (!isBullet(event.getSource()))
			ci.cancel();
	}

	@Inject(
					method = "death",
					at = @At("HEAD"),
					cancellable = true,
					remap = false
	)
	private void onDeath(LivingDeathEvent event, CallbackInfo ci) {
		if (!isBullet(event.getSource()))
			ci.cancel();
	}

	private boolean isBullet(DamageSource source) {
		return (source instanceof DamageSourceProjectile);
	}

}
