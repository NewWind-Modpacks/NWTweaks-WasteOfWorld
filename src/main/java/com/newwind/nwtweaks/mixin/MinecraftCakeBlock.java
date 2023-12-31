package com.newwind.nwtweaks.mixin;

import com.newwind.nwtweaks.util.MixinExternalFunctions;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodData;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.CakeBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(CakeBlock.class)
public class MinecraftCakeBlock {

	@Unique
	private static float nWTweaks$multiplier = 1f;

	@Inject(
					method = "eat",
					at = @At(
									value = "INVOKE",
									target = "Lnet/minecraft/world/food/FoodData;eat(IF)V"
					)
	)
	private static void onEat(LevelAccessor level, BlockPos pos, BlockState blockState, Player player, CallbackInfoReturnable<InteractionResult> cir) {
		nWTweaks$multiplier = MixinExternalFunctions.getFoodModifier(player, blockState.getCloneItemStack(null, level, pos, player));
	}

	@Redirect(
					method = "eat",
					at = @At(
									value = "INVOKE",
									target = "Lnet/minecraft/world/food/FoodData;eat(IF)V"
					)
	)
	private static void overrideFoodValue(FoodData instance, int nutrition, float saturation) {
		instance.eat((int) (nutrition * nWTweaks$multiplier), saturation * nWTweaks$multiplier);
	}

}
