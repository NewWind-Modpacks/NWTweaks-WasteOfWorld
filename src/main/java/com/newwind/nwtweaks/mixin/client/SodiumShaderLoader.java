package com.newwind.nwtweaks.mixin.client;

import com.newwind.nwtweaks.NWTweaks;
import me.jellysquid.mods.sodium.client.gl.shader.ShaderLoader;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import org.apache.commons.io.IOUtils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.NoSuchElementException;

@Mixin(ShaderLoader.class)
public class SodiumShaderLoader {

	@SuppressWarnings("OptionalGetWithoutIsPresent")
	@Inject(
					method = "getShaderSource",
					at = @At("HEAD"),
					cancellable = true,
					remap = false
	)
	private static void replaceShader(ResourceLocation name, CallbackInfoReturnable<String> cir) {
		if (name.toString().equals("sodium:include/fog.glsl")) {
			try (InputStream shaderIn = Minecraft.getInstance().getResourceManager().getResource(
							new ResourceLocation(NWTweaks.MODID, "shaders/include/sodium_fog.glsl")
			).get().open()) {

				cir.setReturnValue(IOUtils.toString(shaderIn, StandardCharsets.UTF_8));

			} catch (IOException e) {
				NWTweaks.LOGGER.error(e.toString());
			} catch (NoSuchElementException e) {
				NWTweaks.LOGGER.error("Fog shader is not present!");
			}
		}
	}

}
