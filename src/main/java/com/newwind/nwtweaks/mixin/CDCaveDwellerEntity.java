package com.newwind.nwtweaks.mixin;

import com.newwind.nwtweaks.access.ICaveDwellerEntity;
import com.newwind.nwtweaks.capability.RedDwellerProvider;
import de.cadentem.cave_dweller.entities.CaveDwellerEntity;
import de.cadentem.cave_dweller.util.Utils;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.concurrent.atomic.AtomicBoolean;

@Mixin(CaveDwellerEntity.class)
public abstract class CDCaveDwellerEntity extends Monster implements ICaveDwellerEntity {
	@Unique
	private Player nWTweaks$hasBeenHitByPlayer = null;

	// Dummy
	protected CDCaveDwellerEntity(EntityType<? extends Monster> p_33002_, Level p_33003_) {
		super(p_33002_, p_33003_);
	}

	@Override
	public boolean doHurtTarget(@NotNull Entity p_21372_) {
		float f = (float) this.getAttributeValue(Attributes.ATTACK_DAMAGE);
		float f1 = (float) this.getAttributeValue(Attributes.ATTACK_KNOCKBACK);
		if (p_21372_ instanceof LivingEntity) {
			f += EnchantmentHelper.getDamageBonus(this.getMainHandItem(), ((LivingEntity) p_21372_).getMobType());
			f1 += (float) EnchantmentHelper.getKnockbackBonus(this);
		}

		int i = EnchantmentHelper.getFireAspect(this);
		if (i > 0) {
			p_21372_.setSecondsOnFire(i * 4);
		}

		DamageSource damageSource = DamageSource.mobAttack(this);
		AtomicBoolean isRedDweller = new AtomicBoolean(false);
		this.getCapability(RedDwellerProvider.CAPABILITY).ifPresent(redDweller -> isRedDweller.set(redDweller.isRedDweller()));
		if (isRedDweller.get())
			damageSource = damageSource.bypassArmor().bypassEnchantments().bypassMagic();

		boolean flag = p_21372_.hurt(damageSource, f);
		if (flag) {
			if (f1 > 0.0F && p_21372_ instanceof LivingEntity) {
				((LivingEntity) p_21372_).knockback(f1 * 0.5F, Mth.sin(this.getYRot() * ((float) Math.PI / 180F)), -Mth.cos(this.getYRot() * ((float) Math.PI / 180F)));
				this.setDeltaMovement(this.getDeltaMovement().multiply(0.6D, 1.0D, 0.6D));
			}

			this.doEnchantDamageEffects(this, p_21372_);
			this.setLastHurtMob(p_21372_);
		}

		return flag;
	}

	@Inject(
					method = "disappear",
					at = @At("HEAD"),
					cancellable = true,
					remap = false
	)
	private void makeRedPersistent(CallbackInfo ci) {
		AtomicBoolean isRedDweller = new AtomicBoolean(false);
		this.getCapability(RedDwellerProvider.CAPABILITY).ifPresent(redDweller -> isRedDweller.set(redDweller.isRedDweller()));
		if (isRedDweller.get() && (Utils.isValidTarget(this.getTarget()) || this.getTarget().isInvisible()))
			ci.cancel();
	}

	@Override
	public boolean isInvulnerableTo(@NotNull DamageSource source) {
		AtomicBoolean isRedDweller = new AtomicBoolean(false);
		this.getCapability(RedDwellerProvider.CAPABILITY).ifPresent(redDweller -> isRedDweller.set(redDweller.isRedDweller()));
		if (isRedDweller.get() && !source.isBypassInvul() || source.isMagic())
			return true;
		return super.isInvulnerableTo(source);
	}

	@Override
	public boolean hurt(DamageSource source, float damage) {
		Entity entity = source.getEntity();
		if (entity != null && entity.equals(this.getTarget()) && Utils.isValidTarget(entity))
			nWTweaks$hasBeenHitByPlayer = (Player) entity;

		return super.hurt(source, damage);
	}

	@Override
	public Player nWTweaks$attackedByPlayer() {
		return nWTweaks$hasBeenHitByPlayer;
	}

	@Inject(
					method = "startRiding",
					at = @At("HEAD"),
					cancellable = true,
					remap = false
	)
	public void startRidingFilter(Entity vehicle, boolean force, CallbackInfoReturnable<Boolean> cir) {
		if (vehicle instanceof LivingEntity)
			cir.setReturnValue(super.startRiding(vehicle, force));
	}

	@Inject(
					method = "canRide",
					at = @At("HEAD"),
					cancellable = true,
					remap = false
	)
	public void canRideFilter(Entity vehicle, CallbackInfoReturnable<Boolean> cir) {
		if (vehicle instanceof LivingEntity)
			cir.setReturnValue(super.canRide(vehicle));
	}

}
