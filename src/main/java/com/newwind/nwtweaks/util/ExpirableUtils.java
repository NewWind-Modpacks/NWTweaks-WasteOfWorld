package com.newwind.nwtweaks.util;

import com.newwind.nwtweaks.NWConfig;
import com.newwind.nwtweaks.access.IAbstractFurnaceBlockEntity;
import com.newwind.nwtweaks.capability.ExpirableContainer;
import com.newwind.nwtweaks.capability.ExpirableContainerProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.world.Container;
import net.minecraft.world.Containers;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntity;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class ExpirableUtils {

	public static void touch(BlockEntity blockEntity) {
		blockEntity.getCapability(ExpirableContainerProvider.CAPABILITY).ifPresent(ExpirableContainer::touch);
	}

	public static boolean isBlockLonely(BlockPos pos, Level level) {
		return level.getNearestPlayer(
						pos.getX(),
						pos.getY(),
						pos.getZ(),
						NWConfig.Common.CONTAINER_COMPANY_RANGE.get(),
						EntitySelector.NO_SPECTATORS)
						== null;
	}

	@SuppressWarnings("DataFlowIssue")
	public static void dropContents(BlockEntity blockEntity) {
		if (blockEntity instanceof Container container && blockEntity.hasLevel()) {
			Level level = blockEntity.getLevel();
			int containerSize = container.getContainerSize();
			for (int i = 0; i < containerSize; i++) {
				Containers.dropContents(level, blockEntity.getBlockPos(), container);
			}
		}
	}

	public static void dropInput(AbstractFurnaceBlockEntity blockEntity) {
		if (blockEntity.hasLevel()) {
			Level level = blockEntity.getLevel();
			assert level != null;
			BlockPos pos = blockEntity.getBlockPos();
			for (ItemStack stack : removeAndGetInputs(blockEntity))
				Containers.dropItemStack(level, pos.getX(), pos.getY(), pos.getZ(), stack);
		}
	}

	public static List<ItemStack> removeAndGetInputs(AbstractFurnaceBlockEntity blockEntity) {
		ItemStack[] stacks = new ItemStack[2];
		for (int i = 0; i < 2; i++)
			stacks[i] = blockEntity.removeItemNoUpdate(i);
		return Arrays.asList(stacks);
	}

	@SuppressWarnings("DataFlowIssue")
	public static boolean isWorthChecking(AbstractFurnaceBlockEntity blockEntity) {
		AtomicBoolean touched = new AtomicBoolean(false);
		blockEntity.getCapability(ExpirableContainerProvider.CAPABILITY).ifPresent(expirableContainer -> touched.set(expirableContainer.isTouched()));
		return touched.get()
						&& blockEntity.hasLevel()
						&& (
						blockEntity.getLevel().dimension() == Level.OVERWORLD
										|| ((IAbstractFurnaceBlockEntity) blockEntity).nWTweaks$isUnlit()
		)
						&& !blockEntity.isEmpty();
	}
}
