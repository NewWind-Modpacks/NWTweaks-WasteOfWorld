package com.newwind.nwtweaks.mixin;

import com.newwind.nwtweaks.sanity.SanityCommon;
import croissantnova.sanitydim.SanityProcessor;
import croissantnova.sanitydim.capability.ISanity;
import croissantnova.sanitydim.config.ConfigProxy;
import croissantnova.sanitydim.passive.IPassiveSanitySource;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Iterator;

import static croissantnova.sanitydim.SanityProcessor.getGarlandMultiplier;
import static croissantnova.sanitydim.SanityProcessor.getSanityMultiplier;

@Mixin(SanityProcessor.class)
public class SaProcessor {

	@Unique
	private static boolean nWTweaks$IsNewCall;
	@Unique
	private static ServerPlayer nWTweaks$player;
	@Unique
	private static ISanity nWTweaks$sanity;
	@Unique
	private static ResourceLocation nWTweaks$dim;

	@ModifyVariable(
					method = "calcPassive",
					at = @At(
									value = "STORE"
					),
					ordinal = 0,
					remap = false
	)
	private static float calculateExtra(float passive) {
		if (!nWTweaks$IsNewCall)
			return passive;

		float val;
		for (Iterator var4 = SanityCommon.PASSIVE_SANITY_SOURCES.iterator(); var4.hasNext(); passive += val) {
			IPassiveSanitySource pss = (IPassiveSanitySource) var4.next();
			val = pss.get(nWTweaks$player, nWTweaks$sanity, nWTweaks$dim);
			val /= 100;
			val *= getSanityMultiplier(nWTweaks$player, val);
		}

		nWTweaks$IsNewCall = false;
		return passive;
	}

	@Inject(
					method = "calcPassive",
					at = @At("HEAD"),
					remap = false
	)
	private static void captureParamethers(ServerPlayer player, ISanity sanity, CallbackInfoReturnable<Float> cir) {
		nWTweaks$IsNewCall = true;
		nWTweaks$player = player;
		nWTweaks$sanity = sanity;
		nWTweaks$dim = player.level.dimension().location();
	}

	/**
	 * @author Kevadroz
	 * @reason TODO: Implement skill multipliers.
	 */
	@Overwrite(
					remap = false
	)
	public static float getSanityMultiplier(ServerPlayer player, float value) {
		ResourceLocation dim = player.level.dimension().location();
		return value >= 0.0F ? ConfigProxy.getNegMul(dim) * getGarlandMultiplier(player) : ConfigProxy.getPosMul(dim);
	}

}
