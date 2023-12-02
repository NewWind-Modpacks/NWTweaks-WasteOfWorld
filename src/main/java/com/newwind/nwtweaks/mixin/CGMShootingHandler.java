package com.newwind.nwtweaks.mixin;

import com.mrcrayfish.guns.client.handler.ShootingHandler;
import com.newwind.nwtweaks.util.MixinExternalFunctions;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ShootingHandler.class)
public class CGMShootingHandler {

	@Redirect(
					method = "onMouseClick",
					at = @At(
									value = "INVOKE",
									target = "Lnet/minecraft/world/item/ItemStack;getItem()Lnet/minecraft/world/item/Item;",
									ordinal = 2
					),
					remap = false
	)
	private Item compatibilityShields(ItemStack instance) {
		return MixinExternalFunctions.isStackShield(instance);
	}

}
