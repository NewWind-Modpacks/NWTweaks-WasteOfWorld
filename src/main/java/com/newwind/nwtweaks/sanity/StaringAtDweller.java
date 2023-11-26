package com.newwind.nwtweaks.sanity;

import com.newwind.nwtweaks.NWConfig;
import com.newwind.nwtweaks.capability.RedDwellerProvider;
import croissantnova.sanitydim.capability.ISanity;
import croissantnova.sanitydim.passive.IPassiveSanitySource;
import de.cadentem.cave_dweller.entities.CaveDwellerEntity;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.entity.EntityTypeTest;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class StaringAtDweller implements IPassiveSanitySource {

	@Override
	public float get(@NotNull ServerPlayer player, @NotNull ISanity iSanity, @NotNull ResourceLocation resourceLocation) {
		Level level = player.getLevel();
		EntityTypeTest test = EntityTypeTest.forClass(CaveDwellerEntity.class);
		float range = 512F;
		AABB playerSurroundings = new AABB(player.position().add(new Vec3(-range, -range, -range)), player.position().add(new Vec3(range, range, range)));
		PlayerHelper helper = new PlayerHelper(player);

		List<CaveDwellerEntity> dwellers = level.getEntities(test, playerSurroundings, helper::isTargetterdPlayerLookingAtDweller);
		if (!dwellers.isEmpty()) {
			AtomicBoolean isRedDweller = new AtomicBoolean(false);

			for (CaveDwellerEntity dweller : dwellers) {
				dweller.getCapability(RedDwellerProvider.CAPABILITY).ifPresent(
								redDweller -> isRedDweller.set(isRedDweller.get() || redDweller.isRedDweller())
				);
			}

			if (isRedDweller.get())
				return -NWConfig.Common.RED_DWELLER_STARE_SANITY_MOD.get().floatValue();
			return -NWConfig.Common.DWELLER_STARE_SANITY_MOD.get().floatValue();
		}
		return 0F;
	}

	private record PlayerHelper(ServerPlayer player) {
		private boolean isTargetterdPlayerLookingAtDweller(Entity entity) {
			if (entity instanceof CaveDwellerEntity dweller) {
				return dweller.getTarget() == player && dweller.targetIsFacingMe && player.hasLineOfSight(entity);
			}
			return false;
		}
	}
}
