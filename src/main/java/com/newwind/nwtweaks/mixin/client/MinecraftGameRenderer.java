package com.newwind.nwtweaks.mixin.client;

import com.newwind.nwtweaks.NWConfig;
import com.newwind.nwtweaks.access.IPostProcessRenderizable;
import com.newwind.nwtweaks.client.NWClient;
import croissantnova.sanitydim.client.PostProcessor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
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

//	@Unique
//	private static final ResourceLocation nWTweaks$winterShader = new ResourceLocation(NWTweaks.MODID, "shaders/post/winter.json");
//
//	@Inject(
//					method = "checkEntityPostEffect",
//					at = @At(
//									value = "TAIL"
//					)
//	)
//	public void applyShader(Entity p_109107_, CallbackInfo ci) {
//		Minecraft mc = Minecraft.getInstance();
//		mc.gameRenderer.loadEffect(nWTweaks$winterShader);
//	}

	@Inject(
					method = "render(FJZ)V",
					at = @At(
									value = "INVOKE",
									target = "Lcom/mojang/blaze3d/pipeline/RenderTarget;bindWrite(Z)V"
					)
	)
	private void nWTweaks$postProcess(float pPartialTicks, long pFinishTimeNano, boolean pRenderLevel, CallbackInfo ci) {
		GameRenderer renderer = (GameRenderer) (Object) this;
		if (renderer != null && pRenderLevel && renderer.getMinecraft().level != null) {
			if (NWClient.postProcessor == null) {
				NWClient.postProcessor = new PostProcessor();
				NWClient.postProcessor.addSinglePassEntry("winter", pass ->
				{
					float transitionValue = 1F;
					if (NWClient.undergroundTransitionValue > 0F)
						transitionValue = 1F - NWClient.undergroundTransitionValue;
					pass.getEffect().safeGetUniform("DesaturateFactor").set(NWConfig.Client.DESATURATION_FACTOR.get().floatValue() * transitionValue);
					pass.getEffect().safeGetUniform("Gray").set(NWConfig.Client.BRIGHTNESS_FACTOR.get().floatValue() * transitionValue);
					return true;
				});
			}
			if (NWClient.postProcessor != null)
				((IPostProcessRenderizable) (Object) NWClient.postProcessor).nWTweaks$alwaysRender(pPartialTicks);
		}
	}

	@Inject(method = "resize(II)V", at = @At("TAIL"))
	private void resize(int pWidth, int pHeight, CallbackInfo ci) {
		if (NWClient.postProcessor != null)
			NWClient.postProcessor.resize(pWidth, pHeight);
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
