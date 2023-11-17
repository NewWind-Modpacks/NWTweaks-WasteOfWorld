package com.newwind.nwtweaks.mixin.client;

import com.mojang.blaze3d.vertex.PoseStack;
import fuzs.overflowingbars.client.handler.RowCountRenderer;
import net.minecraft.client.gui.Font;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(RowCountRenderer.class)
public class OBRowCountRenderer {

	@Inject(
					method = "drawBarRowCount(Lcom/mojang/blaze3d/vertex/PoseStack;IIIZLnet/minecraft/client/gui/Font;)V",
					at = @At(
									value = "HEAD"
					),
					cancellable = true,
					remap = false)
	private static void atDrawRowCount(PoseStack poseStack, int posX, int posY, int barValue, boolean left, Font font, CallbackInfo ci) {
		StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();
		if (stackTraceElements[3].getClassName().equals("com.elenai.feathers.client.gui.FeathersHudOverlay"))
			ci.cancel();
	}

}
