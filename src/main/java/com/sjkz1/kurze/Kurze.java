package com.sjkz1.kurze;


import com.mojang.serialization.Codec;
import com.sjkz1.kurze.effects.FearEffcts;
import com.sjkz1.kurze.entity.KurzeEntity;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Unit;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.SpawnEggItem;
import net.minecraft.world.level.block.Blocks;

import java.util.Optional;

public class Kurze implements ModInitializer {

    public static final MemoryModuleType<Unit> SPAWN_OBSIDIAN_COOLDOWN = Registry.register(Registry.MEMORY_MODULE_TYPE, new ResourceLocation("kurze", "spawn_obsidian_cooldown"), new MemoryModuleType<>(Optional.of(Codec.unit(Unit.INSTANCE))));
    public static final MemoryModuleType<Integer> KNOCK_OUT_COOLDOWN = Registry.register(Registry.MEMORY_MODULE_TYPE, new ResourceLocation("kurze", "knock_out_cooldown"), new MemoryModuleType<>(Optional.of(Codec.INT)));
    public static final Item KURZE_SPAWN_EGG = new SpawnEggItem(KurzeEntities.KURZE, 0x4b064f, 0xe254eb, new Item.Properties().tab(CreativeModeTab.TAB_MISC));
    public static final MobEffect FEAR = new FearEffcts();

    @Override
    public void onInitialize() {
        FabricDefaultAttributeRegistry.register(KurzeEntities.KURZE, KurzeEntity.createKurzeAttributes());
        Registry.register(Registry.ITEM, new ResourceLocation("kurze", "kurze_spawn_egg"), KURZE_SPAWN_EGG);
        Registry.register(Registry.MOB_EFFECT, new ResourceLocation("kurze", "fear"), FEAR);
        PlayerBlockBreakEvents.AFTER.register((world, player, pos, state, blockEntity) -> {
            if (world.random.nextInt(100) == 1 && state.is(Blocks.OBSIDIAN)) {
                KurzeEntity kurzeEntity = KurzeEntities.KURZE.create(world);
                assert kurzeEntity != null;
                kurzeEntity.setPos(pos.getX(), pos.getY(), pos.getZ());
                world.addFreshEntity(kurzeEntity);
            }
        });
    }

}
