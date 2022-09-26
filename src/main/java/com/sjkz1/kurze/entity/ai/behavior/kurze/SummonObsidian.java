package com.sjkz1.kurze.entity.ai.behavior.kurze;

import com.google.common.collect.ImmutableMap;
import com.sjkz1.kurze.Kurze;
import com.sjkz1.kurze.KurzeEntities;
import com.sjkz1.kurze.entity.FallingObsidian;
import com.sjkz1.kurze.entity.KurzeEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.util.Unit;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;

public class SummonObsidian extends Behavior<KurzeEntity> {
    public SummonObsidian() {
        super(ImmutableMap.of(MemoryModuleType.ATTACK_TARGET, MemoryStatus.VALUE_PRESENT, Kurze.SPAWN_OBSIDIAN_COOLDOWN, MemoryStatus.REGISTERED), DURATION);
    }

    private static final int DURATION = Mth.ceil(60.0f);

    private static final int TICKS_BEFORE_PLAYING_SOUND = Mth.ceil(34.0);


    @Override
    protected boolean canStillUse(ServerLevel serverLevel, KurzeEntity livingEntity, long l) {
        return true;
    }

    @Override
    protected void start(ServerLevel serverLevel, KurzeEntity livingEntity, long l) {
        livingEntity.getBrain().setMemoryWithExpiry(Kurze.SPAWN_OBSIDIAN_COOLDOWN, Unit.INSTANCE, TICKS_BEFORE_PLAYING_SOUND);
        livingEntity.playSound(SoundEvents.EVOKER_CAST_SPELL, 3.0f, 1.0f);
    }

    @Override
    protected void tick(ServerLevel serverLevel, KurzeEntity kurzeEntity, long l) {
        kurzeEntity.getBrain().getMemory(MemoryModuleType.ATTACK_TARGET).ifPresent(livingEntity -> kurzeEntity.getLookControl().setLookAt(livingEntity.position()));
        if (kurzeEntity.getBrain().hasMemoryValue(Kurze.SPAWN_OBSIDIAN_COOLDOWN)) {
            return;
        }
        kurzeEntity.getBrain().setMemoryWithExpiry(Kurze.SPAWN_OBSIDIAN_COOLDOWN, Unit.INSTANCE, DURATION - TICKS_BEFORE_PLAYING_SOUND);
        kurzeEntity.getBrain().getMemory(MemoryModuleType.ATTACK_TARGET).filter(livingEntity -> kurzeEntity.closerThan((Entity) livingEntity, 15.0, 20.0)).ifPresent(livingEntity -> {
            BlockPos blockPos = livingEntity.getOnPos();
            FallingObsidian fallingObsidian = new FallingObsidian(KurzeEntities.FALLING_OBSIDIAN, kurzeEntity.level);
            fallingObsidian.setPos(blockPos.getX() + 0.5, blockPos.getY() + 3, blockPos.getZ() + 0.5);
            fallingObsidian.setHurtsEntities(8, 40);
            fallingObsidian.time = 20;
            fallingObsidian.dropItem = false;
            fallingObsidian.cancelDrop = true;
            kurzeEntity.level.addFreshEntity(fallingObsidian);
            livingEntity.addEffect(new MobEffectInstance(Kurze.FEAR));
        });
    }

    @Override
    protected void stop(ServerLevel serverLevel, KurzeEntity kurzeEntity, long l) {
        SummonObsidian.setCooldown(kurzeEntity, 40);
    }

    public static void setCooldown(LivingEntity livingEntity, int i) {
        livingEntity.getBrain().setMemoryWithExpiry(MemoryModuleType.SONIC_BOOM_COOLDOWN, Unit.INSTANCE, i);
    }
}
