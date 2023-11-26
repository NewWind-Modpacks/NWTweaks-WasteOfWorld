package com.newwind.nwtweaks.world.blocks;

import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import org.jetbrains.annotations.NotNull;

import static net.minecraft.world.level.block.RotatedPillarBlock.AXIS;

public class ChippedPillarBlock extends ChippedBlock {
	public ChippedPillarBlock(Properties p_49795_) {
		super(p_49795_);
		this.registerDefaultState(this.defaultBlockState().setValue(STAGE, 0).setValue(AXIS, Direction.Axis.Y));
	}

	@Override
	public @NotNull BlockState rotate(BlockState p_55930_, Rotation p_55931_) {
		return rotatePillar(p_55930_, p_55931_);
	}

	public static BlockState rotatePillar(BlockState state, Rotation rotation) {
		switch (rotation) {
			case COUNTERCLOCKWISE_90:
			case CLOCKWISE_90:
				switch ((Direction.Axis)state.getValue(AXIS)) {
					case X:
						return state.setValue(AXIS, Direction.Axis.Z);
					case Z:
						return state.setValue(AXIS, Direction.Axis.X);
					default:
						return state;
				}
			default:
				return state;
		}
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> state) {
		super.createBlockStateDefinition(state);
		state.add(AXIS);
	}

	@Override
	public BlockState getStateForPlacement(BlockPlaceContext context) {
		return this.defaultBlockState().setValue(AXIS, context.getClickedFace().getAxis());
	}
}
