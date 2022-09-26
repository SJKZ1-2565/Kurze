package com.sjkz1.kurze;

import com.sjkz1.kurze.client.model.KurzeEntityModel;
import com.sjkz1.kurze.client.render.KurzeEntityRenderer;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendereregistry.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.client.rendereregistry.v1.EntityRendererRegistry;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.resources.ResourceLocation;

public class Client implements ClientModInitializer {
    public static final ModelLayerLocation MODEL_KURZE_LAYER = new ModelLayerLocation(new ResourceLocation("hundred_day", "kurze"), "main");

    @Override
    public void onInitializeClient() {
        EntityRendererRegistry.INSTANCE.register(HundredDay.KURZE, KurzeEntityRenderer::new);
        EntityModelLayerRegistry.registerModelLayer(MODEL_KURZE_LAYER, KurzeEntityModel::createBodyLayer);
    }
}
