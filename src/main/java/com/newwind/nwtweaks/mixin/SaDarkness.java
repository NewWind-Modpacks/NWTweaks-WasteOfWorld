package com.newwind.nwtweaks.mixin;

import com.newwind.nwtweaks.NWConfig;
import com.newwind.nwtweaks.util.MixinExternalFunctions;
import croissantnova.sanitydim.capability.ISanity;
import croissantnova.sanitydim.config.ConfigProxy;
import croissantnova.sanitydim.passive.Darkness;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

import javax.annotation.Nonnull;

@Mixin(Darkness.class)
public class SaDarkness {

	/**
	 * @author Kevadroz
	 * @reason Implement held light multiplier
	 */
	@Overwrite(
					remap = false
	)
	public float get(@Nonnull ServerPlayer player, @Nonnull ISanity cap, @Nonnull ResourceLocation dim) {
		float passiveIncrease = player.level.getMaxLocalRawBrightness(player.blockPosition()) <= ConfigProxy.getDarknessThreshold(dim) ? ConfigProxy.getDarkness(dim) : 0.0F;
		if (MixinExternalFunctions.CaveLantern.hasValidLightSource(player))
			passiveIncrease *= NWConfig.Common.HELD_LIGHT_SANITY_MULTIPLIER.get();
		return passiveIncrease;
	}

}
