package com.newwind.nwtweaks.mixin;

import com.newwind.nwtweaks.util.CommonUtils;
import de.maxhenkel.corpse.corelib.death.Death;
import de.maxhenkel.corpse.entities.CorpseEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.levelgen.Heightmap;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(CorpseEntity.class)
public class CoCorpseEntity {

	@Inject(
					method = "createFromDeath",
					at = @At(
									value = "INVOKE",
									target = "Lde/maxhenkel/corpse/entities/CorpseEntity;setYRot(F)V"
					),
					locals = LocalCapture.CAPTURE_FAILHARD
	)
	private static void onSetPos(Player player, Death death, CallbackInfoReturnable<CorpseEntity> cir, CorpseEntity corpse) {
		if (CommonUtils.isUnderground(player)) {
			BlockPos spawnPos = player.level.getHeightmapPos(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, new BlockPos(death.getPosX(), death.getPosY(), death.getPosZ()));
			corpse.setPos(spawnPos.getX(), spawnPos.getY(), spawnPos.getZ());
		}
	}

}
