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

public class IsUndergroundProvider implements ICapabilityProvider, INBTSerializable<CompoundTag> {

	public static final Capability<IsUnderground> IS_UNDERGROUND = CapabilityManager.get(new CapabilityToken<IsUnderground>() {});

	private IsUnderground undergroundObject = null;
	private final LazyOptional<IsUnderground> optional = LazyOptional.of(this::getUndergroundObject);

	private IsUnderground getUndergroundObject() {
		if (undergroundObject == null)
			undergroundObject = new IsUnderground();

		return undergroundObject;
	}


	@Override
	public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
		if (cap == IS_UNDERGROUND)
			return optional.cast();

		return LazyOptional.empty();
	}

	@Override
	public CompoundTag serializeNBT() {
		CompoundTag nbt = new CompoundTag();
		getUndergroundObject().serialize(nbt);
		return nbt;
	}

	@Override
	public void deserializeNBT(CompoundTag nbt) {
	getUndergroundObject().deserialize(nbt);
	}
}
