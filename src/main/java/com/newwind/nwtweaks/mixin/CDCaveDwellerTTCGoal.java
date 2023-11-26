package com.newwind.nwtweaks.mixin;

import com.newwind.nwtweaks.access.ICaveDwellerEntity;
import de.cadentem.cave_dweller.entities.CaveDwellerEntity;
import de.cadentem.cave_dweller.entities.goals.CaveDwellerTargetTooCloseGoal;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(CaveDwellerTargetTooCloseGoal.class)
public class CDCaveDwellerTTCGoal extends NearestAttackableTargetGoal<Player> {

	@Mutable
	@Final
	@Shadow(remap = false)
	private final CaveDwellerEntity caveDweller;

	// Dummy
	public CDCaveDwellerTTCGoal(Mob p_26060_, Class<Player> p_26061_, boolean p_26062_, CaveDwellerEntity caveDweller) {
		super(p_26060_, p_26061_, p_26062_);
		this.caveDweller = caveDweller;
	}


	@Inject(
					method = "canUse",
					at = @At("HEAD"),
					cancellable = true
	)
	private void wasHitByPlayer(CallbackInfoReturnable<Boolean> cir) {
		Player player = ((ICaveDwellerEntity) this.caveDweller).nWTweaks$attackedByPlayer();
		if (player != null) {
			this.target = player;
			cir.setReturnValue(true);
		}

	}

}
