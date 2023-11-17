package com.newwind.nwtweaks.mixin;

import com.newwind.nwtweaks.NWConfig;
import net.minecraft.world.item.CrossbowItem;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(CrossbowItem.class)
public class MinecraftCrossbowItem {

	@Inject(
					method = "getChargeDuration",
					at = @At(
									value = "RETURN"
					),
					cancellable = true
	)
	private static void applyReloadSpeedMod(ItemStack stack, CallbackInfoReturnable<Integer> cir) {
		String tag = NWConfig.Common.RELOAD_SPEED_TAG.get();
		if (stack.getTag() != null && stack.getTag().contains(tag) && stack.getTag().getBoolean(tag))
			cir.setReturnValue((int) (cir.getReturnValue() * (1 - NWConfig.Common.RELOAD_SPEED_MULTIPLIER.get())));
	}

}
