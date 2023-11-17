package com.newwind.nwtweaks.mixin;

import com.mojang.datafixers.util.Pair;
import com.newwind.nwtweaks.NWConfig;
import com.newwind.nwtweaks.util.MixinExternalFunctions;
import fuzs.thinair.advancements.AirSource;
import fuzs.thinair.capability.AirProtectionCapability;
import fuzs.thinair.helper.AirQualityLevel;
import fuzs.thinair.init.ModRegistry;
import fuzs.thinair.world.item.AirBladderItem;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

import java.util.Optional;

import static com.newwind.nwtweaks.util.CommonUtils.AIR_BLADDER_TAG_OXYGEN_AMOUNT;

@Mixin(AirBladderItem.class)
public abstract class TAAirBladderItem extends Item {


	public TAAirBladderItem(Properties p_41383_) {
		super(p_41383_);
	}

	/**
	 * @author Kevadroz
	 * @reason Implement IsUnderground && improve air system
	 */
	@Overwrite
	public void onUseTick(Level level, LivingEntity livingEntity, ItemStack stack, int remainingUseDuration) {
		Pair<AirQualityLevel, AirSource> o2Pair = MixinExternalFunctions.CaveOxygen.getO2FromEntity(livingEntity);
		AirQualityLevel o2Level = (AirQualityLevel) MixinExternalFunctions.CaveOxygen.soulifyO2Pair(livingEntity, o2Pair).getFirst();
		stack.getOrCreateTag().putString("oxygenLevel", o2Level.toString());
//		ServerPlayer maybeSplayer = null;
//		if (livingEntity instanceof ServerPlayer splayer) {
//			maybeSplayer = splayer;
//		}

		int bladderCapacity = NWConfig.Common.BLADDER_CAPACITY.get();

		int oxygenAmount;
		if (stack.getOrCreateTag().contains(AIR_BLADDER_TAG_OXYGEN_AMOUNT))
			oxygenAmount = stack.getOrCreateTag().getInt(AIR_BLADDER_TAG_OXYGEN_AMOUNT);
		else {
			oxygenAmount = bladderCapacity;
			stack.getOrCreateTag().putInt(AIR_BLADDER_TAG_OXYGEN_AMOUNT, oxygenAmount);
		}

		if (o2Level == AirQualityLevel.GREEN) {
			if (oxygenAmount < bladderCapacity) {
				oxygenAmount += bladderCapacity / NWConfig.Common.BLADDER_FILL_TIME.get();
				stack.getOrCreateTag().putInt(AIR_BLADDER_TAG_OXYGEN_AMOUNT, Math.min(bladderCapacity, oxygenAmount));
			}
		} else if (oxygenAmount > 0) {
			int breathSpeed = NWConfig.Common.BLADDER_BREATH_SPEED.get();
			for (int i = 0; i < breathSpeed; ++i) {
				if (oxygenAmount <= 0
								|| livingEntity.getAirSupply() >= livingEntity.getMaxAirSupply()
								|| stack.getCount() < 1) {
					break;
				}

				oxygenAmount--;
				stack.hurtAndBreak(1, livingEntity, (entity) -> {
					entity.broadcastBreakEvent(livingEntity.getUsedItemHand());
					Optional<AirProtectionCapability> maybeCap = ModRegistry.AIR_PROTECTION_CAPABILITY.maybeGet(livingEntity);
					maybeCap.ifPresent((cap) -> {
						cap.setProtected(false);
					});
				});
				livingEntity.setAirSupply(livingEntity.getAirSupply() + 1);
			}
			stack.getOrCreateTag().putInt(AIR_BLADDER_TAG_OXYGEN_AMOUNT, Math.max(0, oxygenAmount));
		}

	}

	/**
	 * @author Kevadroz
	 * @reason Implement better air system
	 */
	@Overwrite
	public UseAnim getUseAnimation(ItemStack pStack) {
		UseAnim out = UseAnim.NONE;
		if (pStack.hasTag()
						&& pStack.getOrCreateTag().contains(AIR_BLADDER_TAG_OXYGEN_AMOUNT)
						&& pStack.getOrCreateTag().getInt(AIR_BLADDER_TAG_OXYGEN_AMOUNT) < NWConfig.Common.BLADDER_CAPACITY.get()) {
			CompoundTag tag = pStack.getTag();
			if (tag.contains("oxygenLevel", 8)) {
				String o2LevelStr = tag.getString("oxygenLevel");

				try {
					AirQualityLevel o2Level = AirQualityLevel.valueOf(o2LevelStr);
					out = o2Level == AirQualityLevel.GREEN ? UseAnim.BOW : UseAnim.DRINK;
				} catch (IllegalArgumentException var6) {
				}
			}
		}

		return out;
	}

	@Override
	public int getMaxDamage(ItemStack stack) {
		return NWConfig.Common.BLADDER_DURABILITY.get();
	}
}
