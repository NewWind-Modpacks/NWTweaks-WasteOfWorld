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

public class BladderAirProvider implements ICapabilityProvider, INBTSerializable<CompoundTag> {

	public static final Capability<BladderAir> BLADDER_AIR = CapabilityManager.get(new CapabilityToken<BladderAir>() {});

	private BladderAir bladderAir = null;
	private final LazyOptional<BladderAir> optional = LazyOptional.of(this::getBladderAir);

	private BladderAir getBladderAir() {
		if (bladderAir == null)
			bladderAir = new BladderAir();

		return bladderAir;
	}


	@Override
	public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
		if (cap == BLADDER_AIR)
			return optional.cast();

		return LazyOptional.empty();
	}

	@Override
	public CompoundTag serializeNBT() {
		CompoundTag nbt = new CompoundTag();
		getBladderAir().serialize(nbt);
		return nbt;
	}

	@Override
	public void deserializeNBT(CompoundTag nbt) {
		getBladderAir().deserialize(nbt);
	}

}
