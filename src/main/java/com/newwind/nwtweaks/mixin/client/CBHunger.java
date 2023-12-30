package com.newwind.nwtweaks.mixin.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.newwind.nwtweaks.util.MixinExternalFunctions;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.client.gui.overlay.ForgeGui;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;
import tfar.classicbar.impl.overlays.vanilla.Hunger;
import tfar.classicbar.util.Color;
import tfar.classicbar.util.ModUtils;

@Mixin(Hunger.class)
public class CBHunger {

	/**
	 * @author Kevadroz
	 * @reason Fix overflow caused by survive
	 */
	@Overwrite(remap = false)
	public double getBarWidth(Player player) {
		double hunger = player.getFoodData().getFoodLevel();
		double maxHunger = 20.0;
		return Math.ceil(77.0 * Math.min(hunger, maxHunger) / maxHunger);
	}

	@Inject(
					method = "renderBar",
					at = @At(
									value = "INVOKE",
									target = "Ltfar/classicbar/impl/overlays/vanilla/Hunger;renderFullBarBackground(Lcom/mojang/blaze3d/vertex/PoseStack;II)V",
									shift = At.Shift.AFTER
					),
					locals = LocalCapture.CAPTURE_FAILHARD,
					remap = false
	)
	private void renderOvereat(ForgeGui gui, PoseStack matrices, Player player, int screenWidth, int screenHeight, int vOffset, CallbackInfo ci, double hunger, double maxHunger, double barWidthH, double currentSat, double maxSat, double barWidthS, float exhaustion, int xStart, int yStart) {
		double overflow = Math.max(player.getFoodData().getFoodLevel() - 20D, 0D);
		double width = Math.min(Math.ceil(81D * overflow / 20D), 81D);

		Color.RED.color2Gl();
		ModUtils.drawTexturedModalRect(matrices, xStart, yStart, 0, 0, width, 9);
		Color.reset();
	}


	@Unique
	private static Float nWTweaks$multiplier = 1f;

	@Inject(
					method = "renderBar",
					at = @At(
									value = "INVOKE",
									target = "Lnet/minecraft/world/food/FoodProperties;getNutrition()I"
					),
					locals = LocalCapture.CAPTURE_FAILHARD
	)
	private void preGetNutrition(ForgeGui gui, PoseStack matrices, Player player, int screenWidth, int screenHeight, int vOffset, CallbackInfo ci, double hunger, double maxHunger, double barWidthH, double currentSat, double maxSat, double barWidthS, float exhaustion, int xStart, int yStart, double f, Color hungerColor, Color satColor, ItemStack stack, double time, double foodAlpha, FoodProperties food) {
		nWTweaks$multiplier = MixinExternalFunctions.getFoodModifier(player, stack);
	}

	@Redirect(
					method = "renderBar",
					at = @At(
									value = "INVOKE",
									target = "Lnet/minecraft/world/food/FoodProperties;getNutrition()I"
					)
	)
	private int overrideNutrition(FoodProperties instance) {
		int returnValue = instance.getNutrition();
		if (nWTweaks$multiplier < 1f) {
			returnValue = (int) (((float) returnValue) * nWTweaks$multiplier);
		}
		return returnValue;
	}
	@Redirect(
					method = "renderBar",
					at = @At(
									value = "INVOKE",
									target = "Lnet/minecraft/world/food/FoodProperties;getSaturationModifier()F"
					)
	)
	private float overrideSaturation(FoodProperties instance) {
		float returnValue = instance.getSaturationModifier();
		if (nWTweaks$multiplier < 1f) {
			returnValue *= nWTweaks$multiplier;
			nWTweaks$multiplier = 1f;
		}
		return returnValue;
	}

}
