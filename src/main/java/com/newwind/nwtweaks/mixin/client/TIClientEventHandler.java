package com.newwind.nwtweaks.mixin.client;

import net.minecraft.client.Minecraft;
import net.minecraftforge.client.event.RenderGuiOverlayEvent;
import nuparu.tinyinv.events.ClientEventHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientEventHandler.class)
public class TIClientEventHandler {

	@Inject(
					method = "onOverlayRenderPre",
					at = @At("HEAD"),
					cancellable = true,
					remap = false
	)
	private static void preSpectator(RenderGuiOverlayEvent.Pre event, CallbackInfo ci) {
		if (Minecraft.getInstance().player != null && Minecraft.getInstance().player.isSpectator())
			ci.cancel();
	}

	@Inject(
					method = "onOverlayRenderPost",
					at = @At("HEAD"),
					cancellable = true,
					remap = false
	)
	private static void postSpectator(RenderGuiOverlayEvent.Post event, CallbackInfo ci) {
		if (Minecraft.getInstance().player != null && Minecraft.getInstance().player.isSpectator())
			ci.cancel();
	}

}
