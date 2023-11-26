package com.newwind.nwtweaks.client.gui;

import com.mojang.blaze3d.vertex.PoseStack;
import com.newwind.nwtweaks.NWConfig;
import com.newwind.nwtweaks.NWTweaks;
import croissantnova.sanitydim.capability.IPassiveSanity;
import croissantnova.sanitydim.capability.ISanity;
import croissantnova.sanitydim.capability.SanityProvider;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.client.gui.overlay.ForgeGui;
import tfar.classicbar.impl.BarOverlayImpl;
import tfar.classicbar.util.Color;
import tfar.classicbar.util.ModUtils;

public class SanityClassicBar extends BarOverlayImpl {
	private static final ResourceLocation ICON_RL = new ResourceLocation(NWTweaks.MODID, "textures/gui/sanity_bar.png");
	private static final float MAX_SANITY = 1F;
	private static final Color SANITY_COLOR = Color.from(213, 174, 158);


	private float lastSanity = MAX_SANITY;
	private float highlightSanity = 0F;
	private float flashTimer = 0F;
	private float arrowTimer = 0F;

	//	private static final Color THIRSTY_COLOR = Color.from(103, 234, 0);
//	private static final Color HYDRO_COLOR = Color.from(97, 185, 255);
//	private static final Color HYDRO_THIRSTY_COLOR = Color.from(194, 246, 153);
	public SanityClassicBar() {
		super("sanitydim");
	}

	private static float getSanity(Player player) {
		ISanity sanityCap = player.getCapability(SanityProvider.CAP).orElse(null);
		if (sanityCap != null)
			return MAX_SANITY - sanityCap.getSanity();
		else
			return 0F;
	}

	@Override
	public void renderBar(ForgeGui forgeGui, PoseStack poseStack, Player player, int screenWidth, int screenHeight, int vOffset) {
		tick(player);
		int xStart = screenWidth / 2 + getHOffset();
		int yStart = screenHeight - vOffset;
		int backgroundTextureY = 0;
		double barWidth = getBarWidth(player);
		double barXStart = xStart + (rightHandSide() ? WIDTH - barWidth : 0);
		Color barColor = getPrimaryBarColor(0, player);

		if (this.flashTimer > 0.0F && (int) this.flashTimer / 3 % 2 == 0)
			backgroundTextureY = 18;

		// Bar Background
		Color.reset();
		ModUtils.drawTexturedModalRect(poseStack, xStart, yStart, 0, backgroundTextureY, 81.0, 9);

		float highlightLength = highlightSanity * (flashTimer / 20F);
		int trueBarEnd = (int) (barWidth + highlightLength);
		// Lost Sanity Highlight
		if (highlightSanity > 0F) {
			Color.reset();
			if (rightHandSide())
				renderPartialBar(poseStack, barXStart + 2 - highlightLength, yStart + 2, trueBarEnd);
			else
				renderPartialBar(poseStack, barXStart + 2, yStart + 2, trueBarEnd);
		}

		// Bar
		barColor.color2Gl();
		renderPartialBar(poseStack, barXStart + 2, yStart + 2, Math.round(barWidth));


		// Render Arrows
		ISanity sanityCap = player.getCapability(SanityProvider.CAP).orElse(null);
		if (sanityCap != null && sanityCap instanceof IPassiveSanity passiveCap) {
			float passiveIncrease = passiveCap.getPassiveIncrease();
			float abPassiveIncrease = Math.abs(passiveIncrease);
			if (abPassiveIncrease > 0) {
				this.bindIconTexture();

				boolean useBigArrows = abPassiveIncrease >= 2.0E-4F * NWConfig.Client.ARROW_PASSIVE_SCALE.get();
				boolean isRightArrow = passiveIncrease < 0F;
				if (this.rightHandSide())
					isRightArrow = !isRightArrow;
				int yTexOffset = (isRightArrow ? 9 : 0) + (useBigArrows ? 18 : 0);
				int xOffset = getArrowOffset(abPassiveIncrease);
				int offsetBarEnd = trueBarEnd - xOffset;

				Color.reset();
				if (rightHandSide()) {
					int reversedBar = trueBarEnd + xOffset;
					ModUtils.drawTexturedModalRect(poseStack, xStart + xOffset, yStart, 0, 9 + yTexOffset, 81, 9);
					ModUtils.drawTexturedModalRect(poseStack, xStart + xOffset, yStart, 81, 9 + yTexOffset, 79.0 - reversedBar, 9);
				} else {
					ModUtils.drawTexturedModalRect(poseStack, xStart + xOffset, yStart, 81, 9 + yTexOffset, 81, 9);
					ModUtils.drawTexturedModalRect(poseStack, xStart + xOffset, yStart, 0, 9 + yTexOffset, offsetBarEnd + 2.0, 9);
				}

				forgeGui.setupOverlayRenderState(true, false, BarOverlayImpl.ICON_BAR);
			}
		}

		this.lastSanity = getSanity(player);
	}

