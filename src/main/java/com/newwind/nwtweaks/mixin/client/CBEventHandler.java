package com.newwind.nwtweaks.mixin.client;

import com.newwind.nwtweaks.event.client.ClientEvents;
import net.minecraftforge.client.gui.overlay.ForgeGui;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import tfar.classicbar.EventHandler;

@Mixin(EventHandler.class)
public class CBEventHandler {

	@Inject(
					method = "increment",
					at = @At("TAIL"),
					remap = false
	)
	private static void updateItemNameHeight(ForgeGui gui, boolean side, int amount, CallbackInfo ci) {
		ClientEvents.updateNameDrawHeight(gui.leftHeight, gui.rightHeight);
	}

}
