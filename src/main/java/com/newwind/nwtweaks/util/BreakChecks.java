package com.newwind.nwtweaks.util;

import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TieredItem;
import net.minecraft.world.item.Tiers;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.TierSortingRegistry;
import se.mickelus.tetra.items.modular.ItemModularHandheld;
import se.mickelus.tetra.util.TierHelper;
import se.mickelus.tetra.util.ToolActionHelper;

import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;

import static java.util.Map.entry;

//This class was written while reading code from Force Correct Tool (https://www.curseforge.com/minecraft/mc-mods/force-correct-tool/)
public class BreakChecks {

	private static final Map<TagKey<Block>, Integer> tierMap = Collections.unmodifiableMap(Map.ofEntries(
					entry(Tags.Blocks.NEEDS_WOOD_TOOL, 0),
					entry(Tags.Blocks.NEEDS_GOLD_TOOL, 1),
					entry(BlockTags.NEEDS_STONE_TOOL, 2),
					entry(BlockTags.NEEDS_IRON_TOOL, 3),
					entry(BlockTags.NEEDS_DIAMOND_TOOL, 4),
					entry(Tags.Blocks.NEEDS_NETHERITE_TOOL, 5)
	));

	public static boolean check(Player player, BlockState state) {;
		if (player.isCreative())
			return true;

		var stack = player.getMainHandItem();

		if (stack.getItem() instanceof ItemModularHandheld)
			return checkTetraItem(stack, state);
		else
			return  stack.isCorrectToolForDrops(state) || checkTags(stack, state);
	}

	private static boolean checkTags(ItemStack stack, BlockState state) {
		var usableTools = new HashSet<String>();
		var blockTags = state.getTags().iterator();
		TagKey<Block> requiredTier = null;

		while (blockTags.hasNext()) {
			var tag = blockTags.next();
			var tagPath = tag.location().getPath();
			if (tagPath.startsWith("mineable/"))
				if (tagPath.contentEquals("mineable/hand"))
					return true;
				else
					usableTools.add(tagPath.substring(9));
			else if (tagPath.startsWith("needs_"))
				requiredTier = pickTier(requiredTier, tag);
		}

		var stackTags = stack.getTags().iterator();
		while (stackTags.hasNext()) {
			var tag = stackTags.next().location().getPath();
			if (tag.startsWith("tools/")) {
				var toolType = tag.substring(6);
				for (String usableTool : usableTools)
					if (toolType.matches("^%s.*".formatted(usableTool)))
						return checkTier(requiredTier, state, stack);
			}
		}

		return false;
	}

/*
	private static boolean checkTags(ItemStack ignoredStack, BlockState state) {
		var blockTags = state.getTags().iterator();

		while (blockTags.hasNext()) {
			if (blockTags.next().location().getPath().contentEquals("mineable/hand"))
				return true;
		}

		return false;
	}
*/

	private static boolean checkTier(TagKey<Block> requiredTier, BlockState state, ItemStack stack) {
		if (requiredTier == null)
			return true;
		var tierNum = tierMap.get(requiredTier);
		if (stack.getItem() instanceof TieredItem || tierNum != null) {
			var itemTier = ((TieredItem) stack.getItem()).getTier();
//			if (tierNum == 1)
//				return itemTier.equals(Tiers.GOLD);
//			if (tierNum > 0)
//				tierNum -= 1;
//			return itemTier.getLevel()
			return TierSortingRegistry.isCorrectTierForDrops(itemTier, state);
		} else
			return true;
	}

	private static TagKey<Block> pickTier(TagKey<Block> currentTag, TagKey<Block> newTag) {
		var currentNum = tierMap.get(currentTag);
		var newNum = tierMap.get(newTag);
		if (currentNum == null) {
			if (newNum == null)
				return null;
			else
				return newTag;
		} else if (newNum == null || currentNum > newNum)
			return currentTag;
		return newTag;

	}

	// Code from Tetra
	private static boolean checkTetraItem(ItemStack stack, BlockState state) {
		return ToolActionHelper.getAppropriateTools(state).stream().map((requiredTool) -> {
			return ((ItemModularHandheld) stack.getItem()).getHarvestTier(stack, requiredTool);
		}).map(TierHelper::getTier).filter(Objects::nonNull).anyMatch((tier) -> {
			return TierSortingRegistry.isCorrectTierForDrops(tier, state);
		});
	}

}
