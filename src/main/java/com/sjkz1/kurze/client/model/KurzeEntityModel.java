package com.sjkz1.kurze.client.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.sjkz1.kurze.entity.KurzeEntity;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;

public class KurzeEntityModel<T extends KurzeEntity> extends EntityModel<T> {
    private final ModelPart main;
    private final ModelPart tentacle;

    public KurzeEntityModel(ModelPart root) {
        this.main = root.getChild("main");
        this.tentacle = main.getChild("tentacle");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition main = partdefinition.addOrReplaceChild("main", CubeListBuilder.create(),
                PartPose.offset(0.0F, 24.0F, 0.0F));

        main.addOrReplaceChild("eye",
                CubeListBuilder.create().texOffs(0, 0).addBox(-3.0F, -11.0F, -3.0F, 6.0F, 6.0F, 6.0F, new CubeDeformation(0.0F)),
                PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition mouth = main.addOrReplaceChild("mouth",
                CubeListBuilder.create().texOffs(32, 32)
                        .addBox(-8.0F, -16.0F, -8.0F, 0.0F, 16.0F, 16.0F, new CubeDeformation(0.0F)).texOffs(32, 16)
                        .addBox(8.0F, -16.0F, -8.0F, 0.0F, 16.0F, 16.0F, new CubeDeformation(0.0F)),
                PartPose.offset(0.0F, 0.0F, 0.0F));

        mouth.addOrReplaceChild("cube_r1",
                CubeListBuilder.create().texOffs(0, 16).addBox(0.0F, -8.0F, -8.0F, 0.0F, 16.0F, 16.0F, new CubeDeformation(0.0F))
                        .texOffs(0, 32).addBox(-16.0F, -8.0F, -8.0F, 0.0F, 16.0F, 16.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 1.5708F));

        mouth.addOrReplaceChild("cube_r2",
                CubeListBuilder.create().texOffs(0, 0).addBox(8.0F, -16.0F, -8.0F, 0.0F, 16.0F, 16.0F, new CubeDeformation(0.0F))
                        .texOffs(32, 0).addBox(-8.0F, -16.0F, -8.0F, 0.0F, 16.0F, 16.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, 0.0F, -1.5708F, 0.0F));

        PartDefinition tentacle = main.addOrReplaceChild("tentacle",
                CubeListBuilder.create().texOffs(32, 0).addBox(-8.0F, 0.0F, -5.0F, 16.0F, 16.0F, 0.0F, new CubeDeformation(0.0F))
                        .texOffs(32, -16).addBox(5.0F, 0.0F, -8.0F, 0.0F, 16.0F, 16.0F, new CubeDeformation(0.0F)).texOffs(32, -16)
                        .addBox(-5.0F, 0.0F, -8.0F, 0.0F, 16.0F, 16.0F, new CubeDeformation(0.0F)),
                PartPose.offset(0.0F, 0.0F, 0.0F));

        tentacle.addOrReplaceChild("cube_r3", CubeListBuilder.create().texOffs(32, -16).addBox(5.0F, -16.0F,
                        -8.0F, 0.0F, 16.0F, 16.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(0.0F, 16.0F, 0.0F, 0.0F, -1.5708F, 0.0F));

        return LayerDefinition.create(meshdefinition, 64, 64);
    }

    @Override
    public void setupAnim(T entity, float f, float g, float h, float i, float j) {
        this.tentacle.yRot = (float) Math.ceil((h / 4) * Math.PI);
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int i, int j, float f, float g, float h, float k) {
        main.render(poseStack, vertexConsumer, i, j, f, g, h, k);
    }
}
