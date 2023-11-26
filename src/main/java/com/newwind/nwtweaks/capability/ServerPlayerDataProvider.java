package com.newwind.nwtweaks.capability;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ServerPlayerDataProvider implements ICapabilityProvider, INBTSerializable<CompoundTag> {

	public static final Capability<ServerPlayerData> CAPABILITY = CapabilityManager.get(new CapabilityToken<ServerPlayerData>() {});

	private ServerPlayerData data = null;
	private final LazyOptional<ServerPlayerData> optional = LazyOptional.of(this::getData);

	private ServerPlayerData getData() {
		if (data == null)
			data = new ServerPlayerData();

		return data;
	}


	@Override
	public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
		if (cap == CAPABILITY)
			return optional.cast();

		return LazyOptional.empty();
	}

	@Override
	public CompoundTag serializeNBT() {
		CompoundTag nbt = new CompoundTag();
		getData().serialize(nbt);
		return nbt;
	}

	@Override
	public void deserializeNBT(CompoundTag nbt) {
		getData().deserialize(nbt);
	}

}
