package com.newwind.nwtweaks.mixin;

import com.newwind.nwtweaks.registries.Attributes;
import net.minecraft.world.damagesource.CombatRules;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.IndirectEntityDamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public class MinecraftLivingEntity {

	@Unique
	private DamageSource nw_ap_capturedDamageSource;

	/*@Redirect(
					method = "baseTick",
					at = @At(
									value = "INVOKE",
									target = "Lnet/minecraft/world/entity/LivingEntity;getEyeInFluidType()Lnet/minecraftforge/fluids/FluidType;",
									ordinal = 0,
									remap = false
					)
	)
	private FluidType shouldDrown(LivingEntity instance) {
		if (NWConfig.Common.ENABLE_TOXIC_GAS.get() && instance instanceof Player player && CommonUtils.isInCave(player)) {
			DrownUtil.handlePlayerDrown(player);
			return ForgeMod.EMPTY_TYPE.get();
		}
		return instance.getEyeInFluidType();
	}

	@Redirect(
					method = "baseTick",
					at = @At(
									value = "INVOKE",
									target = "Lnet/minecraft/world/entity/LivingEntity;getEyeInFluidType()Lnet/minecraftforge/fluids/FluidType;",
									ordinal = 3,
									remap = false
					)
	)
	private FluidType shouldBreathe(LivingEntity instance) {
		if (NWConfig.Common.ENABLE_TOXIC_GAS.get() && instance instanceof Player player) {
			ItemStack mask = ProtectiveMask.getMaskStack(player);
			if (CommonUtils.isInCave((Player) instance)) {
				if (mask != null) {
					ProtectiveMask.modifyAir(mask, -1);
					return ForgeMod.EMPTY_TYPE.get();
				}
				return ForgeMod.WATER_TYPE.get();
			}
			if (mask != null)
				ProtectiveMask.refill(mask);
		}
		return instance.getEyeInFluidType();
	}

	@Redirect(
					method = "baseTick",
					at = @At(
									value = "INVOKE",
									target = "Lnet/minecraft/world/entity/LivingEntity;getMaxAirSupply()I"
					)
	)
	private int forceFluidCheck(LivingEntity instance) {
		if (NWConfig.Common.ENABLE_TOXIC_GAS.get() && instance instanceof Player player) {
			ItemStack mask = ProtectiveMask.getMaskStack(player);
			if (mask != null)
				return Integer.MAX_VALUE;
		}
		return instance.getMaxAirSupply();
	}*/

	@Inject(
					method = "actuallyHurt",
					at = @At(
									value = "INVOKE",
									target = "Lnet/minecraft/world/entity/LivingEntity;getDamageAfterArmorAbsorb(Lnet/minecraft/world/damagesource/DamageSource;F)F"
					)
	)
	public void captureDamageSource(DamageSource damageSource, float damage, CallbackInfo ci) {
		nw_ap_capturedDamageSource = damageSource;
	}

	@Redirect(
					method = "getDamageAfterArmorAbsorb",
					at = @At(
									value = "INVOKE",
									target = "Lnet/minecraft/world/damagesource/CombatRules;getDamageAfterAbsorb(FFF)F"
					)
	)
	public float applyArmorPiercing(float damage, float armor, float armorToughness) {
		var source = nw_ap_capturedDamageSource;
		if (source instanceof IndirectEntityDamageSource
						&& source.getEntity() instanceof Player) {
			double piercing = 1 - ((Player) source.getEntity()).getAttributeValue(Attributes.RANGED_ARMOR_PIERCING.get());
			armor *= piercing;
			armorToughness *= piercing;
		}
		return CombatRules.getDamageAfterAbsorb(damage, armor, armorToughness);
	}

}
