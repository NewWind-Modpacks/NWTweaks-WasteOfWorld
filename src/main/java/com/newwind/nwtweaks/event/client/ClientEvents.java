package com.newwind.nwtweaks.event.client;

import com.newwind.nwtweaks.NWTweaks;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterShadersEvent;
import net.minecraftforge.event.TickEvent.RenderTickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;

@Mod.EventBusSubscriber(modid = NWTweaks.MODID, value = Dist.CLIENT)
public class ClientEvents {

	private static boolean isShaderApplied = false;

	@SubscribeEvent
	public static void onRenderTick(RenderTickEvent event) {
		Minecraft mc = Minecraft.getInstance();
		if (mc.level != null) {
			if (!isShaderApplied) {
				GameRenderer renderer = mc.gameRenderer;
				renderer.loadEffect(new ResourceLocation(NWTweaks.MODID, "shaders/post/winter.json"));
				isShaderApplied = true;
			}
		} else
			refreshShader();
	};

	public static void refreshShader() {
		isShaderApplied = false;
	}

}
