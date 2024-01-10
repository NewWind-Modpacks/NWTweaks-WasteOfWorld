package com.newwind.nwtweaks.client.world.entity.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.newwind.nwtweaks.NWTweaks;
import com.newwind.nwtweaks.client.world.entity.model.LootBagModel;
import com.newwind.nwtweaks.world.entities.LootBag;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public class LootBagRenderer extends EntityRenderer<LootBag> {

	private static final ResourceLocation TEXTURE_PLAYER = new ResourceLocation(NWTweaks.MODID, "textures/entity/loot_bag/player.png");
	private static final ResourceLocation TEXTURE_BOSS = new ResourceLocation(NWTweaks.MODID, "textures/entity/loot_bag/boss.png");
	private static final ResourceLocation TEXTURE_EVENT = new ResourceLocation(NWTweaks.MODID, "textures/entity/loot_bag/event.png");

	private final LootBagModel<LootBag> model;

	public LootBagRenderer(EntityRendererProvider.Context context) {
		super(context);
		this.shadowRadius = 0f;
		this.model = new LootBagModel<>(context.bakeLayer(LootBagModel.LAYER_LOCATION));
	}

	@Override
	public @NotNull ResourceLocation getTextureLocation(@NotNull LootBag lootBag) {
		return switch (lootBag.getBagType()) {
			case PLAYER -> TEXTURE_PLAYER;
			case BOSS -> TEXTURE_BOSS;
			case EVENT -> TEXTURE_EVENT;
		};
	}

	@Override
	public void render(@NotNull LootBag lootBag, float p_114486_, float p_114487_, @NotNull PoseStack poseStack, @NotNull MultiBufferSource multiBufferSource, int packedLight) {
		poseStack.pushPose();

		poseStack.scale(-1f, -1f, 1f);
		poseStack.translate(0f, -1.5f, 0f);
		VertexConsumer vertexConsumer = multiBufferSource.getBuffer(RenderType.entitySolid(getTextureLocation(lootBag)));
		model.renderToBuffer(poseStack, vertexConsumer, packedLight, OverlayTexture.NO_OVERLAY, 1f, 1f, 1f, 1f);

		poseStack.popPose();
		super.render(lootBag, p_114486_, p_114487_, poseStack, multiBufferSource, packedLight);
	}

	@Override
	protected boolean shouldShowName(@NotNull LootBag lootBag) {
		LocalPlayer localPlayer = Minecraft.getInstance().player;
		if (localPlayer == null)
			return false;
		return super.shouldShowName(lootBag) || (
						lootBag.hasCustomName()
										&& lootBag == this.entityRenderDispatcher.crosshairPickEntity
										&& Minecraft.renderNames()
										&& !lootBag.isInvisibleTo(localPlayer)
		);
	}

}
