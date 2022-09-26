package com.sjkz1.kurze;


import Item;
import TintedGlassPane;
import com.sjkz1.kurze.entity.KurzeEntity;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.core.Registry;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.item.*;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;

public class HundredDay implements ModInitializer {

    public static final EntityType<KurzeEntity> KURZE = Registry.register(Registry.ENTITY_TYPE, new ResourceLocation("kurze", "kurze"), FabricEntityTypeBuilder.create(MobCategory.CREATURE, KurzeEntity::new).dimensions(EntityDimensions.fixed(1f, 1f)).build());
    public static final Item KURZE_SPAWN_EGG = new SpawnEggItem(KURZE, 0x27006b, 0x0c0021, new Item.Properties().tab(CreativeModeTab.TAB_MISC));
    public static final Item EXPOSED_COPPER_INGOT = new Item(new Item.Properties().tab(CreativeModeTab.TAB_MATERIALS));
    public static final Item WAXED_EXPOSED_COPPER_INGOT = new Item(new Item.Properties().tab(CreativeModeTab.TAB_MATERIALS));
    public static final Item WAXED_COPPER_INGOT = new Item(new Item.Properties().tab(CreativeModeTab.TAB_MATERIALS));
    public static final Item WEATHER_COPPER_INGOT = new Item(new Item.Properties().tab(CreativeModeTab.TAB_MATERIALS));
    public static final Item WAXED_WEATHER_COPPER_INGOT = new Item(new Item.Properties().tab(CreativeModeTab.TAB_MATERIALS));
    public static final Item OXIDIZED_COPPER_INGOT = new Item(new Item.Properties().tab(CreativeModeTab.TAB_MATERIALS));
    public static final Item WAXED_OXIDIZED_COPPER_INGOT = new Item(new Item.Properties().tab(CreativeModeTab.TAB_MATERIALS));
    public static final ObsidianBlock FALLING_OBSIDIAN = new ObsidianBlock(BlockBehaviour.Properties.copy(Blocks.OBSIDIAN));
    public static final TintedGlassPane TINTED_GLASS_PANE = new TintedGlassPane(BlockBehaviour.Properties.copy(Blocks.TINTED_GLASS));

    @Override
    public void onInitialize() {
        FabricDefaultAttributeRegistry.register(KURZE, KurzeEntity.createKurzeAttributes());
        Registry.register(Registry.ITEM, new ResourceLocation("kurze", "kurze_spawn_egg"), KURZE_SPAWN_EGG);
        Registry.register(Registry.ITEM, new ResourceLocation("kurze", "exposed_copper_ingot"), EXPOSED_COPPER_INGOT);
        Registry.register(Registry.ITEM, new ResourceLocation("kurze", "weathered_copper_ingot"), WEATHER_COPPER_INGOT);
        Registry.register(Registry.ITEM, new ResourceLocation("kurze", "oxidized_copper_ingot"), OXIDIZED_COPPER_INGOT);

        Registry.register(Registry.ITEM, new ResourceLocation("kurze", "waxed_copper_ingot"), WAXED_COPPER_INGOT);
        Registry.register(Registry.ITEM, new ResourceLocation("kurze", "waxed_exposed_copper_ingot"), WAXED_EXPOSED_COPPER_INGOT);
        Registry.register(Registry.ITEM, new ResourceLocation("kurze", "waxed_weathered_copper_ingot"), WAXED_WEATHER_COPPER_INGOT);
        Registry.register(Registry.ITEM, new ResourceLocation("kurze", "waxed_oxidized_copper_ingot"), WAXED_OXIDIZED_COPPER_INGOT);

        Registry.register(Registry.BLOCK, new ResourceLocation("kurze", "falling_obsidian"), FALLING_OBSIDIAN);
        Registry.register(Registry.BLOCK, new ResourceLocation("kurze", "tinted_glass_pane"), TINTED_GLASS_PANE);
        Registry.register(Registry.ITEM, new ResourceLocation("kurze", "tinted_glass_pane"), new BlockItem(TINTED_GLASS_PANE, new FabricItemSettings().tab(CreativeModeTab.TAB_MISC)));
    }

}
