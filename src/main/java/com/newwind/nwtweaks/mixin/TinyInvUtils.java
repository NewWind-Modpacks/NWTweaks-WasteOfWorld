package com.newwind.nwtweaks.mixin;

import com.newwind.nwtweaks.client.gui.TetraStats;
import com.newwind.nwtweaks.util.CommonUtils;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import nuparu.tinyinv.config.ServerConfig;
import nuparu.tinyinv.inventory.FakeSlot;
import nuparu.tinyinv.utils.Utils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import se.mickelus.tetra.items.modular.IModularItem;
import se.mickelus.tetra.items.modular.impl.toolbelt.ToolbeltHelper;

@Mixin(value = Utils.class, remap = false)
public abstract class TinyInvUtils {

	/**
	 * @author Kevadroz
	 * @reason Rewrite method to account for a backpack
	 */
	@Overwrite
	public static boolean shouldBeRemoved(int id, Player player, Object container) {
		return CommonUtils.tinyInvShouldBeRemoved(id, player, container, true);
	}

	/**
	 * @author Kevadroz
	 * @reason Rewrite method to account for a backpack
	 */
	@Overwrite
	public static boolean shouldBeRemoved(Slot slot, Player player, AbstractContainerMenu container) {
		return CommonUtils.tinyInvShouldBeRemoved(slot, player, container, true);
	}

	/**
	 * @author Kevadroz
	 * @reason Rewrite method to allow restoration of regular slots
	 */
	@Overwrite
	public static void fixContainer(AbstractContainerMenu container, Player player) {
		for(int i = 0; i < container.slots.size(); ++i) {
			Slot slot = container.slots.get(i);
			if (CommonUtils.tinyInvShouldBeRemoved(slot, player, container, true)) {
				Slot slot2 = new FakeSlot(slot.container, slot.getSlotIndex(), slot.x, slot.y, player);
				slot2.index = i;
				container.slots.set(i, slot2);
			} else if (container.slots.get(i) instanceof FakeSlot) {
				Slot slot2 = new Slot(slot.container, slot.getSlotIndex(), slot.x, slot.y);
				slot2.index = i;
				container.slots.set(i, slot2);
			}
		}
	}

	@Inject(
					method = "getHotbarSlots",
					at = @At("RETURN"),
					cancellable = true
	)
	private static void getHotbarSlots(Player player, CallbackInfoReturnable<Integer> cir) {
		if (player.isCreative() && ServerConfig.excludeCreativeModePlayers.get())
			return;

		ItemStack belt = ToolbeltHelper.findToolbelt(player);
		if (!belt.isEmpty() && belt.getItem() instanceof IModularItem beltItem) {
			int slots = beltItem.getEffectLevel(belt, TetraStats.HOTBAR_SLOTS);
			if (slots > 0)
				cir.setReturnValue(Math.min(cir.getReturnValue() + slots, Math.min(ServerConfig.inventorySlots.get(), 9)));
		}
	}

}
