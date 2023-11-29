package com.newwind.nwtweaks.mixin;

import com.newwind.nwtweaks.registries.Blocks;
import com.newwind.nwtweaks.world.blocks.ChippedBlock;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Tiers;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.TierSortingRegistry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(TierSortingRegistry.class)
public class ForgeTierSortingRegistry {

	@Redirect(
					method = "isCorrectTierForDrops",
					at = @At(
									value = "INVOKE",
									target = "Lnet/minecraft/world/level/block/state/BlockState;is(Lnet/minecraft/tags/TagKey;)Z"
					)
	)
	private static boolean modifyRequiredTier(BlockState state, TagKey<Block> tagKey) {
		Block block = state.getBlock();
		if (block instanceof ChippedBlock) {
			int stage = state.getValue(ChippedBlock.STAGE);
			if (stage >= 2) {
				if (block == Blocks.CHIPPED_DEEPSLATE.get()
								|| block == Blocks.CHIPPED_TUFF.get()
								|| block == Blocks.CHIPPED_BLACKSTONE.get())
					return tagKey.equals(Tiers.NETHERITE.getTag());
				return tagKey.equals(Tiers.DIAMOND.getTag());
			}
		}
		return state.is(tagKey);
	}

}
