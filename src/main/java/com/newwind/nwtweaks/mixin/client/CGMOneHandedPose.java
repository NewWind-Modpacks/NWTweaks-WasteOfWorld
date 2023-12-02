package com.newwind.nwtweaks.mixin.client;

import com.mrcrayfish.guns.client.render.pose.OneHandedPose;
import com.newwind.nwtweaks.util.MixinExternalFunctions;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(OneHandedPose.class)
public class CGMOneHandedPose {

	@Redirect(
					method = "applyPlayerModelRotation",
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
