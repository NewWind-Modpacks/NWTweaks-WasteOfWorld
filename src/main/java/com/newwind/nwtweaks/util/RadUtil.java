package com.newwind.nwtweaks.util;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;

import java.util.UUID;

public class RadUtil {

	private static final UUID attributeId = UUID.fromString("9bd88b43-5ca5-4528-bc98-1cfb9966e1bb");
	public static final DamageSource RADIATION_DAMAGE = new DamageSource("radiation").bypassArmor().bypassEnchantments().bypassMagic();

	public static void modifyRadiation(Player player, double rads) {
		if (!player.getAbilities().invulnerable && player.isAlive()) {
			AttributeInstance maxHealth = player.getAttribute(Attributes.MAX_HEALTH);
			assert maxHealth != null;
			AttributeModifier radAttribute = maxHealth.getModifier(attributeId);
			if (radAttribute == null)
				radAttribute = genAttribute(0.0);
			if (radAttribute.getAmount() - rads <= -1.0) {
				maxHealth.removeModifier(attributeId);
				maxHealth.addPermanentModifier(genAttribute(0.0));
				player.hurt(RADIATION_DAMAGE, Float.MAX_VALUE);
			}
			maxHealth.removeModifier(attributeId);
			maxHealth.addPermanentModifier(genAttribute(Math.min(radAttribute.getAmount() - rads, 0.0)));
//			if (rads > 0 && Math.ceil(player.getHealth()) > Math.ceil(maxHealth.getValue()))
//				player.hurt(RADIATION_DAMAGE, (float) Math.max(player.getHealth() - maxHealth.getValue(), 1.0));
//			AbstractPlayerDamageModel damageModel = ichttt.mods.firstaid.common.util.CommonUtils.getDamageModel(player);
//			damageModel.getCurrentMaxHealth()
		}
	}

	private static AttributeModifier genAttribute(double rads) {
		return new AttributeModifier(attributeId, "Radiation", rads, AttributeModifier.Operation.MULTIPLY_TOTAL);
	}

}
