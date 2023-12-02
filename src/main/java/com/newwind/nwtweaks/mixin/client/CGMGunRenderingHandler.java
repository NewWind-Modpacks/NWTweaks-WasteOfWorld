package com.newwind.nwtweaks.mixin.client;

import com.mrcrayfish.guns.client.handler.GunRenderingHandler;
import com.newwind.nwtweaks.util.MixinExternalFunctions;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(GunRenderingHandler.class)
public class CGMGunRenderingHandler {

	@Redirect(
					method = "applyShieldTransforms",
					at = @At(
									value = "INVOKE",
									target = "Lnet/minecraft/world/item/ItemStack;getItem()Lnet/minecraft/world/item/Item;"
					),
					remap = false
	)
	private Item compatibilityShields(ItemStack instance) {
		return MixinExternalFunctions.isStackShield(instance);
	}

}
