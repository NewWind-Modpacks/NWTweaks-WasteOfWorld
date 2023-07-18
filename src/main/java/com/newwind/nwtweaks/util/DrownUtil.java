package com.newwind.nwtweaks.util;

import com.newwind.nwtweaks.NWConfig;
import com.newwind.nwtweaks.world.items.ProtectiveMask;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public class DrownUtil {

	public static final DamageSource TOXIC_GAS_DAMAGE = new DamageSource("toxic_gas").bypassArmor().bypassEnchantments().bypassMagic();

	public static void handlePlayerDrown(Player player) {
		if (!player.getAbilities().invulnerable) {
			ItemStack mask = ProtectiveMask.getMaskStack(player);
			if (mask != null) {
				if (ProtectiveMask.getAir(mask) <= -20) {
//					player.setAirSupply(0);
					ProtectiveMask.setAir(mask, NWConfig.Common.CAVE_DAMAGE_TIME.get() - 20);
					player.hurt(TOXIC_GAS_DAMAGE, NWConfig.Common.CAVE_DAMAGE_AMOUNT.get().floatValue());
				}
			} else {
				player.setAirSupply(player.getAirSupply() - 1);
				if (player.getAirSupply() <= -20) {
					player.setAirSupply(NWConfig.Common.CAVE_DAMAGE_TIME.get() - 20);
					player.hurt(TOXIC_GAS_DAMAGE, NWConfig.Common.CAVE_DAMAGE_AMOUNT.get().floatValue());
				}
			}
		}
	}
}
