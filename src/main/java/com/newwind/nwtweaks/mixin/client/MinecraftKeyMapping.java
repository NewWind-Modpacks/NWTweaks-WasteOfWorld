package com.newwind.nwtweaks.mixin.client;

import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import nuparu.tinyinv.utils.Utils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(KeyMapping.class)
public class MinecraftKeyMapping {

	@Inject(
					method = "consumeClick",
					at = @At("HEAD"),
					cancellable = true
	)
	private void limitHotbarToMax(CallbackInfoReturnable<Boolean> cir) {
		KeyMapping self = (KeyMapping) (Object) this;

		if (!self.getCategory().equals(KeyMapping.CATEGORY_INVENTORY)
		|| !self.getName().substring(0, self.getName().length() - 1).equals("key.hotbar."))
			return;

		int slots = Utils.getHotbarSlots(Minecraft.getInstance().player);
		String keyName = self.getName();

		int keySlot;
		try {
			keySlot = Integer.parseInt(String.valueOf(keyName.charAt(keyName.length() - 1)));
		} catch (NumberFormatException e) {
			return;
		}

		if (slots < keySlot)
			cir.setReturnValue(false);
	}

}
