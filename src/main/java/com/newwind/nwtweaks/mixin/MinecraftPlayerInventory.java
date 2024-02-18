package com.newwind.nwtweaks.mixin;

import com.newwind.nwtweaks.util.DeathUtil;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import nuparu.tinyinv.init.ModItems;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.Iterator;
import java.util.List;

@Mixin(Inventory.class)
public class MinecraftPlayerInventory {

	@Unique
	private int nWTweaks$keepCount = 0;

	@Inject(
					method = "dropAll",
					at = @At(
									value = "INVOKE",
									target = "Lnet/minecraft/world/entity/player/Player;drop(Lnet/minecraft/world/item/ItemStack;ZZ)Lnet/minecraft/world/entity/item/ItemEntity;"),
					locals = LocalCapture.CAPTURE_FAILHARD
	)
	private void beforeDropOnDeath(CallbackInfo ci, @SuppressWarnings("rawtypes") Iterator var1, List<ItemStack> list, int i, ItemStack stack) {
		nWTweaks$keepCount = DeathUtil.applyDestroyAndGetKeep(stack);
		if (stack.getItem() == ModItems.FAKE_ITEM.get())
			stack.shrink(Integer.MAX_VALUE);
	}

	@Redirect(
					method = "dropAll",
					at = @At(
									value = "INVOKE",
									target = "Lnet/minecraft/world/entity/player/Player;drop(Lnet/minecraft/world/item/ItemStack;ZZ)Lnet/minecraft/world/entity/item/ItemEntity;"
					)
	)
	private ItemEntity DropOnDeath(Player instance, ItemStack stack, boolean f7, boolean f8) {
		return instance.drop(stack.copy(), f7, f8);
	}

	@Inject(
					method = "dropAll",
					at = @At(
									value = "INVOKE",
									target = "Ljava/util/List;set(ILjava/lang/Object;)Ljava/lang/Object;"),
					locals = LocalCapture.CAPTURE_FAILHARD
	)
	private void afterDropOnDeath(CallbackInfo ci, @SuppressWarnings("rawtypes") Iterator var1, List<ItemStack> list, int i, ItemStack stack) {
		stack.setCount(nWTweaks$keepCount);
	}

	@Redirect(
					method = "dropAll",
					at = @At(
									value = "INVOKE",
									target = "Ljava/util/List;set(ILjava/lang/Object;)Ljava/lang/Object;")
	)
	private Object cancelOGListSet(List<ItemStack> instance, int i, Object e) {
		return null;
	}

}
