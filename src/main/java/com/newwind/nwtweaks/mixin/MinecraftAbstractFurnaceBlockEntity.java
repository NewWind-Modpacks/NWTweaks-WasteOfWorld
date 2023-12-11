package com.newwind.nwtweaks.mixin;

import com.newwind.nwtweaks.NWConfig;
import com.newwind.nwtweaks.access.IAbstractFurnaceBlockEntity;
import com.newwind.nwtweaks.capability.ExpirableContainer;
import com.newwind.nwtweaks.capability.ExpirableContainerProvider;
import com.newwind.nwtweaks.util.ExpirableUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.concurrent.atomic.AtomicReference;

@Mixin(AbstractFurnaceBlockEntity.class)
public abstract class MinecraftAbstractFurnaceBlockEntity implements IAbstractFurnaceBlockEntity {

	@Inject(
					method = "serverTick",
					at = @At("TAIL")
	)
	private static void onTick(Level level, BlockPos pos, BlockState state, AbstractFurnaceBlockEntity blockEntity, CallbackInfo ci) {
		AtomicReference<ExpirableContainer> expirableContainerAtomic = new AtomicReference<>();
		blockEntity.getCapability(ExpirableContainerProvider.CAPABILITY).ifPresent(expirableContainerAtomic::set);
		ExpirableContainer expirableContainer = expirableContainerAtomic.get();

		if (expirableContainer != null) {
			if (ExpirableUtils.isWorthChecking(blockEntity) && ExpirableUtils.isBlockLonely(pos, level)) {
				if (expirableContainer.setAlone(level))
					blockEntity.setChanged();
				long loneStart = expirableContainer.getLoneStart();
				long loneTarget = loneStart + NWConfig.Common.CONTAINER_LONE_TICKS.get();
				long gameTime = level.getGameTime();
				if (loneStart >= 0 && loneTarget <= gameTime)
					if (loneTarget + 6000 > gameTime)
						ExpirableUtils.dropContents(blockEntity);
					else
						blockEntity.clearContent();
				else if (NWConfig.Common.CONTAINER_DROP_INPUT_INSTANTLY.get() && ((IAbstractFurnaceBlockEntity) blockEntity).nWTweaks$isUnlit()) {
					if (loneStart + 6000 > gameTime)
						ExpirableUtils.dropInput(blockEntity);
					else
						ExpirableUtils.removeAndGetInputs(blockEntity);
				}
			} else {
				if (expirableContainer.setInCompany())
					blockEntity.setChanged();
			}
		}
	}

	@Shadow
	protected abstract boolean isLit();

//	@Inject(
//					method = "setItem",
//					at = @At("HEAD")
//	)
//	private void onSetItem(int p_58333_, ItemStack p_58334_, CallbackInfo ci) {
//		ExpirableUtils.touch(((AbstractFurnaceBlockEntity) ((Object) this)));
//	}
//
//	@Inject(
//					method = "removeItem",
//					at = @At("HEAD")
//	)
//	private void onRemoveItem(int p_58330_, int p_58331_, CallbackInfoReturnable<ItemStack> cir) {
//		ExpirableUtils.touch(((AbstractFurnaceBlockEntity) ((Object) this)));
//	}
//
//	@Inject(
//					method = "removeItemNoUpdate",
//					at = @At("HEAD")
//	)
//	private void onRemoveItemNoUpdate(int p_58387_, CallbackInfoReturnable<ItemStack> cir) {
//		ExpirableUtils.touch(((AbstractFurnaceBlockEntity) ((Object) this)));
//	}

	@Unique
	public boolean nWTweaks$isUnlit() {
		return !this.isLit();
	}

}
