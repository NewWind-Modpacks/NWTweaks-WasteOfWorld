package com.newwind.nwtweaks;

import net.minecraftforge.common.ForgeConfigSpec;

import java.util.Arrays;
import java.util.List;

public class NWConfig {

	public static class Common {
		public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
		public static final ForgeConfigSpec SPEC;

		public static final ForgeConfigSpec.IntValue CM_TIMEOUT_START;
		public static final ForgeConfigSpec.IntValue CM_TIMEOUT_END;
		public static final ForgeConfigSpec.DoubleValue FEATHERS_FREEZE_TEMP;
		public static final ForgeConfigSpec.ConfigValue<List<? extends Integer>> BACKPACK_SIZES;
		public static final ForgeConfigSpec.ConfigValue<List<? extends Integer>> BSP_LEVEL_COSTS;
		public static final ForgeConfigSpec.IntValue BACKPACK_MIN_POWER;
		public static final ForgeConfigSpec.ConfigValue<Integer> UNDERGROUND_HEIGHT_OFFSET;
		public static final ForgeConfigSpec.ConfigValue<Integer> UNDERGROUND_INNER_HEIGHT;
		public static final ForgeConfigSpec.ConfigValue<Integer> UNDERGROUND_OUTER_HEIGHT;
		public static final ForgeConfigSpec.ConfigValue<Integer> UNDERGROUND_CHECK_INTERVAL;
		public static final ForgeConfigSpec.ConfigValue<Integer> BLADDER_CAPACITY;
		public static final ForgeConfigSpec.ConfigValue<Integer> BLADDER_DURABILITY;
		public static final ForgeConfigSpec.ConfigValue<Integer> BLADDER_BREATH_SPEED;
		public static final ForgeConfigSpec.ConfigValue<Integer> BLADDER_FILL_TIME;
		public static final ForgeConfigSpec.BooleanValue ENABLE_RADIATION;
		public static final ForgeConfigSpec.BooleanValue ENABLE_BLOCK_BREAK_CHECK;
		public static final ForgeConfigSpec.ConfigValue<String> RELOAD_SPEED_TAG;
		public static final ForgeConfigSpec.DoubleValue RELOAD_SPEED_MULTIPLIER;

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

			BUILDER.comment("Underground air is unbreathable and the darkness consumes.")
							.push("Underground Effects");
			UNDERGROUND_HEIGHT_OFFSET = BUILDER.comment("The underground will start at this additional distance from the surface.")
							.define("Underground Height Offset", 8);
			UNDERGROUND_INNER_HEIGHT = BUILDER.comment("Y-level below which underground effects will always apply.")
							.define("Underground Inner Height", 25);
			UNDERGROUND_OUTER_HEIGHT = BUILDER.comment("Y-level below which underground effects can apply.")
							.define("Underground Outer Height", 100);
			UNDERGROUND_CHECK_INTERVAL = BUILDER.comment("Ticks between checks of being underground.")
							.define("Underground Check Interval", 100);
			BUILDER.pop();
			BUILDER.comment("Modifications to ThinAir's Air Bladder")
							.push("Air Bladder");
			BLADDER_CAPACITY = BUILDER.comment("The capacity of the bladder in ticks.")
							.define("Bladder Capacity", 6000);
			BLADDER_DURABILITY = BUILDER.comment("The durability of the bladder in ticks.")
							.define("Bladder Durability", 144000);
			BLADDER_BREATH_SPEED = BUILDER.comment("The speed in air ticks at which the bladder fills the user's air.")
							.define("Bladder Breath Speed", 4);
			BLADDER_FILL_TIME = BUILDER.comment("The amount of ticks the bladder takes to fill up.")
							.define("Bladder Fill Time", 200);
			BUILDER.pop();

			BUILDER.comment("Certain conditions will temporally lower your max hp.")
							.push("Radiation");
			ENABLE_RADIATION = BUILDER.comment("Is the feature active?")
							.define("Enabled", true);
			BUILDER.pop();

			BUILDER.push("Skills");
			BUILDER.comment("The tags an entity has to have for the effects to apply.")
							.push("Tags");
			RELOAD_SPEED_TAG = BUILDER.comment("Ranged weapons load ammo faster.")
							.define("Reload Speed", "_nw-ps_reload_speed");
			BUILDER.pop();
			RELOAD_SPEED_MULTIPLIER = BUILDER.comment("The percentage of the time that's cut from bows and crossbows arrow loading time and guns reloading time.")
							.defineInRange("Reload Speed", 0.3D, 0.0D, 1.0D);
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
		public static final ForgeConfigSpec.DoubleValue UNDERGROUND_FOG_START;
		public static final ForgeConfigSpec.DoubleValue UNDERGROUND_FOG_END;
		public static final ForgeConfigSpec.DoubleValue DESATURATION_FACTOR;
		public static final ForgeConfigSpec.DoubleValue BRIGHTNESS_FACTOR;
		public static final ForgeConfigSpec.ConfigValue<Boolean> RENDER_THIRST_TEXT;
		public static final ForgeConfigSpec.ConfigValue<Boolean> RENDER_THIRST_EXHAUSTION;
		public static final ForgeConfigSpec.ConfigValue<Boolean> RENDER_SANITY_TEXT;

		static {
			SNOW_CUTOUT = BUILDER.comment("Render minecraft:snow in the cutout RenderType layer.")
							.define("Render Snow as Cutout", true);
			BLOCK_SHADER_TOGGLE = BUILDER.comment("Disallows using F4 to toggle vanilla shaders, including winter.json. Ignores if F3 is held.")
							.define("Block Shader Toggle", true);
			UNDERGROUND_FOG_START = BUILDER.comment("Underground dark fog start.")
							.defineInRange("Underground Fog Start", 0D ,0D, 512D);
			UNDERGROUND_FOG_END = BUILDER.comment("Underground dark fog end.")
							.defineInRange("Underground Fog End", 32D, 1D, 512D);
			DESATURATION_FACTOR = BUILDER.comment("Desaturate the world by this amount.")
							.defineInRange("Desaturation Factor", 0.6D, 0D, 1D);
			BRIGHTNESS_FACTOR = BUILDER.comment("Enlighten the world by this amount.")
							.defineInRange("Brightness", 0.3D, 0D, 1D);
			RENDER_THIRST_TEXT = BUILDER.comment("Render the thirst value next to the bar.")
							.define("Render Thirst Text", true);
			RENDER_THIRST_EXHAUSTION = BUILDER.comment("Render the thirst exhaustion value.")
							.define("Render Thirst Exhaustion", true);
			RENDER_SANITY_TEXT = BUILDER.comment("Render the sanity value next to the bar.")
							.define("Render Sanity Text", true);

			SPEC = BUILDER.build();
		}

	}
	
}
