package com.newwind.nwtweaks.mixin;

import com.newwind.nwtweaks.NWConfig;
import com.newwind.nwtweaks.util.CommonUtils;
import com.newwind.nwtweaks.util.DrownUtil;
import com.newwind.nwtweaks.world.items.ProtectiveMask;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.fluids.FluidType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(LivingEntity.class)
public class MinecraftLivingEntity {

	@Redirect(
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
	}

}
