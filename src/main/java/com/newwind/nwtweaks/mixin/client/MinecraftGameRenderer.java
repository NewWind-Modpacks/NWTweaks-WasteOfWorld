package com.newwind.nwtweaks.mixin.client;

import com.newwind.nwtweaks.NWConfig;
import com.newwind.nwtweaks.NWTweaks;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GameRenderer.class)
public abstract class MinecraftGameRenderer {

	@Shadow
	private boolean effectActive;

	private static final ResourceLocation winterShader = new ResourceLocation(NWTweaks.MODID, "shaders/post/winter.json");

	@Inject(
					method = "checkEntityPostEffect",
					at = @At(
									value = "TAIL"
					)
	)
	public void applyShader(Entity p_109107_, CallbackInfo ci) {
		Minecraft mc = Minecraft.getInstance();
		mc.gameRenderer.loadEffect(winterShader);
	}

	/**
	 * @author Kevadroz
	 * @reason Implement block option
	 */
	@Overwrite
	public void togglePostEffect() {
		if (!NWConfig.Client.BLOCK_SHADER_TOGGLE.get())
			this.effectActive = !this.effectActive;
	}

}
