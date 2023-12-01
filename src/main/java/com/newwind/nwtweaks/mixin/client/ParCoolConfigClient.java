package com.newwind.nwtweaks.mixin.client;

import com.alrex.parcool.config.ParCoolConfig;
import com.newwind.nwtweaks.util.MixinExternalFunctions;
import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ParCoolConfig.Client.Booleans.class)
public abstract class ParCoolConfigClient {

	@Shadow public abstract Object get();

	@Inject(
					method = "get()Ljava/lang/Boolean;",
					at = @At("HEAD"),
					cancellable = true,
					remap = false
	)
	private void negateParcool(CallbackInfoReturnable<Boolean> cir) {
		//noinspection EqualsBetweenInconvertibleTypes
		if (!this.equals(ParCoolConfig.Client.Booleans.EnableFPVAnimation))
			return;

		Minecraft mc = Minecraft.getInstance();
		StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();
		if (stackTraceElements[3].getMethodName().contains("$onSetupAnim")
						&& mc.player != null
						&& MixinExternalFunctions.hasAnimationDisablerStack(mc.player))
			cir.setReturnValue(false);

	}

}
