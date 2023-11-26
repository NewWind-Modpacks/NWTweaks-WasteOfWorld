package com.newwind.nwtweaks.capability;

import com.newwind.nwtweaks.NWConfig;
import net.minecraft.nbt.CompoundTag;

public class IsUnderground {

	private final String NBT_NAME = "isUnderground";

	private int undergroundTime;
	private int nextCheck;


	public boolean isUnderground() {
		return undergroundTime > 0;
	}

	public int getTime() {
		return this.undergroundTime;
	}

	public void setUnderground(int underground) {
		undergroundTime = underground;
	}

	public void addTime(int amount) {
		undergroundTime += amount;
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
		nbt.putInt(NBT_NAME, undergroundTime);
	}

	public void deserialize(CompoundTag nbt) {
		undergroundTime = nbt.getInt(NBT_NAME);
		nextCheck = 0;
	}

}
