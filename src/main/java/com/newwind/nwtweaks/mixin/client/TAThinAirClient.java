package com.newwind.nwtweaks.mixin.client;

import com.mojang.datafixers.util.Pair;
import com.newwind.nwtweaks.util.MixinExternalFunctions;
import fuzs.puzzleslib.client.core.ClientModConstructor;
import fuzs.thinair.ThinAir;
import fuzs.thinair.advancements.AirSource;
import fuzs.thinair.client.ThinAirClient;
import fuzs.thinair.helper.AirHelper;
import fuzs.thinair.helper.AirQualityLevel;
import fuzs.thinair.init.ModRegistry;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.ItemLike;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(ThinAirClient.class)
public class TAThinAirClient {


	/**
	 * @author Kevadroz
	 * @reason Implement on-player soul source check && IsUnderground.
	 * @implNote Copied from original class
	 */
	@Overwrite(
					remap = false
	)
	public void onRegisterItemModelProperties(ClientModConstructor.ItemModelPropertiesContext context) {
		context.registerItemProperty(ThinAir.id("air_quality"), (stack, level, maybeEntity, seed) -> {
			Entity entity = maybeEntity != null ? maybeEntity : stack.getEntityRepresentation();
			if (entity != null) {
				float var10000;
				// Start of modded code
				Pair<AirQualityLevel, AirSource> o2Pair = MixinExternalFunctions.CaveOxygen.getO2FromLantern(entity);
				if ( entity instanceof Player player
								&& o2Pair.getSecond() == AirSource.DIMENSION
								&& (o2Pair.getFirst() == AirQualityLevel.YELLOW || o2Pair.getFirst() == AirQualityLevel.RED)
								&& MixinExternalFunctions.CaveOxygen.hasValidSoulSource(player))
					o2Pair = new Pair<>(AirQualityLevel.BLUE, AirSource.DIMENSION);
				// End of modded code
				var10000 = switch (o2Pair.getFirst()) {
					case RED -> 0.0F;
					case YELLOW -> 0.1F;
					case BLUE -> 0.2F;
					case GREEN -> 0.3F;
					default -> throw new IncompatibleClassChangeError();
				};

				return var10000;
			} else {
				return 0.3F;
			}
		}, new ItemLike[]{(ItemLike) ModRegistry.SAFETY_LANTERN_BLOCK.get()});
	}

}
