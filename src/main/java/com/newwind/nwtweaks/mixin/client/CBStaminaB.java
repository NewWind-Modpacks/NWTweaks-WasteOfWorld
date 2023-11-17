package com.newwind.nwtweaks.mixin.client;

import com.alrex.parcool.client.hud.impl.HUDType;
import com.alrex.parcool.config.ParCoolConfig;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import tfar.classicbar.impl.overlays.mod.StaminaB;

@Mixin(StaminaB.class)
public class CBStaminaB {

	/**
	 * @author Kevadroz
	 * @reason Updated ParCool implementation so it doesn't crash
	 */
	@Overwrite(
					remap = false
	)
	public static boolean checkConfigs() {
		return false;/*!ParCoolConfig.Client.StaminaHUDType.get().equals(HUDType.Normal)
						&& !(Boolean) ParCoolConfig.Client.Booleans.InfiniteStamina.get()
						&& !(Boolean) ParCoolConfig.Client.Booleans.UseHungerBarInstead.get();*/
	}

}
