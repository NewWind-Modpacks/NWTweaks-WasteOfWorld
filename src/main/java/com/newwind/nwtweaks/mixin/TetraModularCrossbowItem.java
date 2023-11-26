package com.newwind.nwtweaks.mixin;

import com.newwind.nwtweaks.NWConfig;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import se.mickelus.tetra.items.modular.impl.crossbow.ModularCrossbowItem;

@Mixin(ModularCrossbowItem.class)
public class TetraModularCrossbowItem {



	@Inject(
					method = "getReloadDuration",
					at = @At(
									value = "RETURN"
					),
					cancellable = true,
					remap = false
	)
	private void applyReloadSpeedMod(ItemStack stack, CallbackInfoReturnable<Integer> cir) {
		if (stack.getTag() != null && stack.getTag().getBoolean(NWConfig.Common.RELOAD_SPEED_TAG.get()))
			cir.setReturnValue((int) (cir.getReturnValue() * (1.0D - NWConfig.Common.RELOAD_SPEED_MULTIPLIER.get())));
	}

}
