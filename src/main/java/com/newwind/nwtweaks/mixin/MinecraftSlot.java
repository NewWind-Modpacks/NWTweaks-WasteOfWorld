package com.newwind.nwtweaks.mixin;

import com.newwind.nwtweaks.access.IRestrictedContainer;
import net.minecraft.world.Container;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Slot.class)
public class MinecraftSlot {

	@Shadow @Final public Container container;

	@Inject(
					method = "mayPlace",
					at = @At("HEAD"),
					cancellable = true
	)
	private void checkContainerType(ItemStack p_40231_, CallbackInfoReturnable<Boolean> cir) {
		if (this.container instanceof IRestrictedContainer restrictedContainer
						&& !restrictedContainer.nWTweaks$canPlaceItemsInside())
			cir.setReturnValue(false);
	}

}
