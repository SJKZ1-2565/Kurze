package com.sjkz1.kurze;

import com.sjkz1.kurze.entity.FallingObsidian;
import com.sjkz1.kurze.entity.KurzeEntity;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;

public class KurzeEntities {
    public static final EntityType<KurzeEntity> KURZE = Registry.register(Registry.ENTITY_TYPE, new ResourceLocation("kurze", "kurze_"), FabricEntityTypeBuilder.create(MobCategory.MONSTER, KurzeEntity::new).dimensions(EntityDimensions.fixed(1f, 1f)).build());
    public static final EntityType<FallingObsidian> FALLING_OBSIDIAN = Registry.register(Registry.ENTITY_TYPE, new ResourceLocation("kurze", "falling_obsidian"), FabricEntityTypeBuilder.create(MobCategory.MISC, FallingObsidian::new).dimensions(EntityDimensions.fixed(1f, 1f)).build());

}
