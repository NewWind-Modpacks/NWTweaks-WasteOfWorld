package com.newwind.nwtweaks.mixin.client;

import com.elenai.feathers.client.ClientFeathersData;
import com.elenai.feathers.config.FeathersClientConfig;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.newwind.nwtweaks.util.CommonUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import tfar.classicbar.util.Color;
import tfar.classicbar.util.ModUtils;

@Mixin(targets = "com.elenai.feathers.compat.FeathersBarWrapper$FeathersClassicBar")
public class FeathersClassicBar {

	@Unique
	private float alpha = 1F;

	@Inject(
					method = "shouldRender",
					at = @At(
									value = "HEAD"
					),
					cancellable = true,
					remap = false
	)
	private void shouldRenderApplyAlpha(Player player, CallbackInfoReturnable<Boolean> cir) {
		int fadeCooldown = (Integer) FeathersClientConfig.FADE_COOLDOWN.get();
		int fadeIn = (Integer) FeathersClientConfig.FADE_IN_COOLDOWN.get();
		int fadeOut = (Integer) FeathersClientConfig.FADE_OUT_COOLDOWN.get();

		if ((Boolean) FeathersClientConfig.FADE_WHEN_FULL.get()) {
			if (ClientFeathersData.getFeathers() == ClientFeathersData.getMaxFeathers()) {
				if (ClientFeathersData.getFadeCooldown() == fadeCooldown && alpha > 0.0F) {
					alpha = (double) alpha <= 0.025 ? 0.0F : alpha - 1.0F / (float) fadeOut;
				}
			} else {
				alpha = alpha >= 1.0F ? 1.0F : alpha + 1.0F / (float) fadeIn;
			}
		}

		if (alpha <= 0)
			cir.setReturnValue(false);
	}

	@Redirect(
					method = "renderBar",
					at = @At(
									value = "INVOKE",
									target = "Ltfar/classicbar/util/Color;reset()V"
					),
					remap = false
	)
	private void setBackgroundAlpha() {
		RenderSystem.setShaderColor(1F, 1F, 1F, alpha);
	}

	@Redirect(
					method = "renderBar",
					at = @At(
									value = "INVOKE",
									target = "Ltfar/classicbar/util/Color;color2Gl()V"
					),
					remap = false
	)
	private void setBarColorAlpha(Color instance) {
		instance.color2Gla(alpha);
	}

	@Inject(
					method = "renderIcon",
					at = @At(
									value = "INVOKE",
									target = "Ltfar/classicbar/util/ModUtils;drawTexturedModalRect(Lcom/mojang/blaze3d/vertex/PoseStack;DIIIDI)V"
					),
					remap = false
	)
	private void setIconColorAlpha(PoseStack stack, Player player, int width, int height, int vOffset, CallbackInfo ci) {
		RenderSystem.enableBlend();
		RenderSystem.setShaderColor(1F, 1F, 1F, alpha);
	}

	@Redirect(
					method = "renderIcon",
					at = @At(
									value = "INVOKE",
									target = "Ltfar/classicbar/util/ModUtils;drawTexturedModalRect(Lcom/mojang/blaze3d/vertex/PoseStack;DIIIDI)V"
					),
					remap = false
	)
	// Does not work
	private void setIconIfItsCold(PoseStack stack, double x, int y, int textureX, int textureY, double width, int height) {
		if (ClientFeathersData.isCold())
			textureY = CommonUtils.getEffectivePlayerTemperature(Minecraft.getInstance().player) < 0F ? 18 : 27;
		ModUtils.drawTexturedModalRect(stack, x, y, textureX, textureY, width, height);
	}

	@Redirect(
					method = "renderText",
					at = @At(
									value = "INVOKE",
									target = "Ljava/lang/Integer;decode(Ljava/lang/String;)Ljava/lang/Integer;"
					),
					remap = false
	)
	// Does not work
	private Integer setTextColorAlpha(String result) {
		int alpha = (int) (this.alpha * 255);
		return (alpha >> 24) + (34 << 16) + (165 << 8) + 240;
	}

}
