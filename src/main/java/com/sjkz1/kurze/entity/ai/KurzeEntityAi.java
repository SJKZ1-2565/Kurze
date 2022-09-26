package com.sjkz1.kurze.entity.ai;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.mojang.datafixers.util.Pair;
import com.sjkz1.kurze.entity.KurzeEntity;
import com.sjkz1.kurze.entity.ai.behavior.kurze.SummonObsidian;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.behavior.*;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.schedule.Activity;

public class KurzeEntityAi {

    public static Brain<?> makeBrain(Brain<KurzeEntity> brain) {
        KurzeEntityAi.initCoreActivity(brain);
        KurzeEntityAi.initIdleActivity(brain);
        KurzeEntityAi.initFightActivity(brain);
        brain.setCoreActivities(ImmutableSet.of(Activity.CORE));
        brain.setDefaultActivity(Activity.IDLE);
        brain.useDefaultActivity();
        return brain;
    }

    private static void initCoreActivity(Brain<KurzeEntity> brain) {
        brain.addActivity(Activity.CORE, 0, ImmutableList.of(new Swim(0.8f), new AnimalPanic(2.5f), new LookAtTargetSink(45, 90), new MoveToTargetSink()));
    }

    private static void initIdleActivity(Brain<KurzeEntity> brain) {
        brain.addActivityWithConditions(Activity.IDLE, ImmutableList.of(Pair.of(0, new RunSometimes<LivingEntity>(new SetEntityLookTarget(livingEntity -> true, 6.0f), UniformInt.of(30, 60))), Pair.of(1, new RunOne(ImmutableList.of(Pair.of(new FlyingRandomStroll(1.0f), 2), Pair.of(new SetWalkTargetFromLookTarget(1.0f, 3), 2))))), ImmutableSet.of());


    }

    private static void initFightActivity(Brain<KurzeEntity> brain) {
        brain.addActivityAndRemoveMemoryWhenStopped(Activity.FIGHT, 10, ImmutableList.of(new StopAttackingIfTargetInvalid<>(), new SetWalkTargetFromAttackTargetIfTargetOutOfReach(0.15f), new SummonObsidian(), new MeleeAttack(18)), MemoryModuleType.ATTACK_TARGET);

    }

    public static void updateActivity(KurzeEntity kurze) {
        Brain<KurzeEntity> brain = kurze.getBrain();
        brain.setActiveActivityToFirstValid(ImmutableList.of(Activity.FIGHT, Activity.IDLE));
    }

}
