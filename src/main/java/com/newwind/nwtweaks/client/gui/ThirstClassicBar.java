package com.newwind.nwtweaks.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.newwind.nwtweaks.NWConfig;
import com.newwind.nwtweaks.access.IWaterData;
import com.stereowalker.survive.needs.IRealisticEntity;
import com.stereowalker.survive.world.effect.SMobEffects;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.client.gui.overlay.ForgeGui;
import tfar.classicbar.impl.BarOverlayImpl;
import tfar.classicbar.util.Color;
import tfar.classicbar.util.ModUtils;

public class ThirstClassicBar extends BarOverlayImpl {
	private static final ResourceLocation ICON_RL = new ResourceLocation("survive", "textures/gui/icons.png");
	private static final int MAX_WATER = 20;
	private static final Color WATER_COLOR = Color.from(0, 118, 244);
	private static final Color THIRSTY_COLOR = Color.from(103, 234, 0);
	private static final Color HYDRO_COLOR = Color.from(97, 185, 255);
	private static final Color HYDRO_THIRSTY_COLOR = Color.from(194, 246, 153);
	public ThirstClassicBar() {
		super("thirst_level");
	}

	@Override
	public void renderBar(ForgeGui forgeGui, PoseStack poseStack, Player player, int screenWidth, int screenHeight, int vOffset) {
		IRealisticEntity realisticPlayer = (IRealisticEntity) player;
		int xStart = screenWidth / 2 + getHOffset();
		int yStart = screenHeight - vOffset;
		double barWidth = getBarWidth(player);
		Color.reset();
		renderFullBarBackground(poseStack, xStart, yStart);
		renderOverthirst(poseStack, player, xStart, yStart);

		double f = xStart + (rightHandSide() ? WIDTH - barWidth : 0);
		Color color = getPrimaryBarColor(0, player);
		color.color2Gl();
		renderPartialBar(poseStack, f + 2, yStart + 2, barWidth);


		if (NWConfig.Client.RENDER_THIRST_EXHAUSTION.get()) {
			float wExhaustion = ((IWaterData) realisticPlayer.getWaterData()).nwTweaks$getExhaustion();
			wExhaustion = Math.min(wExhaustion, 4F);
			f = xStart + (rightHandSide() ? BarOverlayImpl.WIDTH - ModUtils.getWidth(wExhaustion, 4) : 0);
			//draw exhaustion
			RenderSystem.setShaderColor(1, 1, 1, .25f);
			ModUtils.drawTexturedModalRect(poseStack, f + 2, yStart + 1, 1, 28, ModUtils.getWidth(wExhaustion, 4f), 9);
		}
	}

	private void renderOverthirst(PoseStack matrices, Player player, int xStart, int yStart) {
		IRealisticEntity realisticPlayer = (IRealisticEntity) player;
		double waterL = realisticPlayer.getWaterData().getWaterLevel();

		double overflow = Math.max(waterL - MAX_WATER, 0D);
		double width = Math.min(Math.ceil(81D * overflow / 20D), 81D);

		Color.RED.color2Gl();
		ModUtils.drawTexturedModalRect(matrices, xStart, yStart, 0, 0, width, 9);
		Color.reset();
	}

	@Override
	public void renderText(PoseStack poseStack, Player player, int width, int height, int vOffset) {
		int xStart = width / 2 + getIconOffset();
		int yStart = height - vOffset;
		double waterL = ((IRealisticEntity) player).getWaterData().getWaterLevel();
		int c = getPrimaryBarColor(0, player).colorToText();
		textHelper(poseStack, xStart, yStart, waterL, c);
	}

	@Override
	public void renderIcon(PoseStack poseStack, Player player, int width, int height, int vOffset) {
		boolean thirsty = player.hasEffect(SMobEffects.THIRST);
		int xStart = width / 2 + getIconOffset();
		int yStart = height - vOffset;
		tfar.classicbar.util.ModUtils.drawTexturedModalRect(poseStack, xStart, yStart, 16, 54, 9, 9);
		if (thirsty)
			tfar.classicbar.util.ModUtils.drawTexturedModalRect(poseStack, xStart, yStart, 88, 54, 9, 9);
		else
			tfar.classicbar.util.ModUtils.drawTexturedModalRect(poseStack, xStart, yStart, 52, 54, 9, 9);
	}

	@Override
	public double getBarWidth(Player player) {
		IRealisticEntity realisticPlayer = (IRealisticEntity) player;
		double waterL = Math.min(realisticPlayer.getWaterData().getWaterLevel(), MAX_WATER);
		return Math.ceil(WIDTH * waterL / MAX_WATER);
	}

//	public double getHydroWidth(Player player) {
//		IRealisticEntity realisticPlayer = (IRealisticEntity) player;
//		double hydroL = Math.min(realisticPlayer.getWaterData().getHydrationLevel(), 4F);
//		return Math.ceil(WIDTH * hydroL / 4F);
//	}

	@Override
	public Color getPrimaryBarColor(int index, Player player) {
		if (player.hasEffect(SMobEffects.THIRST))
			return THIRSTY_COLOR;
		else
			return WATER_COLOR;
	}

	@Override
	public Color getSecondaryBarColor(int index, Player player) {
		if (player.hasEffect(SMobEffects.THIRST))
			return HYDRO_THIRSTY_COLOR;
		else
			return HYDRO_COLOR;
	}

	@Override
	public ResourceLocation getIconRL() {
		return ICON_RL;
	}

	@Override
	public boolean shouldRenderText() {
		return NWConfig.Client.RENDER_THIRST_TEXT.get();
	}
}
