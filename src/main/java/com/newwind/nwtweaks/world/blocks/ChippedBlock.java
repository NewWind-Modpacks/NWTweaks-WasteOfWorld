package com.newwind.nwtweaks.world.blocks;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;

public class ChippedBlock extends Block {

	public static final int MAX_STAGE = 2;

	public static final IntegerProperty STAGE = IntegerProperty.create("stage", 0, MAX_STAGE);

	public ChippedBlock(Properties p_49795_) {
		super(p_49795_);

		this.registerDefaultState(this.defaultBlockState().setValue(STAGE, 0));
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> p_49915_) {
		p_49915_.add(STAGE);
	}

}
