package com.sjkz1.kurze.entity;


import java.util.EnumSet;
import java.util.Random;

import org.jetbrains.annotations.Nullable;

import com.sjkz1.kurze.HundredDay;

import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.FlyingMob;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public class KurzeEntity extends FlyingMob implements Enemy {


    public KurzeEntity(EntityType<? extends FlyingMob> entityType, Level level) {
        super(entityType, level);
        this.moveControl = new KurzeEntity.GhastMoveControl(this);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(1, new FloatGoal(this));
        this.goalSelector.addGoal(5, new KurzeEntity.RandomFloatAroundGoal(this));
        this.goalSelector.addGoal(7, new KurzeEntity.GhastLookGoal(this));
        this.goalSelector.addGoal(7, new KurzeEntity.GhastShootFireballGoal(this));
        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal(this, Player.class, false));
    }

    private static void spawnFallingBlock(BlockState blockState, Level level, BlockPos blockPos) {
        Vec3 vec3d = Vec3.atBottomCenterOf(blockPos);
        FallingBlockEntity fallingBlockEntity = new FallingBlockEntity(level, vec3d.x, vec3d.y, vec3d.z, blockState);
        fallingBlockEntity.setHurtsEntities(8, 40);
        level.addFreshEntity(fallingBlockEntity);
        fallingBlockEntity.time = 20;
    }


    @Override
    public boolean isPushable() {
        return false;
    }


    public static AttributeSupplier.Builder createKurzeAttributes() {
        return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 16.0D).add(Attributes.FLYING_SPEED, 0.4000000059604645D).add(Attributes.MOVEMENT_SPEED, 0.20000000298023224D);
    }

    @Nullable
    @Override
    protected SoundEvent getHurtSound(DamageSource damageSource) {
        return SoundEvents.SHULKER_HURT;
    }

    @Nullable
    @Override
    protected SoundEvent getAmbientSound() {
        return SoundEvents.ENDERMAN_AMBIENT;
    }

    @Nullable
    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.SHULKER_DEATH;
    }

    private static class RandomFloatAroundGoal extends Goal {
        private final KurzeEntity kurze;

        public RandomFloatAroundGoal(KurzeEntity kurze) {
            this.kurze = kurze;
            this.setFlags(EnumSet.of(Goal.Flag.MOVE));
        }

        @Override
        public boolean canUse() {
            MoveControl moveControl = this.kurze.getMoveControl();
            if (!moveControl.hasWanted()) {
                return true;
            } else {
                double d = moveControl.getWantedX() - this.kurze.getX();
                double e = moveControl.getWantedY() - this.kurze.getY();
                double f = moveControl.getWantedZ() - this.kurze.getZ();
                double g = d * d + e * e + f * f;
                return g < 1.0D || g > 3600.0D;
            }
        }

        @Override
        public boolean canContinueToUse() {
            return false;
        }

        @Override
        public void start() {
            Random random = this.kurze.getRandom();
            double d = this.kurze.getX() + (random.nextFloat() * 2.0F - 1.0F) * 16.0F;
            double e = this.kurze.getY() + (random.nextFloat() * 2.0F - 1.0F) * 2.0F;
            double f = this.kurze.getZ() + (random.nextFloat() * 2.0F - 1.0F) * 16.0F;
            this.kurze.getMoveControl().setWantedPosition(d, e, f, 1.0D);
        }
    }

    static class GhastLookGoal extends Goal {
        private final KurzeEntity ghast;

        public GhastLookGoal(KurzeEntity ghast) {
            this.ghast = ghast;
            this.setFlags(EnumSet.of(Goal.Flag.LOOK));
        }

        @Override
        public boolean canUse() {
            return true;
        }

        @Override
        public void tick() {
            if (this.ghast.getTarget() == null) {
                Vec3 vec3 = this.ghast.getDeltaMovement();
                this.ghast.setYRot(-((float) Mth.atan2(vec3.x, vec3.z)) * 57.295776F);
                this.ghast.yBodyRot = this.ghast.getYRot();
            } else {
                LivingEntity livingEntity = this.ghast.getTarget();
                double d = 64.0D;
                if (livingEntity.distanceToSqr(this.ghast) < 4096.0D) {
                    double e = livingEntity.getX() - this.ghast.getX();
                    double f = livingEntity.getZ() - this.ghast.getZ();
                    this.ghast.setYRot(-((float) Mth.atan2(e, f)) * 57.295776F);
                    this.ghast.yBodyRot = this.ghast.getYRot();
                }
            }

        }
    }

    private static class GhastMoveControl extends MoveControl {
        private final KurzeEntity ghast;
        private int floatDuration;

        public GhastMoveControl(KurzeEntity ghast) {
            super(ghast);
            this.ghast = ghast;
        }

        @Override
        public void tick() {
            if (this.operation == MoveControl.Operation.MOVE_TO) {
                if (this.floatDuration-- <= 0) {
                    this.floatDuration += this.ghast.getRandom().nextInt(5) + 2;
                    Vec3 vec3 = new Vec3(this.wantedX - this.ghast.getX(), this.wantedY - this.ghast.getY(), this.wantedZ - this.ghast.getZ());
                    double d = vec3.length();
                    vec3 = vec3.normalize();
                    if (this.canReach(vec3, Mth.ceil(d))) {
                        this.ghast.setDeltaMovement(this.ghast.getDeltaMovement().add(vec3.scale(0.1D)));
                    } else {
                        this.operation = MoveControl.Operation.WAIT;
                    }
                }

            }
        }

        private boolean canReach(Vec3 vec3, int i) {
            AABB aABB = this.ghast.getBoundingBox();

            for (int j = 1; j < i; ++j) {
                aABB = aABB.move(vec3);
                if (!this.ghast.level.noCollision(this.ghast, aABB)) {
                    return false;
                }
            }

            return true;
        }
    }

    private static class GhastShootFireballGoal extends Goal {
        private final KurzeEntity ghast;

        public GhastShootFireballGoal(KurzeEntity ghast) {
            this.ghast = ghast;
        }

        @Override
        public boolean canUse() {
            return this.ghast.getTarget() != null;
        }

        @Override
        public void tick() {
            LivingEntity livingEntity = this.ghast.getTarget();
            if (livingEntity.distanceToSqr(this.ghast) < 4096.0D && this.ghast.hasLineOfSight(livingEntity)) {
                Level level = this.ghast.level;
                if (this.ghast.tickCount % 80 == 0) {
                    spawnFallingBlock(HundredDay.FALLING_OBSIDIAN.defaultBlockState(), level, livingEntity.blockPosition().above(2));
                    spawnFallingBlock(HundredDay.FALLING_OBSIDIAN.defaultBlockState(), level, livingEntity.blockPosition().above(2).west());
                    spawnFallingBlock(HundredDay.FALLING_OBSIDIAN.defaultBlockState(), level, livingEntity.blockPosition().above(2).south());
                    spawnFallingBlock(HundredDay.FALLING_OBSIDIAN.defaultBlockState(), level, livingEntity.blockPosition().above(2).north());
                    spawnFallingBlock(HundredDay.FALLING_OBSIDIAN.defaultBlockState(), level, livingEntity.blockPosition().above(2).east());
                }
            }
        }
    }

    @Override
    public boolean canBeLeashed(Player player) {
        return super.canBeLeashed(player);
    }
}


