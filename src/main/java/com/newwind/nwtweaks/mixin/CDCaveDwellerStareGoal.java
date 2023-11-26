package com.newwind.nwtweaks.mixin;

import com.newwind.nwtweaks.NWConfig;
import croissantnova.sanitydim.capability.SanityProvider;
import de.cadentem.cave_dweller.entities.goals.CaveDwellerStareGoal;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.concurrent.atomic.AtomicBoolean;

@Mixin(CaveDwellerStareGoal.class)
public class CDCaveDwellerStareGoal {

	@Redirect(
					method = "tick",
					at = @At(
									value = "INVOKE",
									target = "Lnet/minecraft/world/entity/LivingEntity;hasLineOfSight(Lnet/minecraft/world/entity/Entity;)Z"
					)
	)
	private boolean isSaneEnough(LivingEntity target, Entity dweller) {
		boolean hasLineOfSight = target.hasLineOfSight(dweller);
		if (!hasLineOfSight) return false;
		if (target instanceof Player player) {
			AtomicBoolean isSaneEnough = new AtomicBoolean(false);
			player.getCapability(SanityProvider.CAP).ifPresent(iSanity -> {
				double minSanity = NWConfig.Common.DWELLER_MIN_STARE_SANITY.get();
				isSaneEnough.set(1F - iSanity.getSanity() >= minSanity);
			});
			return isSaneEnough.get();
		}
		return true;
	}

}
