package com.newwind.nwtweaks.mixin.client;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.client.gui.overlay.ForgeGui;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import tfar.classicbar.impl.BarOverlayImpl;
import tfar.classicbar.util.Color;

@Mixin(BarOverlayImpl.class)
public class CBBarOverlayImpl {

	@Inject(
					method = "render",
					at = @At(
									value = "INVOKE",
									target = "Ltfar/classicbar/impl/BarOverlayImpl;renderIcon(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/world/entity/player/Player;III)V"
					),
					remap = false
	)
	private void correctColors(ForgeGui gui, PoseStack stack, Player player, int screenWidth, int screenHeight, int vOffset, CallbackInfo ci) {
		Color.reset();
	}

}