	@Override
	public boolean shouldRenderText() {
		return NWConfig.Client.RENDER_SANITY_TEXT.get();
	}

	@Override
	public void renderText(PoseStack poseStack, Player player, int width, int height, int vOffset) {
		int xStart = width / 2 + getIconOffset();
		int yStart = height - vOffset;
		double sanity = getSanity(player) * 100F;
		int c = getPrimaryBarColor(0, player).colorToText();
		textHelper(poseStack, xStart, yStart, sanity, c);
	}

	@Override
	public void renderIcon(PoseStack poseStack, Player player, int width, int height, int vOffset) {
		int xStart = width / 2 + getIconOffset();
		int yStart = height - vOffset;
		ModUtils.drawTexturedModalRect(poseStack, xStart, yStart, 0, 0, 9, 9);
	}

	@Override
	public double getBarWidth(Player player) {
		double sanity = getSanity(player);
		return Math.ceil(WIDTH * sanity / MAX_SANITY);
	}

	@Override
	public Color getPrimaryBarColor(int index, Player player) {
		return SANITY_COLOR;
	}

	@Override
	public ResourceLocation getIconRL() {
		return ICON_RL;
	}

	private void tick(Player player) {
		float deltaTick = Minecraft.getInstance().getDeltaFrameTime();
		double sanityScale = NWConfig.Client.ARROW_PASSIVE_SCALE.get();
		ISanity sanityCap = player.getCapability(SanityProvider.CAP).orElse(null);
		if (sanityCap != null) {

			this.flashTimer = Mth.clamp(this.flashTimer - deltaTick, 0F, 20F);
			float sanityDifference = getSanity(player) - this.lastSanity;
			if (Math.abs(sanityDifference) > 0.01F * sanityScale) {
				this.highlightSanity = Math.max(this.highlightSanity * (this.flashTimer / 20F) + (-sanityDifference * WIDTH), 0F);
				this.flashTimer = 20F;
			}

			if (this.flashTimer <= 0F)
				this.highlightSanity = 0F;
		}

		if (sanityCap instanceof IPassiveSanity passiveCap) {
			if (passiveCap.getPassiveIncrease() != 0F)
				this.arrowTimer -= deltaTick;
			while (this.arrowTimer < 0F)
				this.arrowTimer += Math.abs(passiveCap.getPassiveIncrease()) >= 2.0E-4F * sanityScale ? 24F : 16F;
		}
	}

	// Code from SanityDIM's GuiHandler class
	private int getArrowOffset(float abPassiveIncrease) {
		double sanityScale = NWConfig.Client.ARROW_PASSIVE_SCALE.get();

		if (abPassiveIncrease < 0.000025F * sanityScale)
			return 0;

		int arrowOffset;
		if (abPassiveIncrease < 2.0E-4F * sanityScale) {
			arrowOffset = (int) this.arrowTimer / 4 % 2;
			arrowOffset *= this.arrowTimer > 8.0F ? 1 : -1;
		} else {
			arrowOffset = this.arrowTimer >= 12.0F && this.arrowTimer <= 15.0F || this.arrowTimer >= 0.0F && this.arrowTimer <= 3.0F ? 0 : ((int) this.arrowTimer / 3 % 2 == 0 ? 2 : 1);
			arrowOffset *= this.arrowTimer > 12.0F ? 1 : -1;
		}
		return arrowOffset;
	}

}
