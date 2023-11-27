package com.newwind.nwtweaks.registries;

import com.newwind.nwtweaks.NWTweaks;
import com.newwind.nwtweaks.world.blocks.ChippedBlock;
import com.newwind.nwtweaks.world.blocks.ChippedPillarBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

@SuppressWarnings("unused")
public class Blocks {

//	public static final DeferredRegister<Block> VANILLA_BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, "minecraft");

//	public static final RegistryObject<Block> SNOW = VANILLA_BLOCKS.register(
//					"snow", () -> new SnowLayerBlock(
//									BlockBehaviour.Properties.of(Material.TOP_SNOW)
//													.randomTicks()
//													.strength(0.1F)
//													.requiresCorrectToolForDrops()
//													.sound(SoundType.SNOW)
//													.isViewBlocking((p_187417_, p_187418_, p_187419_) -> false)));

	public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, NWTweaks.MODID);

	public static final RegistryObject<Block> CHIPPED_STONE = BLOCKS.register(
					"chipped_stone", () -> new ChippedBlock(
									BlockBehaviour.Properties.of(Material.STONE)
													.strength(3f, 6f)
													.requiresCorrectToolForDrops()
													.sound(SoundType.STONE)
													.color(MaterialColor.STONE)
					)
	);

	public static final RegistryObject<Block> CHIPPED_DEEPSLATE = BLOCKS.register(
					"chipped_deepslate", () -> new ChippedPillarBlock(
									BlockBehaviour.Properties.of(Material.STONE)
													.strength(6f, 6f)
													.requiresCorrectToolForDrops()
													.sound(SoundType.DEEPSLATE)
													.color(MaterialColor.DEEPSLATE)
					)
	);

	public static final RegistryObject<Block> CHIPPED_GRANITE = BLOCKS.register(
					"chipped_granite", () -> new ChippedBlock(
									BlockBehaviour.Properties.of(Material.STONE)
													.strength(3f, 6f)
													.requiresCorrectToolForDrops()
													.sound(SoundType.STONE)
													.color(MaterialColor.DIRT)
					)
	);

	public static final RegistryObject<Block> CHIPPED_DIORITE = BLOCKS.register(
					"chipped_diorite", () -> new ChippedBlock(
									BlockBehaviour.Properties.of(Material.STONE)
													.strength(3f, 6f)
													.requiresCorrectToolForDrops()
													.sound(SoundType.STONE)
													.color(MaterialColor.QUARTZ)
					)
	);

	public static final RegistryObject<Block> CHIPPED_ANDESITE = BLOCKS.register(
					"chipped_andesite", () -> new ChippedBlock(
									BlockBehaviour.Properties.of(Material.STONE)
													.strength(3f, 6f)
													.requiresCorrectToolForDrops()
													.sound(SoundType.STONE)
													.color(MaterialColor.STONE)
					)
	);

	public static final RegistryObject<Block> CHIPPED_TUFF = BLOCKS.register(
					"chipped_tuff", () -> new ChippedBlock(
									BlockBehaviour.Properties.of(Material.STONE)
													.strength(3f, 6f)
													.requiresCorrectToolForDrops()
													.sound(SoundType.STONE)
													.color(MaterialColor.TERRACOTTA_GRAY)
					)
	);

	public static final RegistryObject<Block> CHIPPED_DRIPSTONE = BLOCKS.register(
					"chipped_dripstone_block", () -> new ChippedBlock(
									BlockBehaviour.Properties.of(Material.STONE)
													.strength(3f, 1f)
													.requiresCorrectToolForDrops()
													.sound(SoundType.DRIPSTONE_BLOCK)
													.color(MaterialColor.TERRACOTTA_BROWN)
					)
	);

	public static final RegistryObject<Block> CHIPPED_BLACKSTONE = BLOCKS.register(
					"chipped_blackstone", () -> new ChippedBlock(
									BlockBehaviour.Properties.of(Material.STONE)
													.strength(3f, 6f)
													.requiresCorrectToolForDrops()
													.color(MaterialColor.COLOR_BLACK)
					)
	);

/*
	public static final RegistryObject<Block> CHIPPED_NETHERRACK = BLOCKS.register(
					"chipped_netherrack", () -> new ChippedBlock(
									BlockBehaviour.Properties.of(Material.STONE)
													.strength(0.8f, 0.4f)
													.requiresCorrectToolForDrops()
													.sound(SoundType.NETHERRACK)
													.color(MaterialColor.NETHER)
					)
	);
*/

}
