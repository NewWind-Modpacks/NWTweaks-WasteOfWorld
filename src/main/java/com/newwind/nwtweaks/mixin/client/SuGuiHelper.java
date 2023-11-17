package com.newwind.nwtweaks.mixin.client;

import com.newwind.nwtweaks.client.gui.ThirstClassicBar;
import com.stereowalker.survive.GuiHelper;
import net.minecraftforge.client.event.RegisterGuiOverlaysEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import tfar.classicbar.EventHandler;

@Mixin(GuiHelper.class)
public class SuGuiHelper {

	@Inject(
					method = "registerOverlays",
					at = @At(
									value = "INVOKE",
									target = "Lnet/minecraftforge/client/event/RegisterGuiOverlaysEvent;registerAbove(Lnet/minecraft/resources/ResourceLocation;Ljava/lang/String;Lnet/minecraftforge/client/gui/overlay/IGuiOverlay;)V",
									ordinal = 0
					),
					cancellable = true,
					remap = false
	)
	private static void registerThirstAsClassic(RegisterGuiOverlaysEvent event, CallbackInfo ci) {
		EventHandler.register(new ThirstClassicBar());
		ci.cancel();
	}

}
