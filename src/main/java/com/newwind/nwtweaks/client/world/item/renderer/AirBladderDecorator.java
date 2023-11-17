package com.newwind.nwtweaks.client.world.item.renderer;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import com.newwind.nwtweaks.NWConfig;
import com.newwind.nwtweaks.util.CommonUtils;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.client.IItemDecorator;

public class AirBladderDecorator implements IItemDecorator {


	// From vanilla
	private static void fillRect(BufferBuilder p_115153_, int x, int y, int width, int height, int red, int green, int blue, int alpha) {
		RenderSystem.setShader(GameRenderer::getPositionColorShader);
		p_115153_.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR);
		p_115153_.vertex((double) (x), (double) (y), 0.0D).color(red, green, blue, alpha).endVertex();
		p_115153_.vertex((double) (x), (double) (y + height), 0.0D).color(red, green, blue, alpha).endVertex();
		p_115153_.vertex((double) (x + width), (double) (y + height), 0.0D).color(red, green, blue, alpha).endVertex();
		p_115153_.vertex((double) (x + width), (double) (y), 0.0D).color(red, green, blue, alpha).endVertex();
		BufferUploader.drawWithShader(p_115153_.end());
	}

	private static int getBarWidth(float oxygen, float maxOxygen) {
		return (int) Math.ceil(13F * (oxygen / maxOxygen));
	}

	@Override
	public boolean render(Font font, ItemStack stack, int xOffset, int yOffset, float blitOffset) {
		if (stack.getOrCreateTag().contains(CommonUtils.AIR_BLADDER_TAG_OXYGEN_AMOUNT)) {
			int oxygen = stack.getOrCreateTag().getInt(CommonUtils.AIR_BLADDER_TAG_OXYGEN_AMOUNT);
			int maxOxygen = NWConfig.Common.BLADDER_CAPACITY.get();
			if (oxygen < maxOxygen) {
				RenderSystem.disableDepthTest();
				RenderSystem.disableTexture();
				RenderSystem.disableBlend();
				BufferBuilder builder = Tesselator.getInstance().getBuilder();
				int barWidth = getBarWidth(oxygen, maxOxygen);
				int backgroundHeight = 1;
				if (stack.getDamageValue() <= 0)
					backgroundHeight = 2;
				fillRect(builder, xOffset + 2, yOffset + 12, 13, backgroundHeight, 0, 0, 0, 255);
				fillRect(builder, xOffset + 2, yOffset + 12, barWidth, 1, 85, 255, 255, 255);
				RenderSystem.enableBlend();
				RenderSystem.enableTexture();
				RenderSystem.enableDepthTest();
			}
		}
		return true;
	}
}
