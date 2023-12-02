package com.newwind.nwtweaks.mixin.client;

import com.divot.mgsaiming.MgsAimingHandler;
import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.Minecraft;
import net.minecraftforge.client.event.InputEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(MgsAimingHandler.class)
public class MGSHandler {

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

}
