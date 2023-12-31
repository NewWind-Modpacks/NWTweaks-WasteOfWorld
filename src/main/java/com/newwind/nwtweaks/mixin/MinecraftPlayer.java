package com.newwind.nwtweaks.mixin;

import com.illusivesoulworks.diet.common.util.PlayerSensitive;
import com.mojang.authlib.GameProfile;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.ProfilePublicKey;
import net.minecraft.world.food.FoodData;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = Player.class, priority = 2000)
public class MinecraftPlayer {

	@Shadow protected FoodData foodData;

	@Inject(
					method = "<init>",
					at = @At("TAIL")
	)
	private void afterInit(Level p_219727_, BlockPos p_219728_, float p_219729_, GameProfile p_219730_, ProfilePublicKey p_219731_, CallbackInfo ci) {
		((PlayerSensitive) this.foodData).setPlayer(((Player) ((Object) this)));
	}

}
