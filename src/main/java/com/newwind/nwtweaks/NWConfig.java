package com.newwind.nwtweaks;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Arrays;
import java.util.List;

public class NWConfig {

	public static class Common {
		public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
		public static final ForgeConfigSpec SPEC;

		public static final ForgeConfigSpec.IntValue CM_TIMEOUT_START;
		public static final ForgeConfigSpec.IntValue CM_TIMEOUT_END;
		public static final ForgeConfigSpec.DoubleValue FEATHERS_STALL_TEMP;
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
		public static final ForgeConfigSpec.DoubleValue HELD_LIGHT_SANITY_MULTIPLIER;
		public static final ForgeConfigSpec.IntValue SANITY_THIRST_THRESHOLD;
		public static final ForgeConfigSpec.DoubleValue TEMPERATURE_SANITY_THRESHOLD;
		public static final ForgeConfigSpec.DoubleValue EXTREME_TEMPERATURE_SANITY_MOD;
		public static final ForgeConfigSpec.DoubleValue CALMING_TEMPERATURE_SANITY_MOD;
		public static final ForgeConfigSpec.IntValue PILL_POOL_MIN_SIZE;
		public static final ForgeConfigSpec.IntValue PILL_POOL_MAX_SIZE;
		public static final ForgeConfigSpec.DoubleValue DWELLER_MIN_STARE_SANITY;
		public static final ForgeConfigSpec.DoubleValue DWELLER_STARE_SANITY_MOD;
		public static final ForgeConfigSpec.DoubleValue RED_DWELLER_STARE_SANITY_MOD;
		public static final ForgeConfigSpec.IntValue RED_DWELLER_SPAWN_TIME;
		public static final ForgeConfigSpec.DoubleValue STONE_MINE_SPEED_MULTIPLIER;
		public static final ForgeConfigSpec.IntValue CONTAINER_LONE_TICKS;
		public static final ForgeConfigSpec.DoubleValue CONTAINER_COMPANY_RANGE;
		public static final ForgeConfigSpec.BooleanValue CONTAINER_DROP_INPUT_INSTANTLY;
		public static final ForgeConfigSpec.DoubleValue NUTRITION_MIN_DIFFERENCE;
		public static final ForgeConfigSpec.DoubleValue NUTRITION_MAX_DIFFERENCE;
		public static final ForgeConfigSpec.LongValue PLAYER_LOOT_BAG_DESPAWN_TIME;

