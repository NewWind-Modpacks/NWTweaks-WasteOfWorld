package com.newwind.nwtweaks.sanity;

import com.newwind.nwtweaks.NWConfig;
import com.newwind.nwtweaks.util.CommonUtils;
import croissantnova.sanitydim.capability.ISanity;
import croissantnova.sanitydim.passive.IPassiveSanitySource;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import org.jetbrains.annotations.NotNull;

public class ExtremeTemperature implements IPassiveSanitySource {
	@Override
	public float get(@NotNull ServerPlayer serverPlayer, @NotNull ISanity iSanity, @NotNull ResourceLocation resourceLocation) {
		double temp = CommonUtils.getEffectivePlayerTemperature(serverPlayer);
		double threshold = NWConfig.Common.TEMPERATURE_SANITY_THRESHOLD.get();
		double modifier = -NWConfig.Common.EXTREME_TEMPERATURE_SANITY_MOD.get();
		if (temp >= threshold)
			return (float) (((temp - threshold) / (1D - threshold)) * modifier);
		else if (temp <= -threshold)
			return (float) (((temp + threshold) / (-1D + threshold)) * modifier);
		else return 0F;
	}
}
