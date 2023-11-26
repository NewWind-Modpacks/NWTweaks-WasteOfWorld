package com.newwind.nwtweaks.registries;

import com.newwind.nwtweaks.NWTweaks;
import com.newwind.nwtweaks.world.items.PillItem;
import net.minecraft.world.item.*;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

// TODO: implement extra weapon parts
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
	public static final RegistryObject<Item> PILL_ITEM = ITEMS.register(
					"pill", () -> new PillItem(new Item.Properties()
									.tab(CreativeModeTab.TAB_BREWING)
					)
	);
	public static final RegistryObject<Item> CHIPPED_STONE = ITEMS.register(
					"chipped_stone", () -> new BlockItem(Blocks.CHIPPED_STONE.get(),
									new Item.Properties()
													.tab(CreativeModeTab.TAB_BUILDING_BLOCKS)
					)
	);
	public static final RegistryObject<Item> CHIPPED_DEEPSLATE = ITEMS.register(
					"chipped_deepslate", () -> new BlockItem(Blocks.CHIPPED_DEEPSLATE.get(),
									new Item.Properties()
													.tab(CreativeModeTab.TAB_BUILDING_BLOCKS)
					)
	);

}