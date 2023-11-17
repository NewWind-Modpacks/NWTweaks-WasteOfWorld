package com.newwind.nwtweaks.capability;

import com.newwind.nwtweaks.NWConfig;
import net.minecraft.nbt.CompoundTag;

public class IsUnderground {

	private final String NBT_NAME = "isUnderground";

	private boolean isUnderground;
	private int nextCheck;


	public boolean isUnderground() {
		return isUnderground;
	}

	public void setUnderground(boolean underground) {
		isUnderground = underground;
	}

	public int getNextCheck() {
		return nextCheck;
	}

	public void countDown() {
		nextCheck--;
	}

	public void resetCheck() {
		nextCheck = NWConfig.Common.UNDERGROUND_CHECK_INTERVAL.get();
	}

	public void serialize(CompoundTag nbt) {
		nbt.putBoolean(NBT_NAME, isUnderground);
	}

	public void deserialize(CompoundTag nbt) {
		isUnderground = nbt.getBoolean(NBT_NAME);
		nextCheck = 0;
	}

}
