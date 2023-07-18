package com.newwind.nwtweaks.mixin.client;

import com.newwind.nwtweaks.NWConfig;
import com.newwind.nwtweaks.NWTweaks;
import net.minecraft.client.renderer.GameRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(net.minecraft.client.KeyboardHandler.class)
public class KeyboardHandler {

//	@Redirect(
//					method = "keyPress",
//					at = @At(
//									value = "INVOKE",
//									target = "Lnet/minecraft/client/renderer/GameRenderer;togglePostEffect()V"
//					),
//					remap = false,
//					require = 1
//	)
//	private static void onPressTogglePost(GameRenderer instance) {
//		if (!NWConfig.Client.BLOCK_SHADER_TOGGLE.get())
//			instance.togglePostEffect();
//	}

}
