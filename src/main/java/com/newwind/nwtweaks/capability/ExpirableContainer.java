package com.newwind.nwtweaks.capability;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;

public class ExpirableContainer {

	private final String NBT_TIME = "lone_time_start";
	private final String NBT_TOUCHED = "touched";
	private long loneStart = -1;
	private boolean touched = false;


	public boolean setAlone(Level level) {
		if (loneStart < 0) {
			loneStart = level.getGameTime();
			return true;
		}
		return false;
	}

	public boolean setInCompany() {
		if (loneStart != -1) {
			loneStart = -1;
			return true;
		}
		return false;
	}

	public long getLoneStart() {
		return this.loneStart;
	}

	public void touch() {
		this.touched = true;
	}

	public boolean isTouched() {
		return touched;
	}

	public void serialize(CompoundTag nbt) {
		nbt.putLong(NBT_TIME, loneStart);
		nbt.putBoolean(NBT_TOUCHED, touched);
	}

	public void deserialize(CompoundTag nbt) {
		loneStart = nbt.getInt(NBT_TIME);
		touched = nbt.getBoolean(NBT_TOUCHED);
	}

}
