package com.newwind.nwtweaks.mixin;

import com.mrcrayfish.backpacked.item.BackpackItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(BackpackItem.class)
public class BackpackedItem extends Item {

	public BackpackedItem(Properties properties) {
		super(properties);
	}

	@SuppressWarnings("deprecation")
	@Override
	public int getEnchantmentValue() {
		return 30;
	}

	@Override
	public boolean isEnchantable(@NotNull ItemStack stack) {
		return true;
	}


}
