package com.newwind.nwtweaks.mixin;

import com.newwind.nwtweaks.util.MixinExternalFunctions;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodData;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(FoodData.class)
public class MinecraftFoodData {

	@Unique
	private static Float nWTweaks$multiplier = 1f;

	@Inject(
					method = "eat(Lnet/minecraft/world/item/Item;Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/entity/LivingEntity;)V",
					at = @At(
									value = "INVOKE",
									target = "Lnet/minecraft/world/food/FoodProperties;getNutrition()I"
					),
					locals = LocalCapture.CAPTURE_FAILHARD
	)
	private void preGetNutrition(Item item, ItemStack stack, LivingEntity entity, CallbackInfo ci, FoodProperties foodproperties) {
		if (entity instanceof Player player) {
			nWTweaks$multiplier = MixinExternalFunctions.getFoodModifier(player, stack);
		}
	}

	@Redirect(
					method = "eat(Lnet/minecraft/world/item/Item;Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/entity/LivingEntity;)V",
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
					method = "eat(Lnet/minecraft/world/item/Item;Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/entity/LivingEntity;)V",
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
