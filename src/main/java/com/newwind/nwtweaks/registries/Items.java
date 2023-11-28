package com.newwind.nwtweaks.registries;

import com.newwind.nwtweaks.NWTweaks;
import com.newwind.nwtweaks.world.items.PillItem;
import net.minecraft.world.item.*;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

// TODO: implement extra weapon parts
@SuppressWarnings("unused")
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
	public static final RegistryObject<Item> CHIPPED_GRANITE = ITEMS.register(
					"chipped_granite", () -> new BlockItem(Blocks.CHIPPED_GRANITE.get(),
									new Item.Properties()
													.tab(CreativeModeTab.TAB_BUILDING_BLOCKS)
					)
	);
	public static final RegistryObject<Item> CHIPPED_DIORITE = ITEMS.register(
					"chipped_diorite", () -> new BlockItem(Blocks.CHIPPED_DIORITE.get(),
									new Item.Properties()
													.tab(CreativeModeTab.TAB_BUILDING_BLOCKS)
					)
	);
	public static final RegistryObject<Item> CHIPPED_ANDESITE = ITEMS.register(
					"chipped_andesite", () -> new BlockItem(Blocks.CHIPPED_ANDESITE.get(),
									new Item.Properties()
													.tab(CreativeModeTab.TAB_BUILDING_BLOCKS)
					)
	);
	public static final RegistryObject<Item> CHIPPED_TUFF = ITEMS.register(
					"chipped_tuff", () -> new BlockItem(Blocks.CHIPPED_TUFF.get(),
									new Item.Properties()
													.tab(CreativeModeTab.TAB_BUILDING_BLOCKS)
					)
	);
	public static final RegistryObject<Item> CHIPPED_DRIPSTONE = ITEMS.register(
					"chipped_dripstone_block", () -> new BlockItem(Blocks.CHIPPED_DRIPSTONE.get(),
									new Item.Properties()
													.tab(CreativeModeTab.TAB_BUILDING_BLOCKS)
					)
	);
	public static final RegistryObject<Item> CHIPPED_BLACKSTONE = ITEMS.register(
					"chipped_blackstone", () -> new BlockItem(Blocks.CHIPPED_BLACKSTONE.get(),
									new Item.Properties()
													.tab(CreativeModeTab.TAB_BUILDING_BLOCKS)
					)
	);
	public static final RegistryObject<Item> STONE_ROCK = ITEMS.register(
					"stone_rock", () -> new Item(new Item.Properties()
									.tab(CreativeModeTab.TAB_MATERIALS)
					)
	);
	public static final RegistryObject<Item> DEEPSLATE_ROCK = ITEMS.register(
					"deepslate_rock", () -> new Item(new Item.Properties()
									.tab(CreativeModeTab.TAB_MATERIALS)
					)
	);
	public static final RegistryObject<Item> GRANITE_ROCK = ITEMS.register(
					"granite_rock", () -> new Item(new Item.Properties()
									.tab(CreativeModeTab.TAB_MATERIALS)
					)
	);
	public static final RegistryObject<Item> DIORITE_ROCK = ITEMS.register(
					"diorite_rock", () -> new Item(new Item.Properties()
									.tab(CreativeModeTab.TAB_MATERIALS)
					)
	);
	public static final RegistryObject<Item> ANDESITE_ROCK = ITEMS.register(
					"andesite_rock", () -> new Item(new Item.Properties()
									.tab(CreativeModeTab.TAB_MATERIALS)
					)
	);
	public static final RegistryObject<Item> TUFF_ROCK = ITEMS.register(
					"tuff_rock", () -> new Item(new Item.Properties()
									.tab(CreativeModeTab.TAB_MATERIALS)
					)
	);
	public static final RegistryObject<Item> DRIPSTONE_ROCK = ITEMS.register(
					"dripstone_rock", () -> new Item(new Item.Properties()
									.tab(CreativeModeTab.TAB_MATERIALS)
					)
	);
	public static final RegistryObject<Item> BLACKSTONE_ROCK = ITEMS.register(
					"blackstone_rock", () -> new Item(new Item.Properties()
									.tab(CreativeModeTab.TAB_MATERIALS)
					)
	);
	public static final RegistryObject<Item> COBBLED_GRANITE = ITEMS.register(
					"cobbled_granite", () -> new BlockItem(Blocks.COBBLED_GRANITE.get(),
									new Item.Properties()
													.tab(CreativeModeTab.TAB_BUILDING_BLOCKS)
					)
	);
	public static final RegistryObject<Item> COBBLED_DIORITE = ITEMS.register(
					"cobbled_diorite", () -> new BlockItem(Blocks.COBBLED_DIORITE.get(),
									new Item.Properties()
													.tab(CreativeModeTab.TAB_BUILDING_BLOCKS)
					)
	);
	public static final RegistryObject<Item> COBBLED_ANDESITE = ITEMS.register(
					"cobbled_andesite", () -> new BlockItem(Blocks.COBBLED_ANDESITE.get(),
									new Item.Properties()
													.tab(CreativeModeTab.TAB_BUILDING_BLOCKS)
					)
	);
	public static final RegistryObject<Item> COBBLED_TUFF = ITEMS.register(
					"cobbled_tuff", () -> new BlockItem(Blocks.COBBLED_TUFF.get(),
									new Item.Properties()
													.tab(CreativeModeTab.TAB_BUILDING_BLOCKS)
					)
	);
	public static final RegistryObject<Item> COBBLED_DRIPSTONE = ITEMS.register(
					"cobbled_dripstone_block", () -> new BlockItem(Blocks.COBBLED_DRIPSTONE.get(),
									new Item.Properties()
													.tab(CreativeModeTab.TAB_BUILDING_BLOCKS)
					)
	);
	public static final RegistryObject<Item> COBBLED_BLACKSTONE = ITEMS.register(
					"cobbled_blackstone", () -> new BlockItem(Blocks.COBBLED_BLACKSTONE.get(),
									new Item.Properties()
													.tab(CreativeModeTab.TAB_BUILDING_BLOCKS)
					)
	);

}
