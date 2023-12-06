package com.newwind.nwtweaks.mixin.client;

import com.newwind.nwtweaks.NWTweaks;
import net.diebuddies.render.shader.ShaderResourceProvider;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;


@Mixin(ShaderResourceProvider.class)
public abstract class PhysicsShaderResourceProvider {

	@Shadow
	private static boolean isResourceUrlValid(String string, @Nullable URL url) throws IOException {
		return true;
	}

	@SuppressWarnings("DataFlowIssue")
	@Inject(
					method = "processResourceAsStream",
					at = @At("HEAD"),
					cancellable = true,
					remap = false
	)
	private void replaceShader(ResourceLocation resourceLocation, CallbackInfoReturnable<InputStream> cir) {
		if (resourceLocation.getPath().equals("shaders/include/fog.glsl")) {
			String path = "/assets/" + NWTweaks.MODID + "/" + resourceLocation.getPath();

			try {
				URL url = NWTweaks.class.getResource(path);
				cir.setReturnValue(isResourceUrlValid(path, url) ? url.openStream() : null);
			} catch (IOException var5) {
				cir.setReturnValue(ShaderResourceProvider.class.getResourceAsStream(path));
			}
		}
	}

}
