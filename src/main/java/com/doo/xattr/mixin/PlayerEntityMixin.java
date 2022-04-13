package com.doo.xattr.mixin;

import com.doo.xattr.XAttr;
import com.doo.xattr.attribute.ExAttributes;
import com.doo.xattr.events.LivingApi;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.AABB;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Player.class)
public abstract class PlayerEntityMixin {

    private boolean isCritAttack = false;

    @ModifyVariable(method = "attack", at = @At(value = "STORE", ordinal = 1), ordinal = 2)
    private boolean opCritRate(boolean isCrit, Entity entity) {
        isCrit = isCrit || Math.random() <= XAttr.opValue(0, ((LivingEntity) XAttr.get(this)).getAttribute(ExAttributes.Living.Hurt.CRIT_RATE)) / 100;
        return isCritAttack = isCrit;
    }

    @ModifyConstant(method = "attack", constant = @Constant(floatValue = 1.5F))
    private float opCritValue(float critValue) {
        return (float) (critValue + XAttr.opValue(0, ((LivingEntity) XAttr.get(this)).getAttribute(ExAttributes.Living.Hurt.CRIT_VALUE)));
    }

    @ModifyVariable(method = "attack", at = @At(value = "STORE", ordinal = 4), ordinal = 4)
    private float opSweepingValue(float sweepingHurt) {
        return (int) XAttr.opValue(sweepingHurt, ((LivingEntity) XAttr.get(this)).getAttribute(ExAttributes.Living.Hurt.SWEEPING_VALUE));
    }

    @ModifyArg(method = "attack", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/Level;getEntitiesOfClass(Ljava/lang/Class;Lnet/minecraft/world/phys/AABB;)Ljava/util/List;"))
    private AABB opSweepingRange(AABB box) {
        double value = (int) XAttr.opValue(0, ((LivingEntity) XAttr.get(this)).getAttribute(ExAttributes.Living.Hurt.SWEEPING_RANGE));
        return box.inflate(value, 0, value);
    }

    @ModifyVariable(method = "giveExperiencePoints", at = @At("HEAD"), argsOnly = true)
    private int opXP(int i) {
        return (int) XAttr.opValue(i, ((LivingEntity) XAttr.get(this)).getAttribute(ExAttributes.Living.XP));
    }

    @ModifyVariable(method = "actuallyHurt", at = @At(value = "STORE", ordinal = 1), argsOnly = true)
    private float opHurtRealValue(float amount, DamageSource source) {
        return (float) XAttr.opValue(amount, ((LivingEntity) XAttr.get(this)).getAttribute(ExAttributes.Living.Hurt.REAL_VALUE));
    }

    @Inject(method = "actuallyHurt", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/damagesource/CombatTracker;recordDamage(Lnet/minecraft/world/damagesource/DamageSource;FF)V"))
    private void onHurt(DamageSource source, float amount, CallbackInfo ci) {
        LivingApi.Hurt.ON_DAMAGED.invoker().call(source, source.getEntity(), XAttr.get(this), amount, isCritAttack);
    }
}
