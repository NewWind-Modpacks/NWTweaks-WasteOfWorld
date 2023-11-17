package com.newwind.nwtweaks.registries;

import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tiers;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import com.newwind.nwtweaks.NWTweaks;

public class Items {

	public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, NWTweaks.MODID);

	public static final RegistryObject<Item> BROKEN_IRON_SWORD = ITEMS.register(
		"broken_iron_sword", () -> new SwordItem(Tiers.WOOD, 2, -2.0F, new Item.Properties()
			.tab(CreativeModeTab.TAB_COMBAT)
			.durability(83)
			.defaultDurability(58)
			.setNoRepair()
		)
	);
	public static final RegistryObject<Item> PLANT_FIBER = ITEMS.register(
					"plant_fiber", () -> new Item(new Item.Properties()
									.tab(CreativeModeTab.TAB_MATERIALS)
					)
	);
	public static final RegistryObject<Item> PLANT_STRING = ITEMS.register(
					"plant_string", () -> new Item(new Item.Properties()
									.tab(CreativeModeTab.TAB_MATERIALS)
					)
	);
	
}
