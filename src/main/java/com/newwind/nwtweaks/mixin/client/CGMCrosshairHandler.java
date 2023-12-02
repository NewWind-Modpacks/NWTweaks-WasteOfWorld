package com.newwind.nwtweaks.mixin.client;

import com.mrcrayfish.guns.client.handler.CrosshairHandler;
import com.newwind.nwtweaks.util.MixinExternalFunctions;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(CrosshairHandler.class)
public class CGMCrosshairHandler {

	@Redirect(
					method = "onRenderOverlay",
					at = @At(
									value = "INVOKE",
									target = "Lnet/minecraft/world/item/ItemStack;getItem()Lnet/minecraft/world/item/Item;",
									ordinal = 1
					),
					remap = false
	)
	private Item compatibilityShields(ItemStack instance) {
		return MixinExternalFunctions.isStackShield(instance);
	}

}
