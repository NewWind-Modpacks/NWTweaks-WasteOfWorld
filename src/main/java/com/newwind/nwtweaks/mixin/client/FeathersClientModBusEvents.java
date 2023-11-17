package com.newwind.nwtweaks.mixin.client;

import com.elenai.feathers.client.gui.FeathersHudOverlay;
import com.elenai.feathers.compat.ClassicBarsCompat;
import com.elenai.feathers.event.ClientEvents;
import net.minecraftforge.client.event.RegisterGuiOverlaysEvent;
import net.minecraftforge.client.gui.overlay.VanillaGuiOverlay;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(ClientEvents.ClientModBusEvents.class)
public abstract class FeathersClientModBusEvents {

	/**
	 * @author Kevadroz
	 * @reason Fix render order
	 */
	@SubscribeEvent
	@Overwrite(remap = false)
	public static void registerGuiOverlays(RegisterGuiOverlaysEvent event) {
		if (!ModList.get().isLoaded("classicbar")) {
			event.registerBelow(VanillaGuiOverlay.AIR_LEVEL.id(), "feathers", FeathersHudOverlay.FEATHERS);
		} else {
			ClassicBarsCompat.registerClassicBarOverlay();
		}
	}

}
