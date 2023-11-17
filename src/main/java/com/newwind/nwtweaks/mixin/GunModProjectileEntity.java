package com.newwind.nwtweaks.mixin;

import com.mrcrayfish.guns.entity.ProjectileEntity;
import com.newwind.nwtweaks.registries.Attributes;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.common.ForgeConfigSpec;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ProjectileEntity.class)
public class GunModProjectileEntity {

	@Shadow(remap = false)
	protected LivingEntity shooter;

	@Redirect(
					method = "onHitEntity",
					at = @At(
									value = "INVOKE",
									target = "Lnet/minecraftforge/common/ForgeConfigSpec$DoubleValue;get()Ljava/lang/Object;"
					),
					remap = false
	)
	public Object getHeadshotMultiplier(ForgeConfigSpec.DoubleValue instance) {
		return instance.get() + shooter.getAttributeValue(Attributes.HEADSHOT_DAMAGE.get());
	}

}
