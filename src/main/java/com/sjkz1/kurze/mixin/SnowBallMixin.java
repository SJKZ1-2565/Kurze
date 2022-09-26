package com.sjkz1.kurze.mixin;

import com.sjkz1.kurze.entity.KurzeEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.projectile.Snowball;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Snowball.class)
public abstract class SnowBallMixin
        extends ThrowableItemProjectile {
    public SnowBallMixin(EntityType<? extends ThrowableItemProjectile> entityType, Level level) {
        super(entityType, level);
    }

    @Inject(method = "onHitEntity", at = @At("TAIL"))
    public void onHitEntity(EntityHitResult entityHitResult, CallbackInfo ci) {
        Entity entity = entityHitResult.getEntity();
        if (entity instanceof KurzeEntity) {
            if (((KurzeEntity) entity).getTorpor() >= 100) return;
            ((KurzeEntity) entity).setTorpor(((KurzeEntity) entity).getTorpor() + 1);
        }
    }
}
