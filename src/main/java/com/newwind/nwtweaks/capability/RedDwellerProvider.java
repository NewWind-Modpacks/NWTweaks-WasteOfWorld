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

public class RedDwellerProvider implements ICapabilityProvider, INBTSerializable<CompoundTag> {

	public static final Capability<RedDweller> CAPABILITY = CapabilityManager.get(new CapabilityToken<RedDweller>() {});

	private RedDweller redDwellerObject = null;
	private final LazyOptional<RedDweller> optional = LazyOptional.of(this::getRedDwellerObject);

	private RedDweller getRedDwellerObject() {
		if (redDwellerObject == null)
			redDwellerObject = new RedDweller();

		return redDwellerObject;
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
		getRedDwellerObject().serialize(nbt);
		return nbt;
	}

	@Override
	public void deserializeNBT(CompoundTag nbt) {
	getRedDwellerObject().deserialize(nbt);
	}
}
