package com.newwind.nwtweaks.capability;

import com.newwind.nwtweaks.NWConfig;
import net.minecraft.nbt.CompoundTag;

public class BladderAir {

	private final String NBT_NAME = "air";
	private int airTicks = NWConfig.Common.BLADDER_CAPACITY.get();


	public int getAir() {
		return airTicks;
	}

	public void decrease(int amount) {
		airTicks = Math.max(0, airTicks - amount);
	}

	public void increase(int amount) {
		airTicks = Math.min(NWConfig.Common.BLADDER_CAPACITY.get(), airTicks + amount);
	}

	public void serialize(CompoundTag nbt) {
		nbt.putInt(NBT_NAME, airTicks);
	}

	public void deserialize(CompoundTag nbt) {
		airTicks = nbt.getInt(NBT_NAME);
	}

}
