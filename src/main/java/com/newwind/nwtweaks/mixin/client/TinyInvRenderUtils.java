package com.newwind.nwtweaks.mixin.client;

import com.mojang.blaze3d.systems.RenderSystem;
import com.stereowalker.survive.Survive;
import com.stereowalker.survive.core.SurviveEntityStats;
import com.stereowalker.survive.core.TempDisplayMode;
import com.stereowalker.survive.world.entity.ai.attributes.SAttributes;
import net.minecraft.client.Minecraft;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import nuparu.tinyinv.client.RenderUtils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(RenderUtils.class)
public abstract class TinyInvRenderUtils {

	/**
	 * @Author Kevadroz (code shamelessly copied from TinyInv and Survive)
	 */
	@Redirect(
					method = "renderHotbar",
					at = @At(
									value = "INVOKE",
									target = "Lcom/mojang/blaze3d/systems/RenderSystem;setShaderColor(FFFF)V"
					)
	)
	private static void setTemperatureColor(float x, float y, float z, float a) {
		Player playerentity = !(Minecraft.getInstance().getCameraEntity() instanceof Player) ? null : (Player)Minecraft.getInstance().getCameraEntity();
		if (!playerentity.isCreative() && Survive.TEMPERATURE_CONFIG.enabled && Survive.TEMPERATURE_CONFIG.tempDisplayMode.equals(TempDisplayMode.HOTBAR)) {
			double rawTemperature = SurviveEntityStats.getTemperatureStats(playerentity).getTemperatureLevel();
			double tempLocation = rawTemperature - Survive.DEFAULT_TEMP;
			double displayTemp = 0;
			if (tempLocation > 0) {
				double maxTemp = 0.0D;
				if (playerentity.getAttribute(SAttributes.HEAT_RESISTANCE) != null) {
					maxTemp = playerentity.getAttributeValue(SAttributes.HEAT_RESISTANCE);
				} else {
					maxTemp = SAttributes.HEAT_RESISTANCE.getDefaultValue();
				}
				double div = tempLocation / maxTemp;
				displayTemp = Mth.clamp(div, 0, 1.0D+(28.0D/63.0D));
			}
			if (tempLocation < 0) {
				double maxTemp = 0.0D;
				if (playerentity.getAttribute(SAttributes.COLD_RESISTANCE) != null) {
					maxTemp = playerentity.getAttributeValue(SAttributes.COLD_RESISTANCE);
				} else {
					maxTemp = SAttributes.COLD_RESISTANCE.getDefaultValue();
				}
				double div = tempLocation / maxTemp;
				displayTemp = Mth.clamp(div, -1.0D-(28.0D/63.0D), 0);
			}

			float heatTemp = (float) (1.0F - displayTemp);
			float coldTemp = (float) (1.0F + displayTemp);
			float whiteTemp = (float) ((1.0F - Math.abs(displayTemp))/2 + 0.5F);
			RenderSystem.setShaderColor(coldTemp, whiteTemp, heatTemp, 1.0F);
		} else {
			RenderSystem.setShaderColor(x, y, z, a);
		}
	}

}
