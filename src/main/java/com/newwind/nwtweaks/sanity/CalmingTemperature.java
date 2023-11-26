package com.newwind.nwtweaks.sanity;

import com.newwind.nwtweaks.NWConfig;
import com.newwind.nwtweaks.util.CommonUtils;
import com.stereowalker.survive.core.SurviveEntityStats;
import croissantnova.sanitydim.capability.ISanity;
import croissantnova.sanitydim.passive.IPassiveSanitySource;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import org.jetbrains.annotations.NotNull;

public class CalmingTemperature implements IPassiveSanitySource {
	@Override
	public float get(@NotNull ServerPlayer serverPlayer, @NotNull ISanity iSanity, @NotNull ResourceLocation resourceLocation) {
		double temp = CommonUtils.getEffectivePlayerTemperature(serverPlayer);
		double target = SurviveEntityStats.getTemperatureStats(serverPlayer).getTargetTemperature();
		double modifier = -NWConfig.Common.CALMING_TEMPERATURE_SANITY_MOD.get();
		if ((temp > 0 && temp > target) || (temp < 0 && temp < target))
			return (float) modifier;
		else return 0F;
	}
}
