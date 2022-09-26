package com.sjkz1.kurze.client.render;

import com.sjkz1.kurze.client.model.KurzeEntityModel;
import com.sjkz1.kurze.entity.KurzeEntity;

import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.EyesLayer;
import net.minecraft.resources.ResourceLocation;

public class KurzeGlowLayer<T extends KurzeEntity> extends EyesLayer<T, KurzeEntityModel<T>> {
    private static final RenderType PHANTOM_EYES = RenderType.eyes(new ResourceLocation("kurze", "textures/entity/kurze/kurze_glow.png"));

    public KurzeGlowLayer(RenderLayerParent<T, KurzeEntityModel<T>> renderLayerParent) {
        super(renderLayerParent);
    }

    @Override
    public RenderType renderType() {
        return PHANTOM_EYES;
    }
}
