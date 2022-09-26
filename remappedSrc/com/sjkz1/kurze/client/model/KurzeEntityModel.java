package com.sjkz1.kurze.client.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.sjkz1.kurze.entity.KurzeEntity;

import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

public class KurzeEntityModel<T extends KurzeEntity> extends EntityModel<T> {
    // This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation("kurze", "main"), "main");
    private final ModelPart main;
    private final ModelPart tentacle;

    public KurzeEntityModel(ModelPart root) {
        this.main = root.getChild("main");
        this.tentacle = main.getChild("tentacle");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition main = partdefinition.addOrReplaceChild("main", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));

        PartDefinition eye = main.addOrReplaceChild("eye", CubeListBuilder.create().texOffs(0, 0).addBox(-3.0F, -11.0F, -3.0F, 6.0F, 6.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition mouth = main.addOrReplaceChild("mouth", CubeListBuilder.create().texOffs(32, 32).addBox(-8.0F, -16.0F, -8.0F, 0.0F, 16.0F, 16.0F, new CubeDeformation(0.0F))
                .texOffs(32, 16).addBox(8.0F, -16.0F, -8.0F, 0.0F, 16.0F, 16.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition cube_r1 = mouth.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(0, 16).addBox(0.0F, -8.0F, -8.0F, 0.0F, 16.0F, 16.0F, new CubeDeformation(0.0F))
                .texOffs(0, 32).addBox(-16.0F, -8.0F, -8.0F, 0.0F, 16.0F, 16.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 1.5708F));

        PartDefinition cube_r2 = mouth.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(0, 0).addBox(8.0F, -16.0F, -8.0F, 0.0F, 16.0F, 16.0F, new CubeDeformation(0.0F))
                .texOffs(32, 0).addBox(-8.0F, -16.0F, -8.0F, 0.0F, 16.0F, 16.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, -1.5708F, 0.0F));

        PartDefinition tentacle = main.addOrReplaceChild("tentacle", CubeListBuilder.create().texOffs(32, 0).addBox(-8.0F, 0.0F, -5.0F, 16.0F, 16.0F, 0.0F, new CubeDeformation(0.0F))
                .texOffs(32, -16).addBox(5.0F, 0.0F, -8.0F, 0.0F, 16.0F, 16.0F, new CubeDeformation(0.0F))
                .texOffs(32, -16).addBox(-5.0F, 0.0F, -8.0F, 0.0F, 16.0F, 16.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition cube_r3 = tentacle.addOrReplaceChild("cube_r3", CubeListBuilder.create().texOffs(32, -16).addBox(5.0F, -16.0F, -8.0F, 0.0F, 16.0F, 16.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 16.0F, 0.0F, 0.0F, -1.5708F, 0.0F));

        return LayerDefinition.create(meshdefinition, 64, 64);
    }

    @Override
    public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        if (!entity.isOnGround()) {
            this.tentacle.xRot = 0.1F * Mth.sin(ageInTicks * 0.2F) + 0.2F;
        }
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        main.render(poseStack, buffer, packedLight, packedOverlay);
    }
}
