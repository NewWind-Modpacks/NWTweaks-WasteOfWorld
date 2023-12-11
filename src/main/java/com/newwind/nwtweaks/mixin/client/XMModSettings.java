package com.newwind.nwtweaks.mixin.client;

import com.newwind.nwtweaks.util.CommonUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import xaero.common.settings.ModSettings;

@Mixin(ModSettings.class)
public class XMModSettings {

	@Inject(
					method = "getMinimap",
					at = @At("HEAD"),
					cancellable = true,
					remap = false
	)
	private void disableUnderground(CallbackInfoReturnable<Boolean> cir) {
		Minecraft mc = Minecraft.getInstance();
		LocalPlayer player = mc.player;
		if (player != null && CommonUtils.isUnderground(player))
			cir.setReturnValue(false);
	}

	@Inject(
					method = "getShowIngameWaypoints",
					at = @At("HEAD"),
					cancellable = true,
					remap = false
	)
	private void disableWaypointsUnderground(CallbackInfoReturnable<Boolean> cir) {
		Minecraft mc = Minecraft.getInstance();
		LocalPlayer player = mc.player;
		if (player != null && CommonUtils.isUnderground(player))
			cir.setReturnValue(false);
	}
}
