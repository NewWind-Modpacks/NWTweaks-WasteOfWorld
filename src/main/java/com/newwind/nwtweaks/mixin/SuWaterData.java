package com.newwind.nwtweaks.mixin;

import com.newwind.nwtweaks.access.IWaterData;
import com.stereowalker.survive.needs.WaterData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

@Mixin(WaterData.class)
public class SuWaterData implements IWaterData {

	@Shadow(
					remap = false
	)
	private float waterExhaustionLevel;

	@Override
	@Unique
	public float nwTweaks$getExhaustion() {
		return this.waterExhaustionLevel;
	}

}
