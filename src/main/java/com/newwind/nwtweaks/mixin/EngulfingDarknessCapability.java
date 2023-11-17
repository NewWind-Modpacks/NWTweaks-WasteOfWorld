package com.newwind.nwtweaks.mixin;

import com.newwind.nwtweaks.util.CommonUtils;
import com.newwind.nwtweaks.util.MixinExternalFunctions;
import dev.quarris.engulfingdarkness.capability.Darkness;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.LightLayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(Darkness.class)
public abstract class EngulfingDarknessCapability {

	@Redirect(
					method = "tick",
					at = @At(
									value = "INVOKE",
									target = "Ldev/quarris/engulfingdarkness/capability/Darkness;isResistant(Lnet/minecraft/world/entity/player/Player;)Z"
					),
					remap = false
	)
	private boolean onIsResistant(Darkness instance, Player player) {
		return instance.isResistant(player)
						|| MixinExternalFunctions.CaveLantern.hasValidLightSource(player)
						|| player.getLevel().getBrightness(LightLayer.SKY, new BlockPos(player.getEyePosition())) > 0
						|| !CommonUtils.isUnderground(player);
	}

}
