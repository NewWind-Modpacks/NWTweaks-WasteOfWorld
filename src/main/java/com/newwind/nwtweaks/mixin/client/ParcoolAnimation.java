package com.newwind.nwtweaks.mixin.client;

import com.alrex.parcool.common.capability.impl.Animation;
import com.alrex.parcool.config.ParCoolConfig;
import com.newwind.nwtweaks.util.MixinExternalFunctions;
import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(Animation.class)
public class ParcoolAnimation {

	@Redirect(
					method = "cameraSetup",
					at = @At(
									value = "INVOKE",
									target = "Lcom/alrex/parcool/config/ParCoolConfig$Client$Booleans;get()Ljava/lang/Boolean;"
					),
					remap = false
	)
	public Boolean negateParcool(ParCoolConfig.Client.Booleans instance) {
		Minecraft mc = Minecraft.getInstance();

		if (mc.player != null && MixinExternalFunctions.hasAnimationDisablerStack(mc.player))
			return false;
		return instance.get();
	}

}
