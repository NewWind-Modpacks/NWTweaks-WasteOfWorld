package com.newwind.nwtweaks.mixin;

import com.newwind.nwtweaks.util.DeathUtil;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import top.theillusivec4.curios.api.type.inventory.IDynamicStackHandler;
import top.theillusivec4.curios.common.event.CuriosEventHandler;

@Mixin(value = CuriosEventHandler.class, remap = false)
public abstract class CCuriosEventHandler {

	@Shadow
	private static ItemEntity getDroppedItem(ItemStack droppedItem, LivingEntity livingEntity) {
		return null;
	}

	@Redirect(
					method = "handleDrops",
					at = @At(
									value = "INVOKE",
									target = "Ltop/theillusivec4/curios/common/event/CuriosEventHandler;getDroppedItem(Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/entity/LivingEntity;)Lnet/minecraft/world/entity/item/ItemEntity;"
					)
	)
	private static ItemEntity beforeDrop(ItemStack droppedItem, LivingEntity livingEntity) {
		ItemStack dropCopy = droppedItem.copy();
		droppedItem.setCount(DeathUtil.applyDestroyAndGetKeep(dropCopy));
		return getDroppedItem(dropCopy, livingEntity);
	}

	@Redirect(
					method = "handleDrops",
					at = @At(
									value = "INVOKE",
									target = "Ltop/theillusivec4/curios/api/type/inventory/IDynamicStackHandler;setStackInSlot(ILnet/minecraft/world/item/ItemStack;)V"
					)
	)
	private static void cancelOGListSet(IDynamicStackHandler instance, int i, ItemStack stack) {}

}
