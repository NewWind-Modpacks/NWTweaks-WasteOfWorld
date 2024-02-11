package com.newwind.nwtweaks.event.client;

import com.mojang.blaze3d.systems.RenderSystem;
import com.newwind.nwtweaks.NWConfig;
import com.newwind.nwtweaks.NWTweaks;
import com.newwind.nwtweaks.client.NWClient;
import com.newwind.nwtweaks.client.gui.SanityClassicBar;
import com.newwind.nwtweaks.client.gui.TetraStats;
import com.newwind.nwtweaks.client.world.entity.model.LootBagModel;
import com.newwind.nwtweaks.client.world.entity.renderer.LootBagRenderer;
import com.newwind.nwtweaks.client.world.item.renderer.AirBladderDecorator;
import com.newwind.nwtweaks.networking.ModMessages;
import com.newwind.nwtweaks.registries.EntityTypes;
import com.newwind.nwtweaks.util.CommonUtils;
import com.newwind.nwtweaks.world.items.PillItem;
import fuzs.thinair.init.ModRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.OptionInstance;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.FogRenderer;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.Items;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.*;
import net.minecraftforge.client.gui.overlay.VanillaGuiOverlay;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import tfar.classicbar.EventHandler;

import java.util.Objects;

@Mod.EventBusSubscriber(modid = NWTweaks.MODID, value = Dist.CLIENT)
public class ClientEvents {

	private static double nameDrawHeight = 0.0D;

	@SubscribeEvent
	public static void onRenderTick(TickEvent.RenderTickEvent event) {
		if (event.phase == TickEvent.Phase.START) {
			int maxRenderDistance = (int) Math.ceil(NWConfig.Client.MAX_FOG_END.get() / 16D);
			OptionInstance<Integer> renderDistance = Minecraft.getInstance().options.renderDistance();
			if (renderDistance.get() > maxRenderDistance)
				renderDistance.set(maxRenderDistance);
		}
	}

	public static void updateNameDrawHeight(int left, int right) {
		int height = Math.max(left, right);
		nameDrawHeight = Math.min(0.0, -(height - 59.0));
	}

	@SubscribeEvent(
					priority = EventPriority.LOWEST
	)
	public static void preRenderGuiOverlay(RenderGuiOverlayEvent.Pre event) {
		if (event.getOverlay() == VanillaGuiOverlay.ITEM_NAME.type()) {
			event.getPoseStack().translate(0.0, nameDrawHeight, 0.0);
		}
	}

	@SubscribeEvent(
					priority = EventPriority.LOWEST
	)
	public static void postRenderGuiOverlay(RenderGuiOverlayEvent.Post event) {

		if (event.getOverlay() == VanillaGuiOverlay.ITEM_NAME.type()) {
			event.getPoseStack().translate(0.0, -nameDrawHeight, 0.0);
		}
	}

	@SubscribeEvent
	public static void onKeyPressed(InputEvent.Key event) {

	}

	@SubscribeEvent
	public static void onFogColor(ViewportEvent.ComputeFogColor event) {
		if (NWClient.undergroundTransitionValue > 0.0F) {
			float transitionValue = NWClient.undergroundTransitionValue;

			event.setRed(Mth.lerp(transitionValue, event.getRed(), 0F));
			event.setGreen(Mth.lerp(transitionValue, event.getGreen(), 0F));
			event.setBlue(Mth.lerp(transitionValue, event.getBlue(), 0F));
		}
	}

