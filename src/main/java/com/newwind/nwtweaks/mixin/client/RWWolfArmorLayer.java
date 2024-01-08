package com.newwind.nwtweaks.mixin.client;

import baguchan.revampedwolf.client.ModModelLayers;
import baguchan.revampedwolf.client.render.layer.WolfArmorLayer;
import fuzs.betteranimationscollection.client.model.PlayfulDoggyModel;
import net.minecraft.client.model.WolfModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.world.entity.animal.Wolf;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(WolfArmorLayer.class)
public class RWWolfArmorLayer {

	@Mutable
	@Final
	@Shadow private WolfModel<Wolf> model;

	@Inject(
					method = "<init>",
					at = @At("TAIL")
	)
	private void replaceModel(RenderLayerParent<Wolf, WolfModel<Wolf>> p_174496_, EntityModelSet p_174497_, CallbackInfo ci) {
		ModelPart bakedLayer = p_174497_.bakeLayer(ModModelLayers.WOLF_ARMOR);
		this.model = new PlayfulDoggyModel<>(bakedLayer);
	}

}
