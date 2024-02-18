package com.newwind.nwtweaks.capability;

import net.minecraft.nbt.CompoundTag;

public class ServerPlayerData {

	private final static String PILLS_TAG = "discovered_pills";
	private boolean[] discoveredPills = new boolean[1];


	public void initialize(int size) {
		if (notInitialized())
			this.discoveredPills = new boolean[size];
	}

	public boolean notInitialized() {
		return this.discoveredPills.length == 1;
	}

	public boolean getDiscoveredPill(int slot) {
		return discoveredPills[Math.min(slot, discoveredPills.length - 1)];
	}

	public void setDiscoveredPill(int slot, boolean discovered) {
		this.discoveredPills[slot] = discovered;
	}

	public void setDiscoveredPills(boolean[] discoveredPills) {
		this.discoveredPills = discoveredPills;
	}

	public boolean[] getDiscoveredPills() {
		return discoveredPills;
	}

	public void serialize(CompoundTag nbt) {
		byte[] byteArray = new byte[discoveredPills.length];
		for (int i = 0; i < discoveredPills.length; i++)
			if (discoveredPills[i])
				byteArray[i] = 1;
			else
				byteArray[i] = 0;
		nbt.putByteArray(PILLS_TAG, byteArray);
	}

	public void deserialize(CompoundTag nbt) {
		byte[] byteArray = nbt.getByteArray(PILLS_TAG);
		this.discoveredPills = new boolean[byteArray.length];
		for (int i = 0; i < byteArray.length; i++)
			this.discoveredPills[i] = (byteArray[i] != 0);
	}

}
