package com.newwind.nwtweaks.mixin;

import croissantnova.sanitydim.capability.ISanity;
import croissantnova.sanitydim.config.ConfigProxy;
import croissantnova.sanitydim.passive.InWaterOrRain;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

import javax.annotation.Nonnull;

@Mixin(InWaterOrRain.class)
public class SaInWaterOrRain {

	/**
	 * @author Kevadroz
	 * @reason Compatibility with Forgery
	 */
	@Overwrite(
					remap = false
	)
	public float get(@Nonnull ServerPlayer player, @Nonnull ISanity cap, @Nonnull ResourceLocation dim) {
		return player.isInWaterRainOrBubble() ? ConfigProxy.getRaining(dim) : 0.0F;
	}

}
