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

// TODO: implement chipped stone
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

}