		static {


			BUILDER.push("Compact Machines");
			CM_TIMEOUT_START = BUILDER.comment("Daily Tick from which to start enforcing compact machines unavailability")
							.defineInRange("Compact Machines Unavailability Start", 12000, 0, 23999);
			CM_TIMEOUT_END = BUILDER.comment("End for above effect. Setting this to the same value as adobe will disable Compact machines at all times.")
							.defineInRange("Compact Machines Unavailability End", 1000, 0, 23999);
			BUILDER.pop();

			FEATHERS_STALL_TEMP = BUILDER.comment("Temperature at which feathers stop regenerating. 0.0 is normal temperature, 1.0 is same as when receiving damage.")
							.defineInRange("Feather's Freeze Temperature", 0.6, 0.0, 2.0);

			BUILDER.push("Nutrition");
			NUTRITION_MIN_DIFFERENCE = BUILDER.comment("Minimal difference to the average (not including non beneficial groups) for a food's nutrition to be affected.")
							.defineInRange("Nutrition Minimal Difference Debuff", 0.3, 0.0, 1.0);
			NUTRITION_MAX_DIFFERENCE = BUILDER.comment("Difference at which the food will no longer replenish hunger.")
							.defineInRange("Nutrition Maximal Difference Debuff", 0.7, 0.0, 1.0);
			BUILDER.pop();

			BUILDER.push("Backpack");
			BACKPACK_SIZES = BUILDER.comment("Inventory slots that the backpack gives. First is non enchanted and the rest are an enchantment level each. Non-accumulative.")
							.defineList("Backpack Sizes", Arrays.asList(9, 14, 18), Integer.class::isInstance);
			BSP_LEVEL_COSTS = BUILDER.comment("List of costs for the Deep Pockets levels.")
							.defineList("Deep Pockets Costs", List.of(30), Integer.class::isInstance);
			BACKPACK_MIN_POWER = BUILDER.comment("Minimum power required on a enchanting table for a backpack to be enchantable.")
							.defineInRange("Minimum Enchanting Table Power", 30, 0, 30);
			BUILDER.pop();

			BUILDER.push("Container Limiting");
			CONTAINER_LONE_TICKS = BUILDER.comment("Time before an inactive container drops it's contents after being left alone.")
							.defineInRange("Container Lone Ticks", 24000, -1, Integer.MAX_VALUE);
			CONTAINER_COMPANY_RANGE = BUILDER.comment("How far a container has to be from any player to be considered \"alone\".")
							.defineInRange("Container Company Range", 64D, 0D, 1024D);
			CONTAINER_DROP_INPUT_INSTANTLY = BUILDER.comment("Drop a container's input the moment it's inactive or when the output drops (true for the former).")
							.define("Container Drop Input Instantly", true);
			BUILDER.pop();

			BUILDER.push("Loot Bags");
			PLAYER_LOOT_BAG_DESPAWN_TIME = BUILDER.comment("The amount of time that it takes for a (player) loot bag to despawn")
							.defineInRange("Player Loot Bag Despawn Time", 6000L, 0L, Long.MAX_VALUE);
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

			STONE_MINE_SPEED_MULTIPLIER = BUILDER.comment("How much time does it cost to mine raw stone materials.")
							.defineInRange("Raw Stone Mine Speed Multiplier", 1f, 0f, Double.MAX_VALUE);

			BUILDER.push("Cave Dweller");
			DWELLER_MIN_STARE_SANITY = BUILDER.comment("How much sanity you must have to stop a dweller from move by looking at it.")
							.defineInRange("Minimum Sanity To Stare", 0.05D, 0D, 1D);
			DWELLER_STARE_SANITY_MOD = BUILDER.comment("How much sanity is gained when staring at a dweller.")
							.defineInRange("Sanity Modifier When Staring", 0D, -100D, 100D);
			RED_DWELLER_STARE_SANITY_MOD = BUILDER.comment("How much sanity is gained when staring at a dweller.")
							.defineInRange("Sanity Modifier When Staring At Red", 0D, -100D, 100D);
			RED_DWELLER_SPAWN_TIME = BUILDER.comment("From how much time underground cave dwellers should be fatal?")
							.defineInRange("Red Dweller Spawn Time", 24000, 0, Integer.MAX_VALUE);
			BUILDER.pop();

			BUILDER.comment("Modifications to ThinAir's Air Bladder")
							.push("Air Bladder");
			BLADDER_CAPACITY = BUILDER.comment("The capacity of the bladder in ticks.")
							.define("Capacity", 6000);
			BLADDER_DURABILITY = BUILDER.comment("The durability of the bladder in ticks.")
							.define("Durability", 144000);
			BLADDER_BREATH_SPEED = BUILDER.comment("The speed in air ticks at which the bladder fills the user's air.")
							.define("Breath Speed", 4);
			BLADDER_FILL_TIME = BUILDER.comment("The amount of ticks the bladder takes to fill up.")
							.define("Fill Time", 200);
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

			BUILDER.push("Sanity");
			HELD_LIGHT_SANITY_MULTIPLIER = BUILDER.comment("Holding a valid light source multiplies the amount of sanity gained when standing in darkness to this percentage.")
							.defineInRange("Held Light Multiplier", 0.2D, 0.0D, 100.0D);
			SANITY_THIRST_THRESHOLD = BUILDER.comment("Thirst points below which sanity passively changes.")
							.defineInRange("Thirst Threshold", 6, 0, 20);
			TEMPERATURE_SANITY_THRESHOLD = BUILDER.comment("The temperature at which sanity starts to be affected.")
							.defineInRange("Temperature Threshold", 0.5D, 0.0D, 2.0D);
			EXTREME_TEMPERATURE_SANITY_MOD = BUILDER.comment("The amount of sanity gained per tick at fatal temperatures.")
							.defineInRange("Extreme Temperature Modifier", -0.4D, -100.0D, 100.0D);
			CALMING_TEMPERATURE_SANITY_MOD = BUILDER.comment("The amount of sanity gained per tick while the temperature is heading toward a neutral zero.")
							.defineInRange("Calming Temperature Modifier", 0D, -100.0D, 100.0D);
			BUILDER.pop();

			PILL_POOL_MIN_SIZE = BUILDER.comment("How many slots a pill pool will have, in addition to the minimum.")
							.defineInRange("Pill Pool Minimum Additional Slots", 13, 0, Integer.MAX_VALUE);
			PILL_POOL_MAX_SIZE = BUILDER.comment("How many slots a pill pool can have, in addition to the minimum.")
							.defineInRange("Pill Pool Maximum Additional Slots", 17, 0, Integer.MAX_VALUE);


			SPEC = BUILDER.build();
		}
	}

