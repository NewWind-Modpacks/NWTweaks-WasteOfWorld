package com.newwind.nwtweaks.mixin.client;

import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.newwind.nwtweaks.capability.RedDwellerProvider;
import de.cadentem.cave_dweller.client.CaveDwellerEyesLayer;
import de.cadentem.cave_dweller.entities.CaveDwellerEntity;
import net.minecraft.Util;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Unique;
import software.bernie.geckolib3.renderers.geo.GeoLayerRenderer;
import software.bernie.geckolib3.renderers.geo.IGeoRenderer;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.BiFunction;

import static de.cadentem.cave_dweller.client.CaveDwellerEyesLayer.TEXTURE;
import static net.minecraft.client.renderer.RenderStateShard.ShaderStateShard.*;

@Mixin(CaveDwellerEyesLayer.class)
public abstract class CDCaveDwellerEyesLayer extends GeoLayerRenderer<CaveDwellerEntity> {

	@Unique
	private static final String TAG_INVULNERABLE = "nWTweaks:is_red_dweller";
	@Unique
	private static final BiFunction<ResourceLocation, Boolean, RenderType> ENTITY_CUTOUT_NO_CULL_NO_FOG = Util.memoize((texture, outline) -> {
		RenderType.CompositeState.CompositeStateBuilder builder = RenderType.CompositeState.builder();
		builder.setShaderState(RENDERTYPE_OUTLINE_SHADER);
		builder.setTextureState(new TextureStateShard(texture, false, false));
		builder.setTransparencyState(NO_TRANSPARENCY);
		builder.setCullState(NO_CULL);
		builder.setLightmapState(LIGHTMAP);
		builder.setOverlayState(OVERLAY);
		RenderType.CompositeState rendertype$compositestate = builder.createCompositeState(outline);
		return RenderType.create("entity_cutout_no_cull",
						DefaultVertexFormat.NEW_ENTITY,
						VertexFormat.Mode.QUADS,
						256,
						true,
						false,
						rendertype$compositestate);
	});

	// Dummy
	public CDCaveDwellerEyesLayer(IGeoRenderer<CaveDwellerEntity> entityRendererIn) {
		super(entityRendererIn);
	}

	/**
	 * @author Kevadroz
	 * @reason Implement See-through fog color-able eyes.
	 */
	@Overwrite(
					remap = false
	)
	public void render(PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn, CaveDwellerEntity entityLivingBaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
		packedLightIn = 15728880;
		RenderType eyesRenderType = ENTITY_CUTOUT_NO_CULL_NO_FOG.apply(TEXTURE, true);
		VertexConsumer vertexConsumer = bufferIn.getBuffer(eyesRenderType);

		AtomicBoolean isRedDweller = new AtomicBoolean(false);
		entityLivingBaseIn.getCapability(RedDwellerProvider.CAPABILITY).ifPresent(redDweller -> isRedDweller.set(redDweller.isRedDweller()));

		float green = 1F;
		float blue = 1F;
		if (isRedDweller.get()) {
			green = blue = 0F;
		}
		this.getRenderer().render(this.getEntityModel().getModel(this.getEntityModel().getModelResource(entityLivingBaseIn)), entityLivingBaseIn, partialTicks, eyesRenderType, matrixStackIn, bufferIn, vertexConsumer, packedLightIn, OverlayTexture.NO_OVERLAY, 1.0F, green, blue, 1.0F);
	}

}
