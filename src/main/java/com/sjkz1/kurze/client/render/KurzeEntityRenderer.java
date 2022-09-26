package com.sjkz1.kurze.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import com.sjkz1.kurze.Client;
import com.sjkz1.kurze.client.model.KurzeEntityModel;
import com.sjkz1.kurze.entity.KurzeEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class KurzeEntityRenderer extends MobRenderer<KurzeEntity, KurzeEntityModel<KurzeEntity>> {

    public KurzeEntityRenderer(EntityRendererProvider.Context context) {
        super(context, new KurzeEntityModel(context.bakeLayer(Client.MODEL_KURZE_LAYER)), 0.5f);
        this.addLayer(new KurzeGlowLayer<>(this));
    }


    @Override
    public ResourceLocation getTextureLocation(KurzeEntity entity) {
        return new ResourceLocation("kurze", "textures/entity/kurze/kurze.png");
    }

    @Override
    protected void setupRotations(KurzeEntity kurzeEntity, PoseStack poseStack, float f, float g, float h) {
        super.setupRotations(kurzeEntity, poseStack, f, g, h);
        if (kurzeEntity.isKnockOut()) {
            poseStack.translate(0.0, kurzeEntity.getBbHeight() + 0.1f, 0.0);
            poseStack.mulPose(Vector3f.ZP.rotationDegrees(180.0f));
        }
    }
}
