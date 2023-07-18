package com.newwind.nwtweaks.registries;

import com.mrcrayfish.backpacked.Backpacked;
import com.newwind.nwtweaks.NWTweaks;
import com.newwind.nwtweaks.enchantments.BackpackSizePlus;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class Enchantments {

	public static final DeferredRegister<Enchantment> ENCHANTMENTS = DeferredRegister.create(ForgeRegistries.ENCHANTMENTS, NWTweaks.MODID);

	public static final RegistryObject<Enchantment> BACKPACK_SIZE_PLUS = ENCHANTMENTS.register(
					"deep_pockets", () -> new BackpackSizePlus(
									Enchantment.Rarity.VERY_RARE,
									Backpacked.ENCHANTMENT_TYPE
					));

	public static void register(IEventBus bus) {
		ENCHANTMENTS.register(bus);
	}

}