	public static class Client {
		public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
		public static final ForgeConfigSpec SPEC;

		public static final ForgeConfigSpec.ConfigValue<Boolean> SNOW_CUTOUT;
		public static final ForgeConfigSpec.ConfigValue<Boolean> BLOCK_SHADER_TOGGLE;
		public static final ForgeConfigSpec.DoubleValue REGULAR_FOG_START;
		public static final ForgeConfigSpec.DoubleValue REGULAR_FOG_END;
		public static final ForgeConfigSpec.DoubleValue MAX_FOG_END;
		public static final ForgeConfigSpec.DoubleValue UNDERGROUND_FOG_START;
		public static final ForgeConfigSpec.DoubleValue UNDERGROUND_FOG_END;
		public static final ForgeConfigSpec.DoubleValue DESATURATION_FACTOR;
		public static final ForgeConfigSpec.DoubleValue BRIGHTNESS_FACTOR;
		public static final ForgeConfigSpec.ConfigValue<Boolean> RENDER_THIRST_TEXT;
		public static final ForgeConfigSpec.ConfigValue<Boolean> RENDER_THIRST_EXHAUSTION;
		public static final ForgeConfigSpec.ConfigValue<Boolean> RENDER_SANITY_TEXT;
		public static final ForgeConfigSpec.DoubleValue ARROW_PASSIVE_SCALE;
		public static final ForgeConfigSpec.ConfigValue<List<? extends String>> PARCOOL_NOT_ANIMATE;

		static {
			SNOW_CUTOUT = BUILDER.comment("Render minecraft:snow in the cutout RenderType layer.")
							.define("Render Snow as Cutout", true);
			BLOCK_SHADER_TOGGLE = BUILDER.comment("Disallows using F4 to toggle vanilla shaders, including winter.json. Ignores if F3 is held.")
							.define("Block Shader Toggle", true);
			PARCOOL_NOT_ANIMATE = BUILDER.comment("Items that stop the first-person animations from parcool when held.\nIf \"minecraft:air\" is used, anything that isn't an empty hand will cancel the animation")
							.defineList("Parcool Animations Inhibitors", List.of(
											"minecraft:air"
							), i -> i instanceof String s && ForgeRegistries.ITEMS.getValue(new ResourceLocation(s)) != null);

			BUILDER.push("Fog");
			REGULAR_FOG_START = BUILDER.comment("Regular fog start.")
							.defineInRange("Regular Fog Start", 96D, -512D, 512D);
			REGULAR_FOG_END = BUILDER.comment("Regular fog end.")
							.defineInRange("Regular Fog End", 128D, -512D, 512D);
			MAX_FOG_END = BUILDER.comment("Fog end when night vision is active, also controls max render distance in blocks.")
							.defineInRange("Max Fog End", 128D, -512D, 512D);
			UNDERGROUND_FOG_START = BUILDER.comment("Underground dark fog start.")
							.defineInRange("Underground Fog Start", 0D, -512D, 512D);
			UNDERGROUND_FOG_END = BUILDER.comment("Underground dark fog end.")
							.defineInRange("Underground Fog End", 32D, -512D, 512D);
			BUILDER.pop();

			BUILDER.push("Video Filter");
			DESATURATION_FACTOR = BUILDER.comment("Desaturate the world by this amount.")
							.defineInRange("Desaturation Factor", 0.6D, 0D, 1D);
			BRIGHTNESS_FACTOR = BUILDER.comment("Enlighten the world by this amount.")
							.defineInRange("Brightness", 0.3D, 0D, 1D);
			BUILDER.pop();

			BUILDER.push("HUD");
			RENDER_THIRST_TEXT = BUILDER.comment("Render the thirst value next to the bar.")
							.define("Render Thirst Text", true);
			RENDER_THIRST_EXHAUSTION = BUILDER.comment("Render the thirst exhaustion value.")
							.define("Render Thirst Exhaustion", true);
			RENDER_SANITY_TEXT = BUILDER.comment("Render the sanity value next to the bar.")
							.define("Render Sanity Text", true);
			ARROW_PASSIVE_SCALE = BUILDER.comment("Render the sanity value next to the bar.")
							.defineInRange("Arrow Passsive Scale", 1.0D, Double.MIN_NORMAL, Double.MAX_VALUE);
			BUILDER.pop();

			SPEC = BUILDER.build();
		}

	}

}
