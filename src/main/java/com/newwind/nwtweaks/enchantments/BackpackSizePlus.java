package com.newwind.nwtweaks.enchantments;

import com.newwind.nwtweaks.NWConfig;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;

import java.util.List;

public class BackpackSizePlus extends Enchantment {
	public BackpackSizePlus(Rarity p_44676_, EnchantmentCategory p_44677_, EquipmentSlot... p_44678_) {
		super(p_44676_, p_44677_, p_44678_);
	}

	@Override
	public int getMaxLevel() {
		int maxLevel = NWConfig.Common.BACKPACK_SIZES.get().size() - 1;
		return Math.max(maxLevel, 1);
	}

	@Override
	public int getMinCost(int level) {
		List<? extends Integer> costs = NWConfig.Common.BSP_LEVEL_COSTS.get();
		if (costs.size() >= level)
			return costs.get(level - 1);
		return Integer.MAX_VALUE;
	}

	@Override
	public int getMaxCost(int level) {
		List<? extends Integer> costs = NWConfig.Common.BSP_LEVEL_COSTS.get();
		if (costs.size() > level)
			return costs.get(level);
		return Integer.MAX_VALUE;
	}
}
