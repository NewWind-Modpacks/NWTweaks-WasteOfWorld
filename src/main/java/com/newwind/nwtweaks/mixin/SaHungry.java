package com.newwind.nwtweaks.mixin;

import com.newwind.nwtweaks.NWConfig;
import com.stereowalker.survive.needs.IRealisticEntity;
import croissantnova.sanitydim.capability.ISanity;
import croissantnova.sanitydim.config.ConfigProxy;
import croissantnova.sanitydim.passive.Hungry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

import javax.annotation.Nonnull;

@Mixin(Hungry.class)
public class SaHungry {

	/**
	 * @author Kevadroz
	 * @reason Implement Thirst
	 */
	@Overwrite(
					remap = false
	)
	public float get(@Nonnull ServerPlayer player, @Nonnull ISanity cap, @Nonnull ResourceLocation dim) {
		return player.getFoodData().getFoodLevel() <= ConfigProxy.getHungerThreshold(dim) ||
						((IRealisticEntity) player).getWaterData().getWaterLevel() <= NWConfig.Common.SANITY_THIRST_THRESHOLD.get() ? ConfigProxy.getHungry(dim) : 0.0F;
	}

}
