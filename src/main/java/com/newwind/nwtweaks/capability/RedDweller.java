package com.newwind.nwtweaks.capability;

import com.newwind.nwtweaks.NWConfig;
import net.minecraft.nbt.CompoundTag;

public class RedDweller {

	private final String NBT_NAME = "isRedDweller";

	private boolean isRedDweller;


	public boolean isRedDweller() {
		return isRedDweller;
	}

	public void setRedDweller(boolean redDweller) {
		isRedDweller = redDweller;
	}

	public void serialize(CompoundTag nbt) {
		nbt.putBoolean(NBT_NAME, isRedDweller);
	}

	public void deserialize(CompoundTag nbt) {
		isRedDweller = nbt.getBoolean(NBT_NAME);
	}

}
