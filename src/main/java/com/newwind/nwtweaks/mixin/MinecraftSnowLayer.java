package com.newwind.nwtweaks.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SnowLayerBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import snownee.snow.Hooks;
import snownee.snow.block.SnowVariant;

import static net.minecraft.world.level.block.SnowLayerBlock.LAYERS;

@Mixin(value = SnowLayerBlock.class, priority = 1001)
public class MinecraftSnowLayer extends Block implements SnowVariant {

	public MinecraftSnowLayer(Properties p_49795_) {
		super(p_49795_);
	}

	/**
	 * @author Kevadroz (All code from Snow! Real Magic!)
	 */
	@Override
	@SuppressWarnings("deprecation")
	public @NotNull InteractionResult use(@NotNull BlockState state, @NotNull Level worldIn, @NotNull BlockPos pos, @NotNull Player player, @NotNull InteractionHand handIn, @NotNull BlockHitResult hit) {
		if (state.is(Blocks.SNOW)) {
			BlockPlaceContext context = new BlockPlaceContext(player, handIn, player.getItemInHand(handIn), hit);
			Block block = Block.byItem(context.getItemInHand().getItem());
			if (block != null && block != Blocks.AIR && context.replacingClickedOnBlock()) {
				BlockState state2 = block.getStateForPlacement(context);
				if (state2 != null && Hooks.canContainState(state2) && state2.canSurvive(worldIn, pos)) {
					if (!worldIn.isClientSide) {
						worldIn.setBlock(pos, state2, 16 | 32);
						block.setPlacedBy(worldIn, pos, state, player, context.getItemInHand());
						int i = state.getValue(LAYERS);
						if (Hooks.placeLayersOn(worldIn, pos, i, false, context, true, true) && !player.isCreative()) {
							context.getItemInHand().shrink(1);
						}
					}
					return InteractionResult.SUCCESS;
				}
			}
		}
		return super.use(state, worldIn, pos, player, handIn, hit);
	}



}
