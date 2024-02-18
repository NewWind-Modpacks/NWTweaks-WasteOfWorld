package com.newwind.nwtweaks.mixin.client;

import com.bawnorton.mixinsquared.TargetHandler;
import com.newwind.nwtweaks.util.MixinExternalFunctions;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@SuppressWarnings({"MixinAnnotationTarget", "InvalidMemberReference", "UnresolvedMixinReference", "CancellableInjectionUsage"})
@Mixin(value = PlayerModel.class, priority = 1500)
public abstract class MixinPCPlayerModelMixin<T extends LivingEntity> extends HumanoidModel<T> {

	public MixinPCPlayerModelMixin(ModelPart p_170677_) {
		super(p_170677_);
	} // DUMMY

	@TargetHandler(
					mixin = "com.alrex.parcool.mixin.client.PlayerModelMixin",
					name = "onSetupAnimHead"
	)
	@Inject(
					method = "@MixinSquared:Handler",
					at = @At(
									value = "INVOKE",
									target = "Lcom/alrex/parcool/config/ParCoolConfig$Client$Booleans;get()Ljava/lang/Boolean;"
					),
					locals = LocalCapture.CAPTURE_FAILHARD,
					cancellable = true
	)
	private void negateParcoolHead(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, CallbackInfo superCi, CallbackInfo ci, PlayerModel<T> model, Player player) {
		if (MixinExternalFunctions.hasAnimationDisablerStack(player))
			ci.cancel();
	}

	@TargetHandler(
					mixin = "com.alrex.parcool.mixin.client.PlayerModelMixin",
					name = "onSetupAnimTail"
	)
	@Inject(
					method = "@MixinSquared:Handler",
					at = @At(
									value = "INVOKE",
									target = "Lcom/alrex/parcool/config/ParCoolConfig$Client$Booleans;get()Ljava/lang/Boolean;"
					),
					locals = LocalCapture.CAPTURE_FAILHARD,
					cancellable = true
	)
	private void negateParcoolTail(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, CallbackInfo superCi, CallbackInfo ci, Player player) {
		if (MixinExternalFunctions.hasAnimationDisablerStack(player))
			ci.cancel();
	}

}

