package com.sjkz1.kurze.effects;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;

public class FearEffcts extends MobEffect {
    public FearEffcts() {
        super(MobEffectCategory.BENEFICIAL, 0x8f11a8);
    }

    @Override
    public boolean isDurationEffectTick(int i, int j) {
        return true;
    }

    @Override
    public void applyEffectTick(LivingEntity livingEntity, int i) {
        livingEntity.makeStuckInBlock(livingEntity.getFeetBlockState(), new Vec3(0.9f, 1.5, 0.9f));
        livingEntity.invulnerableTime = 1 << i;
    }
}
