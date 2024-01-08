package com.newwind.nwtweaks.mixin.client;

import baguchan.revampedwolf.client.render.WolfArmorModel;
import fuzs.betteranimationscollection.client.model.PlayfulDoggyModel;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(WolfArmorModel.class)
public class RWWolfArmorModel {

	@Inject(
					method = "createBodyLayer",
					at = @At("HEAD"),
					cancellable = true,
					remap = false
	)
	private static void replaceModel(CallbackInfoReturnable<LayerDefinition> cir) {
		cir.setReturnValue(PlayfulDoggyModel.createAnimatedBodyLayer());
	}

}
