package com.newwind.nwtweaks.util;

import com.github.wolfiewaffle.hardcore_torches.init.ItemInit;
import com.mojang.datafixers.util.Pair;
import com.newwind.nwtweaks.client.NWClient;
import fuzs.thinair.advancements.AirSource;
import fuzs.thinair.helper.AirHelper;
import fuzs.thinair.helper.AirQualityLevel;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import top.theillusivec4.curios.api.CuriosApi;

import java.util.function.Predicate;

public class MixinExternalFunctions {

	public static class CaveLantern {

		private static final Item[] validLightItems = new Item[]{
						Items.TORCH,
						Items.SOUL_TORCH,
						Items.LANTERN,
						Items.SOUL_LANTERN,
						ItemInit.LIT_TORCH.get(),
						ItemInit.LIT_LANTERN.get(),
						ItemInit.LIT_SOUL_LANTERN.get()
		};

		private static final Predicate<ItemStack> validLightSources = new Predicate<ItemStack>() {
			@Override
			public boolean test(ItemStack itemStack) {
				return isValidLightSource(itemStack.getItem());
			}

		};

		private static boolean isValidLightSource(Item item) {
			for (Item validLightItem : validLightItems) {
				if (item.equals(validLightItem))
					return true;
			}
			return false;
		}

		public static boolean hasValidLightSource(Player player) {
			return isValidLightSource(player.getMainHandItem().getItem())
							|| isValidLightSource(player.getOffhandItem().getItem())
							|| CuriosApi.getCuriosHelper().findFirstCurio(player, validLightSources).isPresent();
		}

	}

	public static class CaveOxygen {

		private static final Item[] validSoulItems = new Item[]{
						Items.SOUL_TORCH,
						Items.SOUL_LANTERN,
						ItemInit.LIT_SOUL_LANTERN.get()
		};
		private static final Predicate<ItemStack> validSoulSources = new Predicate<ItemStack>() {
			@Override
			public boolean test(ItemStack itemStack) {
				return isValidSoulSource(itemStack.getItem());
			}

		};

		public static Pair<AirQualityLevel, AirSource> getO2FromEntity(Entity entity) {
			Level world = entity.getLevel();
			Vec3 position = entity.getEyePosition();
			Pair<AirQualityLevel, AirSource> o2 = AirHelper.getO2LevelFromLocation(position, world);
			if ((o2.getSecond() == AirSource.DIMENSION || o2.getSecond() == AirSource.LAVA)
							&& !CommonUtils.isUnderground(entity)) {
				o2 = new Pair<>(AirQualityLevel.GREEN, AirSource.DIMENSION);
			}

			return o2;
		}

		public static Pair<AirQualityLevel, AirSource> getO2FromLantern(Entity entity) {
			Level world = entity.getLevel();
			Vec3 position = entity.getEyePosition();
			boolean isUnderground;
			if (entity instanceof LocalPlayer)
				isUnderground = NWClient.isUnderground;
			else
				isUnderground = CommonUtils.isUnderground(world, new BlockPos(position));

			Pair<AirQualityLevel, AirSource> o2 = AirHelper.getO2LevelFromLocation(position, world);
			if ((o2.getSecond() == AirSource.DIMENSION || o2.getSecond() == AirSource.LAVA)
							&& !isUnderground) {
				o2 = new Pair<>(AirQualityLevel.GREEN, AirSource.DIMENSION);
			}

			return o2;
		}

		private static boolean isValidSoulSource(Item item) {
			for (Item validLightItem : validSoulItems) {
				if (item.equals(validLightItem))
					return true;
			}
			return false;
		}

		public static boolean hasValidSoulSource(Player player) {
			return isValidSoulSource(player.getMainHandItem().getItem())
							|| isValidSoulSource(player.getOffhandItem().getItem())
							|| CuriosApi.getCuriosHelper().findFirstCurio(player, validSoulSources).isPresent();
		}

		public static Pair<AirQualityLevel, AirSource> soulifyO2Pair(LivingEntity entity, Pair<AirQualityLevel, AirSource> o2Pair) {

			boolean hasSoulSource = false;
			if (entity instanceof Player player)
				hasSoulSource = MixinExternalFunctions.CaveOxygen.hasValidSoulSource(player);

			if (hasSoulSource &&
							(o2Pair.getFirst() == AirQualityLevel.YELLOW || o2Pair.getFirst() == AirQualityLevel.RED)
							&& o2Pair.getSecond() == AirSource.DIMENSION)
				return new Pair<>(AirQualityLevel.BLUE, AirSource.SOUL);
			else
				return o2Pair;
		}
	}

}
