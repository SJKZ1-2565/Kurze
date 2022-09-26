package com.sjkz1.kurze.entity;

import com.google.common.collect.ImmutableList;
import com.mojang.serialization.Dynamic;
import com.sjkz1.kurze.entity.ai.KurzeEntityAi;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.DebugPackets;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.IndirectEntityDamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.behavior.StartAttacking;
import net.minecraft.world.entity.ai.control.FlyingMoveControl;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.navigation.FlyingPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.ai.sensing.Sensor;
import net.minecraft.world.entity.ai.sensing.SensorType;
import net.minecraft.world.entity.animal.FlyingAnimal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class KurzeEntity extends PathfinderMob implements FlyingAnimal {

    public int torpor;

    private static final EntityDataAccessor<Boolean> DATA_IS_KNOCK_OUT = SynchedEntityData.defineId(KurzeEntity.class, EntityDataSerializers.BOOLEAN);

    protected static final ImmutableList<SensorType<? extends Sensor<? super KurzeEntity>>> SENSOR_TYPES = ImmutableList.of(SensorType.NEAREST_LIVING_ENTITIES, SensorType.NEAREST_PLAYERS);
    protected static final ImmutableList<MemoryModuleType<?>> MEMORY_TYPES = ImmutableList.of(MemoryModuleType.PATH, MemoryModuleType.LOOK_TARGET, MemoryModuleType.NEAREST_VISIBLE_LIVING_ENTITIES, MemoryModuleType.WALK_TARGET, MemoryModuleType.CANT_REACH_WALK_TARGET_SINCE, MemoryModuleType.LIKED_NOTEBLOCK_COOLDOWN_TICKS, MemoryModuleType.IS_PANICKING);


    public KurzeEntity(EntityType<? extends KurzeEntity> entityType, Level level) {
        super(entityType, level);
        this.moveControl = new FlyingMoveControl(this, 20, true);
    }

    @Override
    protected PathNavigation createNavigation(Level level) {
        FlyingPathNavigation flyingPathNavigation = new FlyingPathNavigation(this, level);
        flyingPathNavigation.setCanOpenDoors(false);
        flyingPathNavigation.setCanFloat(true);
        flyingPathNavigation.setCanPassDoors(true);
        return flyingPathNavigation;
    }

    @Override
    public boolean hurt(DamageSource damageSource, float f) {
        boolean bl = super.hurt(damageSource, f);
        if (!(this.level.isClientSide || this.isNoAi())) {
            Entity entity = damageSource.getEntity();
            if (this.brain.getMemory(MemoryModuleType.ATTACK_TARGET).isEmpty() && entity instanceof LivingEntity livingEntity) {
                if (!(damageSource instanceof IndirectEntityDamageSource) || this.closerThan(livingEntity, 5.0)) {
                    this.setAttackTarget(livingEntity);
                }
            }
        }
        return bl;
    }

    public void setAttackTarget(LivingEntity livingEntity) {
        StartAttacking.setAttackTarget(this, livingEntity);
    }

    protected Brain.Provider<KurzeEntity> brainProvider() {
        return Brain.provider(MEMORY_TYPES, SENSOR_TYPES);
    }

    @Override
    protected Brain<?> makeBrain(Dynamic<?> dynamic) {
        return KurzeEntityAi.makeBrain(this.brainProvider().makeBrain(dynamic));
    }


    public Brain<KurzeEntity> getBrain() {
        return (Brain<KurzeEntity>) super.getBrain();
    }

    @Override
    protected void sendDebugPackets() {
        super.sendDebugPackets();
        DebugPackets.sendEntityBrain(this);
    }

    @Override
    @Nullable
    public LivingEntity getTarget() {
        return this.getBrain().getMemory(MemoryModuleType.ATTACK_TARGET).orElse(null);
    }

    @Override
    public boolean causeFallDamage(float f, float g, DamageSource damageSource) {
        return false;
    }

    @Override
    protected void checkFallDamage(double d, boolean bl, BlockState blockState, BlockPos blockPos) {
    }

    @Override
    @Nullable
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor serverLevelAccessor, DifficultyInstance difficultyInstance, MobSpawnType mobSpawnType, @Nullable SpawnGroupData spawnGroupData, @Nullable CompoundTag compoundTag) {
        return super.finalizeSpawn(serverLevelAccessor, difficultyInstance, mobSpawnType, spawnGroupData, compoundTag);
    }


    @Override
    protected void customServerAiStep() {
        this.level.getProfiler().push("kurzeBrain");
        this.getBrain().tick((ServerLevel) this.level, this);
        this.level.getProfiler().pop();
        this.level.getProfiler().push("kurzeActivityUpdate");
        KurzeEntityAi.updateActivity(this);
        this.level.getProfiler().pop();
        super.customServerAiStep();
    }

    @Override
    public boolean isPushable() {
        return false;
    }

    @Override
    public boolean canBeCollidedWith() {
        return false;
    }

    public static AttributeSupplier.Builder createKurzeAttributes() {
        return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 20.0).add(Attributes.FLYING_SPEED, 0.5f).add(Attributes.MOVEMENT_SPEED, 0.5f).add(Attributes.ATTACK_DAMAGE, 2.0).add(Attributes.FOLLOW_RANGE, 48.0);
    }

    @Nullable
    @Override
    protected SoundEvent getHurtSound(DamageSource damageSource) {
        return SoundEvents.SHULKER_HURT;
    }

    @Nullable
    @Override
    protected SoundEvent getAmbientSound() {
        return this.isKnockOut() ? null : SoundEvents.ENDERMAN_AMBIENT;
    }

    @Nullable
    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.SHULKER_DEATH;
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compoundTag) {
        super.addAdditionalSaveData(compoundTag);
        compoundTag.putInt("Torpidity", this.getTorpor());
        compoundTag.putBoolean("KO", this.isKnockOut());
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compoundTag) {
        super.readAdditionalSaveData(compoundTag);
        this.setTorpor(compoundTag.getInt("Torpidity"));
        this.entityData.set(DATA_IS_KNOCK_OUT, compoundTag.getBoolean("KO"));
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_IS_KNOCK_OUT, false);
    }

    public void setTorpor(int torpor) {
        this.torpor = torpor;
    }

    public void setKnockOut(boolean knockOut) {
        this.entityData.set(DATA_IS_KNOCK_OUT, knockOut);
    }

    public int getTorpor() {
        return torpor;
    }

    public boolean isKnockOut() {
        return this.entityData.get(DATA_IS_KNOCK_OUT);
    }

    @Override
    public boolean canBeLeashed(Player player) {
        return true;
    }

    @Override
    public boolean isFlying() {
        return !this.isKnockOut();
    }
}
