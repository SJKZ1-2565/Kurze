package com.sjkz1.kurze;

import com.sjkz1.kurze.client.model.KurzeEntityModel;
import com.sjkz1.kurze.client.render.FallingObsidianRenderer;
import com.sjkz1.kurze.client.render.KurzeEntityRenderer;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.resources.ResourceLocation;


@SuppressWarnings("deprecation")
public class Client implements ClientModInitializer {
    public static final ModelLayerLocation MODEL_KURZE_LAYER = new ModelLayerLocation(new ResourceLocation("kurze", "kurze"), "main");

    @Override
    public void onInitializeClient() {
        EntityRendererRegistry.register(KurzeEntities.KURZE, KurzeEntityRenderer::new);
        EntityRendererRegistry.register(KurzeEntities.FALLING_OBSIDIAN, FallingObsidianRenderer::new);
        EntityModelLayerRegistry.registerModelLayer(MODEL_KURZE_LAYER, KurzeEntityModel::createBodyLayer);
    }
}
