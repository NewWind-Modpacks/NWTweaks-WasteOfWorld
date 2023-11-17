package com.newwind.nwtweaks.mixin;

import com.mojang.datafixers.util.Pair;
import com.newwind.nwtweaks.util.MixinExternalFunctions;
import fuzs.thinair.advancements.AirSource;
import fuzs.thinair.handler.TickAirChecker;
import fuzs.thinair.helper.AirQualityLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TickAirChecker.class)
public class TATickAirChecker {

	@Unique
	private static LivingEntity nWTweaks$capturedEntity;

	@Inject(
					method = "onLivingTick",
					at = @At(
									value = "INVOKE",
									target = "Lfuzs/thinair/helper/AirHelper;getO2LevelFromLocation(Lnet/minecraft/world/phys/Vec3;Lnet/minecraft/world/level/Level;)Lcom/mojang/datafixers/util/Pair;"
					),
					remap = false
	)
	private static void nWTweaks$onGetAir(LivingEntity entity, CallbackInfo ci) {
		nWTweaks$capturedEntity = entity;
	}

	@Redirect(
					method = "onLivingTick",
					at = @At(
									value = "INVOKE",
									target = "Lfuzs/thinair/helper/AirHelper;getO2LevelFromLocation(Lnet/minecraft/world/phys/Vec3;Lnet/minecraft/world/level/Level;)Lcom/mojang/datafixers/util/Pair;"
					),
					remap = false
	)
	private static Pair<AirQualityLevel, AirSource> nWTweaks$newGetAir(Vec3 source, Level bs) {
		Pair<AirQualityLevel, AirSource> o2Pair = MixinExternalFunctions.CaveOxygen.getO2FromEntity(nWTweaks$capturedEntity);
			return MixinExternalFunctions.CaveOxygen.soulifyO2Pair(nWTweaks$capturedEntity, o2Pair);
	}

}
