package com.newwind.nwtweaks.mixin.client;

import com.divot.mgsaiming.MgsAimingHandler;
import com.mojang.blaze3d.platform.InputConstants;
import com.mrcrayfish.guns.item.GunItem;
import com.newwind.nwtweaks.util.MixinExternalFunctions;
import net.minecraft.client.Minecraft;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.client.event.InputEvent;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(MgsAimingHandler.class)
public abstract class MGSHandler {

	@Shadow
	public static boolean checkItem(ItemStack item) {
		return false;
	}

	@Shadow @Final private static Minecraft mc;

	@Redirect(
					method = "unaimEvent",
					at = @At(
									value = "INVOKE",
									target = "Lnet/minecraftforge/client/event/InputEvent$MouseButton$Post;getAction()I"
					),
					remap = false
	)
	private static int ensureIsAimButton(InputEvent.MouseButton.Post instance) {
		Minecraft mc = Minecraft.getInstance();
		InputConstants.Key key = mc.options.keyUse.getKey();
		if (key.getType().equals(InputConstants.Type.MOUSE)
						&& key.getValue() == instance.getButton())
			return 0;
		return 1;
	}

	@Redirect(
					method = "aimEvent",
					at = @At(
									value = "INVOKE",
									target = "Lcom/divot/mgsaiming/MgsAimingHandler;checkItem(Lnet/minecraft/world/item/ItemStack;)Z"
					),
					remap = false
	)
	private static boolean cancelIfOffhandHasShield(ItemStack stack) {
		if (mc.player != null && stack.getItem() instanceof GunItem && MixinExternalFunctions.isStackShield(mc.player.getOffhandItem()) == Items.SHIELD)
			return false;

		return checkItem(stack);
	}

}
