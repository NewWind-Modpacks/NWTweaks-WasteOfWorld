package com.newwind.nwtweaks.mixin;

import com.mrcrayfish.guns.util.GunEnchantmentHelper;
import com.newwind.nwtweaks.NWConfig;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(GunEnchantmentHelper.class)
public class GunModGunEnchantmentHelper {

	@Inject(
					method = "getReloadInterval",
					at = @At(
									value = "RETURN"
					),
					cancellable = true,
					remap = false
	)
	private static void applyReloadSpeedMod(ItemStack weapon, CallbackInfoReturnable<Integer> cir) {
		CompoundTag tag = weapon.getTag();
		if (tag != null && tag.getBoolean(NWConfig.Common.RELOAD_SPEED_TAG.get()))
			cir.setReturnValue((int) Math.max(cir.getReturnValue() * (1.0D - NWConfig.Common.RELOAD_SPEED_MULTIPLIER.get()), 1));
	}

}
