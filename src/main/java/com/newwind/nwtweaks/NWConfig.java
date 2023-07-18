package com.newwind.nwtweaks;

import net.minecraftforge.common.ForgeConfigSpec;

import java.util.Arrays;
import java.util.List;

public class NWConfig {

	public static class Common {
		public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
		public static final ForgeConfigSpec SPEC;

		public static final ForgeConfigSpec.ConfigValue<Integer> CM_TIMEOUT_START;
		public static final ForgeConfigSpec.ConfigValue<Integer> CM_TIMEOUT_END;
		public static final ForgeConfigSpec.ConfigValue<Double> FEATHERS_FREEZE_TEMP;
		public static final ForgeConfigSpec.ConfigValue<List<? extends Integer>> BACKPACK_SIZES;
		public static final ForgeConfigSpec.ConfigValue<List<? extends Integer>> BSP_LEVEL_COSTS;
		public static final ForgeConfigSpec.ConfigValue<Integer> BACKPACK_MIN_POWER;
		public static final ForgeConfigSpec.ConfigValue<Boolean> ENABLE_TOXIC_GAS;
		public static final ForgeConfigSpec.ConfigValue<Integer> CAVE_DAMAGE_HEIGHT_OFFSET;
		public static final ForgeConfigSpec.ConfigValue<Integer> CAVE_DAMAGE_HEIGHT;
		public static final ForgeConfigSpec.ConfigValue<Integer> CAVE_DAMAGE_TIME;
		public static final ForgeConfigSpec.ConfigValue<Double> CAVE_DAMAGE_AMOUNT;
		public static final ForgeConfigSpec.ConfigValue<List<? extends Integer>> MASK_CAPACITY;
		public static final ForgeConfigSpec.ConfigValue<Integer> MASK_REFILL_SPEED;
		public static final ForgeConfigSpec.ConfigValue<Boolean> ENABLE_RADIATION;
		public static final ForgeConfigSpec.ConfigValue<Boolean> ENABLE_BLOCK_BREAK_CHECK;

		static {
			BUILDER.push("Compact Machines");
			CM_TIMEOUT_START = BUILDER.comment("Daily Tick from which to start enforcing compact machines unavailability")
					.defineInRange("Compact Machines Unavailability Start", 12000, 0, 23999);
			CM_TIMEOUT_END = BUILDER.comment("End for above effect. Setting this to the same value as adobe will disable Compact machines at all times.")
					.defineInRange("Compact Machines Unavailability End", 1000, 0, 23999);
			BUILDER.pop();

			FEATHERS_FREEZE_TEMP = BUILDER.comment("Temperature at wich feathers stop regenerating. 0.0 is normal temperature, 1.0 is same as when receiving cold damage")
							.defineInRange("Feather's Freeze Temperature", 0.6, 0.0, 2.0);

			BUILDER.push("Backpack");
			BACKPACK_SIZES = BUILDER.comment("Inventory slots that the backpack gives. First is non enchanted and the rest are an enchantment level each. Non-accumulative.")
							.defineList("Backpack Sizes", Arrays.asList(9, 14, 18), Integer.class::isInstance);
			BSP_LEVEL_COSTS = BUILDER.comment("List of costs for the Deep Pockets levels.")
							.defineList("Deep Pockets Costs", List.of(30), Integer.class::isInstance);
			BACKPACK_MIN_POWER = BUILDER.comment("Minimum power required on a enchanting table for a backpack to be enchantable.")
							.defineInRange("Backpack Minimum Enchanting Table Power", 30, 0, 30);
			BUILDER.pop();

			BUILDER.comment("Underground air is unbreathable.")
							.push("Toxic Gas");
			ENABLE_TOXIC_GAS = BUILDER.comment("Is the feature active?")
							.define("Enabled", true);
			CAVE_DAMAGE_HEIGHT_OFFSET = BUILDER.comment("The toxic gas will start at this additional distance from the surface.")
							.define("Cave Asphyxiation Height Offset", 8);
			CAVE_DAMAGE_HEIGHT = BUILDER.comment("Y-level below which cave asphyxiation will always apply.")
							.define("Cave Asphyxiation Damage Height", 50);
			CAVE_DAMAGE_TIME = BUILDER.comment("Amount of ticks between the damage caused by cave gas asphyxiation.")
							.define("Cave Asphyxiation Damage Interval", 120);
			CAVE_DAMAGE_AMOUNT = BUILDER.comment("Amount of damage caused by cave gas asphyxiation.")
							.define("Cave Asphyxiation Damage Amount", 3.0);

			MASK_CAPACITY = BUILDER.comment("Amount of ticks that the protective mask supplies air for.")
							.defineList("Mask Capacity", List.of(12000, 18000, 24000), Integer.class::isInstance);
			MASK_REFILL_SPEED = BUILDER.comment("Air that the mask gains each tick when refillable.")
							.define("Mask Refill Speed", 3);
			BUILDER.pop();

			BUILDER.comment("Certain conditions will temporally lower your max hp.")
							.push("Radiation");
			ENABLE_RADIATION = BUILDER.comment("Is the feature active?")
							.define("Enabled", true);
			BUILDER.pop();

			ENABLE_BLOCK_BREAK_CHECK = BUILDER.comment("Is the feature active?")
							.define("Block Break Check", true);


			SPEC = BUILDER.build();
		}
	}

	public static class Client {
		public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
		public static final ForgeConfigSpec SPEC;

		public static final ForgeConfigSpec.ConfigValue<Boolean> SNOW_CUTOUT;
		public static final ForgeConfigSpec.ConfigValue<Boolean> BLOCK_SHADER_TOGGLE;

		static {
			SNOW_CUTOUT = BUILDER.comment("Render minecraft:snow in the cutout RenderType layer.")
							.define("Render Snow as Cutout", true);
			BLOCK_SHADER_TOGGLE = BUILDER.comment("Disallows using F4 to toggle vanilla shaders, including winter.json. Ignores if F3 is held")
							.define("Block Shader Toggle", true);

			SPEC = BUILDER.build();
		}

	}
	
}
