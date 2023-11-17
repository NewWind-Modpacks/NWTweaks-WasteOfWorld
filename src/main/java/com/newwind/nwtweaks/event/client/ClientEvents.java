package com.newwind.nwtweaks.event.client;

import com.mojang.blaze3d.systems.RenderSystem;
import com.newwind.nwtweaks.NWConfig;
import com.newwind.nwtweaks.NWTweaks;
import com.newwind.nwtweaks.client.NWClient;
import com.newwind.nwtweaks.client.gui.SanityClassicBar;
import com.newwind.nwtweaks.client.world.item.renderer.AirBladderDecorator;
import com.newwind.nwtweaks.networking.ModMessages;
import com.newwind.nwtweaks.util.CommonUtils;
import fuzs.thinair.init.ModRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.FogRenderer;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.item.Items;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterGuiOverlaysEvent;
import net.minecraftforge.client.event.RegisterItemDecorationsEvent;
import net.minecraftforge.client.event.ViewportEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import tfar.classicbar.EventHandler;

@Mod.EventBusSubscriber(modid = NWTweaks.MODID, value = Dist.CLIENT)
public class ClientEvents {

	private static double nameDrawHeight = 0.0D;

//	@SubscribeEvent
//	public static void onRenderTick(RenderTickEvent event) {
//		Minecraft mc = Minecraft.getInstance();
//		if (mc.level != null) {
//			if (!isShaderApplied) {
//				GameRenderer renderer = mc.gameRenderer;
//				renderer.loadEffect(new ResourceLocation(NWTweaks.MODID, "shaders/post/winter.json"));
//				isShaderApplied = true;
//			}
//		} else
//			refreshShader();
//	}

//	private static void updateNameDrawHeight() {
//		ForgeGui gui = (ForgeGui) Minecraft.getInstance().gui;
//		int height = Math.max(gui.leftHeight, gui.rightHeight);
//		nameDrawHeight = Math.min(0.0, -(height - 59.0));
//	}
//
//	@SubscribeEvent(
//					priority = EventPriority.LOWEST
//	)
//	public static void preRenderGuiOverlay(RenderGuiOverlayEvent.Pre event) {
//		if (event.getOverlay() == VanillaGuiOverlay.ITEM_NAME.type()) {
//			updateNameDrawHeight();
//			event.getPoseStack().translate(0.0, nameDrawHeight, 0.0);
//		}
//	}
//
//	@SubscribeEvent(
//					priority = EventPriority.LOWEST
//	)
//	public static void postRenderGuiOverlay(RenderGuiOverlayEvent.Post event) {
//
//		if (event.getOverlay() == VanillaGuiOverlay.ITEM_NAME.type()) {
//			event.getPoseStack().translate(0.0, -nameDrawHeight, 0.0);
//		}
//	}

/*
	@SubscribeEvent
	public static void applyCaveFogColor(ViewportEvent.ComputeFogColor event) {
		if (CommonUtils.isInCave(Minecraft.getInstance().player)) {
			event.setRed(0);
			event.setGreen(0);
			event.setBlue(0);
		}
	}
*/

//	public static void refreshShader() {
//		isShaderApplied = false;
//	}

	@SubscribeEvent
	public static void applyCaveFogDistance(ViewportEvent.RenderFog event) {
		if (NWClient.isUnderground)
			NWClient.undergroundTransitionValue = Math.min(1.0F, NWClient.undergroundTransitionValue + Minecraft.getInstance().getDeltaFrameTime() / 100F);
		else
			NWClient.undergroundTransitionValue = Math.max(0.0F, NWClient.undergroundTransitionValue - Minecraft.getInstance().getDeltaFrameTime() / 30F);

		if (NWClient.undergroundTransitionValue > 0.0F) {
			float fogStart = event.getMode() == FogRenderer.FogMode.FOG_SKY ? 0.0F : (float) NWConfig.Client.UNDERGROUND_FOG_START.get().floatValue();
			float fogEnd = NWConfig.Client.UNDERGROUND_FOG_END.get().floatValue();
			float[] fogOgColors = RenderSystem.getShaderFogColor();

			float transitionValue = NWClient.undergroundTransitionValue;
			RenderSystem.setShaderFogStart(Mth.lerp(transitionValue, RenderSystem.getShaderFogStart(), fogStart));
			RenderSystem.setShaderFogEnd(Mth.lerp(transitionValue, RenderSystem.getShaderFogEnd(), fogEnd));
			RenderSystem.setShaderFogColor(
							Mth.lerp(transitionValue, fogOgColors[0], 0.0F),
							Mth.lerp(transitionValue, fogOgColors[1], 0.0F),
							Mth.lerp(transitionValue, fogOgColors[2], 0.0F)
			);
		}
	}

	@Mod.EventBusSubscriber(modid = NWTweaks.MODID, value = Dist.CLIENT, bus = Bus.MOD)
	public static class ModBus {

		@SubscribeEvent
		public static void onSetup(final FMLClientSetupEvent event) {
			ModMessages.register();
		}

		// Based off https://github.com/LukeGrahamLandry/inclusive-enchanting-mod/blob/forge-1.19/src/main/java/io/github/lukegrahamlandry/inclusiveenchanting/events/QuickChargeHander.java
		@SubscribeEvent
		public static void onClientSetup(FMLClientSetupEvent event) {
			event.enqueueWork(() -> {
				ItemProperties.register(Items.BOW, new ResourceLocation("pull"), (stack, level, entity, seed) -> {
					if (entity == null || entity.getUseItem() != stack) return 0.0F;
					float maxCharge = 20.0F;
					//  var reloadSpeedTag = NWConfig.Common.RELOAD_SPEED_TAG.get();
					//  var tags = entity.getTags();
					if (stack.getTag() != null) {
						var hasTag = stack.getTag().getBoolean(NWConfig.Common.RELOAD_SPEED_TAG.get());//tags.contains(reloadSpeedTag);
						maxCharge *= hasTag ? (float) (1.0F - NWConfig.Common.RELOAD_SPEED_MULTIPLIER.get()) : 1.0F;
					}
					return (float) (stack.getUseDuration() - entity.getUseItemRemainingTicks()) / maxCharge;
				});
				ItemProperties.register(ModRegistry.AIR_BLADDER_ITEM.get(), new ResourceLocation(NWTweaks.MODID,"oxygen_amount"), (stack, level, entity, seed) -> {
					CompoundTag nbt = stack.getOrCreateTag();
					int oxygenAmount;
					if (nbt.contains(CommonUtils.AIR_BLADDER_TAG_OXYGEN_AMOUNT))
						oxygenAmount = nbt.getInt(CommonUtils.AIR_BLADDER_TAG_OXYGEN_AMOUNT);
					else
						oxygenAmount = NWConfig.Common.BLADDER_CAPACITY.get();

					return (float) oxygenAmount / (float) NWConfig.Common.BLADDER_CAPACITY.get();
				});
			});
		}

		@SubscribeEvent
		public static void registerItemDecorations(RegisterItemDecorationsEvent event) {
			event.register(ModRegistry.AIR_BLADDER_ITEM.get(), new AirBladderDecorator());
		}

		@SubscribeEvent
		public static void registerGuiOverlays(RegisterGuiOverlaysEvent event) {
			EventHandler.register(new SanityClassicBar());
		}

//		@SubscribeEvent
//		public static void onModRegistartion(FMLConstructModEvent event) {
//
//		}

	}

}
