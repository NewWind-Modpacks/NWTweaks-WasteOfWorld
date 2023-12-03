package com.newwind.nwtweaks.mixin.client;

import com.newwind.nwtweaks.util.CommonUtils;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import vazkii.neat.HealthBarRenderer;

@Mixin(HealthBarRenderer.class)
public class NeatHealthBarRenderer {

	@Inject(
					method = "shouldShowPlate",
					at = @At("HEAD"),
					cancellable = true,
					remap = false
	)
	private static void disableUnderground(Entity entity, Entity cameraEntity, CallbackInfoReturnable<Boolean> cir) {
		if (entity.equals(cameraEntity) || CommonUtils.isUnderground(cameraEntity))
			cir.setReturnValue(false);
	}

}
