package com.sjkz1.kurze.entity.ai.behavior.kurze;

import com.google.common.collect.ImmutableMap;
import com.sjkz1.kurze.Kurze;
import com.sjkz1.kurze.entity.KurzeEntity;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.world.entity.ai.memory.MemoryStatus;

public class KnockOut extends Behavior<KurzeEntity> {
    public KnockOut() {
        super(ImmutableMap.of(Kurze.KNOCK_OUT_COOLDOWN, MemoryStatus.VALUE_PRESENT), 6000);
    }

    @Override
    protected boolean checkExtraStartConditions(ServerLevel serverLevel, KurzeEntity kurzeEntity) {
        return kurzeEntity.getTorpor() >= 100;
    }

    @Override
    protected boolean canStillUse(ServerLevel serverLevel, KurzeEntity kurzeEntity, long l) {
        return kurzeEntity.getTorpor() >= 100 && kurzeEntity.getBrain().hasMemoryValue(Kurze.KNOCK_OUT_COOLDOWN);
    }

    @Override
    protected void start(ServerLevel serverLevel, KurzeEntity kurzeEntity, long l) {
        kurzeEntity.setKnockOut(true);
    }

}
