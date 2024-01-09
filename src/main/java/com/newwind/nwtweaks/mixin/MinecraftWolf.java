package com.newwind.nwtweaks.mixin;

import baguchan.revampedwolf.api.WolfTypes;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.animal.Wolf;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;

import java.util.Objects;

@Mixin(value = Wolf.class, priority = 2000)
public abstract class MinecraftWolf extends TamableAnimal {

	@SuppressWarnings({"MissingUnique", "unused"})
	private static EntityDataAccessor<String> DATA_TYPE;

	protected MinecraftWolf(EntityType<? extends TamableAnimal> p_21803_, Level p_21804_) {
		super(p_21803_, p_21804_);
	}

	@SuppressWarnings({"MissingUnique", "AddedMixinMembersNamePattern", "unused"})
	public void setVariant(WolfTypes p_28929_) {
		this.entityData.set(DATA_TYPE, Objects.requireNonNullElse(p_28929_, WolfTypes.WHITE).type);
	}

}
