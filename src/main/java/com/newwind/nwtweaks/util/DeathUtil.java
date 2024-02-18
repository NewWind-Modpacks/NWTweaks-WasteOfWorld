package com.newwind.nwtweaks.util;

import com.newwind.nwtweaks.NWConfig;
import com.newwind.nwtweaks.NWTweaks;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class DeathUtil {

	private static List<GroupOptions> configGroups;

	public static int applyDestroyAndGetKeep(ItemStack stack) {
		for (GroupOptions options : configGroups) {
			if (options.test(stack)) {
				return options.shrinkDestroyAndGetKeep(stack);
			}
		}
		return stack.getCount();
	}

	public static void reloadCache() {
		ArrayList<GroupOptions> optionsArrayList = new ArrayList<>();
		for (String rawOptions : NWConfig.Common.PLAYER_ITEMS_DEATH_MODS.get()) {
			GroupOptions options = generateOptions(rawOptions);
			if (options == null)
				NWTweaks.LOGGER.error("Bad <Common>[Death][Player Items] config line: \"" + rawOptions + "\"");
			else
				optionsArrayList.add(options);
		}
		configGroups = optionsArrayList.stream().toList();
	}

	@Nullable
	public static GroupOptions generateOptions(String rawOptions) {
		String[] options = rawOptions.split(",");
		if (options.length == 4) {
			String type = options[0];
			String params = options[1];
			Predicate<ItemStack> predicate;
			switch (type) {
				case "any":
					if (params.isEmpty())
						predicate = AnyPredicate.getInstance();
					else
						return null;
					break;

				case "stackable":
					predicate = getStackablePredicate(params);
					break;

				case "item":
					predicate = getItemPredicate(params);
					break;

				case "tag":
					predicate = getTagPredicate(params);
					break;

				case "hasnbt":
					predicate = getHasNBTPredicate(params);
					break;

				default:
					return null;
			}

			try {

				double keepAmount = Double.parseDouble(options[2]);
				double destroyAmount = Double.parseDouble(options[3]);
				if (keepAmount < 0D || destroyAmount < 0D || keepAmount > 1D || destroyAmount > 1D)
					return null;
				return new GroupOptions(keepAmount, destroyAmount, predicate);

			} catch (NumberFormatException | NullPointerException e) {
				return null;
			}
		}

		return null;
	}

	private static ItemPredicate getItemPredicate(String params) {
		Item item = ForgeRegistries.ITEMS.getValue(new ResourceLocation(params));
		if (item == null || item.equals(Items.AIR))
			return null;

		return new ItemPredicate(item);
	}

	private static TagPredicate getTagPredicate(String params) {
		if (params.split(",").length > 1)
			return null;

		TagKey<Item> tag;
		try {
			tag = ItemTags.create(new ResourceLocation(params));
		} catch (Exception e) {
			return null;
		}

		return new TagPredicate(tag);
	}

	private static HasNBTPredicate getHasNBTPredicate(String params) {
		boolean hasNBT;
		if (params.equals("true"))
			hasNBT = true;
		else if (params.equals("false"))
			hasNBT = false;
		else
			return null;

		return new HasNBTPredicate(hasNBT);
	}

	@Nullable
	private static StackablePredicate getStackablePredicate(String params) {
		String[] parts = params.split("-");
		if (parts.length != 2)
			return null;
		try {
			int min = Integer.parseInt(parts[0]);
			int max = Integer.parseInt(parts[1]);
			return new StackablePredicate(min, max);
		} catch (NumberFormatException e) {
			return null;
		}
	}

	public static class GroupOptions {

		private final double keepAmount;
		private final double destroyAmount;
		private final Predicate<ItemStack> predicate;

		public GroupOptions(double keepAmount, double destroyAmount, Predicate<ItemStack> predicate) {
			this.keepAmount = keepAmount;
			this.destroyAmount = 1 - destroyAmount;
			this.predicate = predicate;
		}

		public int shrinkDestroyAndGetKeep(ItemStack stack) {
			final int count = stack.getCount();
			int keep = (int) Math.round(count * keepAmount);
			int drop = count - keep;
			drop = (int) Math.round(drop * destroyAmount);
			stack.setCount(drop);
			return keep;
		}

		public boolean test(ItemStack stack) {
			return predicate.test(stack);
		}
	}

	public static class StackablePredicate implements Predicate<ItemStack> {

		private final int min;
		private final int max;

		public StackablePredicate(int min, int max) {
			this.min = min;
			this.max = max;
		}

		@Override
		public boolean test(ItemStack stack) {
			int stackSize = stack.getMaxStackSize();
			return (min <= 0 || min <= stackSize)
							&& (max <= 0 || max >= stackSize);
		}

	}

	public static class ItemPredicate implements Predicate<ItemStack> {

		private final Item item;

		public ItemPredicate(Item item) {
			this.item = item;
		}

		@Override
		public boolean test(ItemStack stack) {
			return stack.getItem().equals(item);
		}

	}

	public static class TagPredicate implements Predicate<ItemStack> {

		private final TagKey<Item> tag;

		public TagPredicate(TagKey<Item> tag) {
			this.tag = tag;
		}

		@Override
		public boolean test(ItemStack stack) {
			return stack.is(tag);
		}

	}

	public static class HasNBTPredicate implements Predicate<ItemStack> {

		private final boolean hasNBT;

		public HasNBTPredicate(boolean hasNBT) {
			this.hasNBT = hasNBT;
		}

		@Override
		public boolean test(ItemStack stack) {
			return stack.hasTag() == hasNBT;
		}

	}

	public static class AnyPredicate implements Predicate<ItemStack> {
		private static final AnyPredicate predicate = new AnyPredicate();

		public static AnyPredicate getInstance() {
			return predicate;
		}

		private AnyPredicate() {}

		@Override
		public boolean test(ItemStack stack) {
			return true;
		}
	}

}
