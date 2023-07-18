package com.newwind.nwtweaks.mixin.client;

import com.ccr4ft3r.actionsofstamina.config.AoSAction;
import com.ccr4ft3r.actionsofstamina.config.ProfileConfig;
import net.bettercombat.api.WeaponAttributes;
import net.bettercombat.logic.WeaponRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.world.item.TieredItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Objects;

@Mixin(Minecraft.class)
public class MinecraftClient {

	@Inject(
					method = "startAttack",
					at = @At("HEAD"),
					cancellable = true
	)
	private void attackFeathers(CallbackInfoReturnable<Boolean> cir) {
		if (ProfileConfig.shouldStop(AoSAction.ATTACKING)
		&& Objects.requireNonNull(((Minecraft) (Object) this).player).getMainHandItem().getItem() instanceof TieredItem)
			cir.cancel();
	}
}
