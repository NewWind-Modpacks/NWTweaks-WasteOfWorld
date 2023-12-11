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

public class ExpirableContainerProvider implements ICapabilityProvider, INBTSerializable<CompoundTag> {

	public static final Capability<ExpirableContainer> CAPABILITY = CapabilityManager.get(new CapabilityToken<>() {
	});

	private ExpirableContainer expirableContainer = null;
	private final LazyOptional<ExpirableContainer> optional = LazyOptional.of(this::getExpirableContainer);

	private ExpirableContainer getExpirableContainer() {
		if (expirableContainer == null)
			expirableContainer = new ExpirableContainer();

		return expirableContainer;
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
		getExpirableContainer().serialize(nbt);
		return nbt;
	}

	@Override
	public void deserializeNBT(CompoundTag nbt) {
		getExpirableContainer().deserialize(nbt);
	}

}
