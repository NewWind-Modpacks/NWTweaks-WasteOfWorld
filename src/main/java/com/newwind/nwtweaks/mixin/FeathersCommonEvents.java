package com.newwind.nwtweaks.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

import com.elenai.feathers.event.CommonEvents;

import net.minecraftforge.event.TickEvent.PlayerTickEvent;

@Mixin(CommonEvents.class)
public abstract class FeathersCommonEvents {

	/**
	 * @author Kevadroz
	 * @reason Effect replaced by internal handling
	 */
	@Overwrite(remap = false)
	private static void handleFrostEffect(PlayerTickEvent event) {};

}
