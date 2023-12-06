package com.newwind.nwtweaks.mixin.client;

import com.newwind.nwtweaks.NWConfig;
import me.jellysquid.mods.sodium.client.gui.options.Option;
import me.jellysquid.mods.sodium.client.gui.options.OptionImpl;
import me.jellysquid.mods.sodium.client.gui.options.control.Control;
import me.jellysquid.mods.sodium.client.gui.options.control.ControlValueFormatter;
import me.jellysquid.mods.sodium.client.gui.options.control.SliderControl;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.function.Function;

@Mixin(me.jellysquid.mods.sodium.client.gui.SodiumGameOptionPages.class)
public class SodiumGameOptionPages {

	@SuppressWarnings({"rawtypes", "unchecked"})
	@Redirect(
					method = "general",
					at = @At(
									value = "INVOKE",
									target = "Lme/jellysquid/mods/sodium/client/gui/options/OptionImpl$Builder;setControl(Ljava/util/function/Function;)Lme/jellysquid/mods/sodium/client/gui/options/OptionImpl$Builder;",
									ordinal = 0
					),
					remap = false
	)
	private static OptionImpl.Builder setMaxRenderDistance(OptionImpl.Builder instance, Function<OptionImpl, Control> control) {
		int maxRenderDistance = (int) Math.ceil(NWConfig.Client.MAX_FOG_END.get() / 16D);
		return instance.setControl((option) ->
						new SliderControl((Option<Integer>) option, 2, Math.max(maxRenderDistance, 3), 1, ControlValueFormatter.translateVariable("options.chunks")));
	}

}