	@SubscribeEvent
	public static void applyFogMods(ViewportEvent.RenderFog event) {
		if (CommonUtils.isUnderground(Objects.requireNonNull(Minecraft.getInstance().player)))
			NWClient.undergroundTransitionValue = Math.min(1.0F, NWClient.undergroundTransitionValue + Minecraft.getInstance().getDeltaFrameTime() / 100F);
		else
			NWClient.undergroundTransitionValue = Math.max(0.0F, NWClient.undergroundTransitionValue - Minecraft.getInstance().getDeltaFrameTime() / 30F);

		if (NWClient.undergroundTransitionValue > 0.0F) {
			float fogStart = event.getMode() == FogRenderer.FogMode.FOG_SKY ? 0.0F : NWConfig.Client.UNDERGROUND_FOG_START.get().floatValue();
			float fogEnd = NWConfig.Client.UNDERGROUND_FOG_END.get().floatValue();
			float regularFogEnd = getSurfaceFogEnd();

			float transitionValue = NWClient.undergroundTransitionValue;
			applyFogDistance(Mth.lerp(transitionValue, NWConfig.Client.REGULAR_FOG_START.get().floatValue(), fogStart),
							Mth.lerp(transitionValue, regularFogEnd, fogEnd));
		} else
			applyRegularFog();
	}

	private static void applyRegularFog() {
		applyFogDistance(NWConfig.Client.REGULAR_FOG_START.get().floatValue(), getSurfaceFogEnd());
	}

	private static float getSurfaceFogEnd() {
		LocalPlayer player = Minecraft.getInstance().player;
		float maxFogEnd = NWConfig.Client.MAX_FOG_END.get().floatValue();
		float regularFogEnd = NWConfig.Client.REGULAR_FOG_END.get().floatValue();
		float fogEnd = Math.min(regularFogEnd, maxFogEnd);
		if (player != null && player.hasEffect(MobEffects.NIGHT_VISION))
			fogEnd += (maxFogEnd - regularFogEnd) * GameRenderer.getNightVisionScale(player, 1f);
		return fogEnd;
	}

	private static void applyFogDistance(float fogStart, float fogEnd) {
		float currentFogEnd = RenderSystem.getShaderFogEnd();
		float currentFogStart = RenderSystem.getShaderFogStart();

		float currentIntensity = -currentFogStart / (currentFogEnd - currentFogStart);
		float nextIntensity = -fogStart / (fogEnd - fogStart);

		if (fogEnd < currentFogEnd) {
			RenderSystem.setShaderFogEnd(fogEnd);
			if (currentIntensity > nextIntensity)
				fogStart = fogEnd * -currentIntensity;
		} else if (currentIntensity < nextIntensity)
			fogStart = currentFogEnd * -nextIntensity;
		else if (currentIntensity > nextIntensity)
			fogStart = currentFogEnd * -currentIntensity;

		if (fogStart < currentFogStart)
			RenderSystem.setShaderFogStart(fogStart);
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
				ItemProperties.register(ModRegistry.AIR_BLADDER_ITEM.get(), new ResourceLocation(NWTweaks.MODID, "oxygen_amount"), (stack, level, entity, seed) -> {
					CompoundTag nbt = stack.getOrCreateTag();
					int oxygenAmount;
					if (nbt.contains(CommonUtils.AIR_BLADDER_TAG_OXYGEN_AMOUNT))
						oxygenAmount = nbt.getInt(CommonUtils.AIR_BLADDER_TAG_OXYGEN_AMOUNT);
					else
						oxygenAmount = NWConfig.Common.BLADDER_CAPACITY.get();

					return (float) oxygenAmount / (float) NWConfig.Common.BLADDER_CAPACITY.get();
				});
				ItemProperties.register(com.newwind.nwtweaks.registries.Items.PILL_ITEM.get(), new ResourceLocation(NWTweaks.MODID, "pill_type"), (stack, level, entity, seed) -> {
					CompoundTag nbt = stack.getOrCreateTag();
					int pillType;
					if (nbt.contains(PillItem.SLOT_TAG))
						pillType = nbt.getInt(PillItem.SLOT_TAG) - 1;
					else
						pillType = -1;

					return pillType;
				});
				TetraStats.addBar();
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

		@SubscribeEvent
		public static void registerEntityLayers(EntityRenderersEvent.RegisterLayerDefinitions event) {
			event.registerLayerDefinition(LootBagModel.LAYER_LOCATION, LootBagModel::createBodyLayer);
		}

		@SubscribeEvent
		public static void registerEntityRenderers(EntityRenderersEvent.RegisterRenderers event) {
			event.registerEntityRenderer(EntityTypes.LOOT_BAG.get(), LootBagRenderer::new);
		}

	}

}
