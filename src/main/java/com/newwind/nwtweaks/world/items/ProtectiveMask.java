package com.newwind.nwtweaks.world.items;

import com.newwind.nwtweaks.NWConfig;
import com.newwind.nwtweaks.registries.Items;
import com.newwind.nwtweaks.util.CommonUtils;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotResult;

import javax.annotation.Nullable;

public class ProtectiveMask extends Item {

	public ProtectiveMask(Properties p_41383_) {
		super(p_41383_);
	}

	@Nullable
	public static ItemStack getMaskStack(Player player) {
		var result = CuriosApi.getCuriosHelper().findFirstCurio(player, Items.PROTECTIVE_MASK.get());
		return result.map(SlotResult::stack).orElse(null);
	}

	public static int getAir(ItemStack stack) {
		return stack.getOrCreateTag().getInt("Air");
	}

	public static void setAir(ItemStack stack, int air) {
		stack.getOrCreateTag().putInt("Air", air);
	}

	public static void modifyAir(ItemStack stack, int air) {
		CompoundTag tag = stack.getOrCreateTag();
		tag.putInt("Air", Math.min(tag.getInt("Air") + air, NWConfig.Common.MASK_CAPACITY.get().get(0)));
	}

	public static void refill(ItemStack stack) {
		modifyAir(stack, NWConfig.Common.MASK_REFILL_SPEED.get());
	}
}
