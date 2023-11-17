package com.newwind.nwtweaks.mixin;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(dev.gigaherz.eyes.entity.EyesEntity.class)
public class EyesEntity {

	@Redirect(
					method = "doHurtTarget",
					at = @At(
									value = "INVOKE",
									target = "Lnet/minecraft/world/entity/LivingEntity;addEffect(Lnet/minecraft/world/effect/MobEffectInstance;)Z"
					)
	)
	private boolean onMobEffectInit(LivingEntity instance, MobEffectInstance effect) {
		MobEffectInstance witherEffect = new MobEffectInstance(MobEffects.WITHER, effect.getDuration(), effect.getAmplifier());
		return instance.addEffect(witherEffect);
	}

}
