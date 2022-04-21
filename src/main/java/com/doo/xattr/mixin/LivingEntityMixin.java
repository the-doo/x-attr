package com.doo.xattr.mixin;

import com.doo.xattr.XAttr;
import com.doo.xattr.events.LivingApi;
import com.doo.xattr.interfaces.Expirable;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.EntityDamageSource;
import net.minecraft.world.damagesource.IndirectEntityDamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Set;
import java.util.stream.Collectors;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin {

    @Shadow
    protected int useItemRemaining;

    @Shadow
    public abstract AttributeMap getAttributes();

    @Inject(method = "tick", at = @At(value = "TAIL"))
    private void tickT(CallbackInfo ci) {
        // remove expired attr
        XAttr.get(getAttributes(), AttrMapAccessor.class).getAttributes().forEach((a, i) -> {
            Set<AttributeModifier> expired = i.getModifiers()
                    .stream()
                    .filter(m -> ((Expirable) XAttr.get(m)).isExpired())
                    .collect(Collectors.toSet());
            expired.forEach(i::removeModifier);
        });

        LivingEntity living = XAttr.get(this);
        if (!living.level.isClientSide()) {
            LivingApi.SEVER_TAIL_TICK.invoker().tick(living);
        }
    }

    @Inject(method = "updateUsingItem", at = @At(value = "HEAD"))
    private void tickActiveItemStackH(ItemStack stack, CallbackInfo ci) {
        // need check 1 to trigger next logic
        if (useItemRemaining > 1) {
            useItemRemaining = Math.max(1, LivingApi.useTime(useItemRemaining, XAttr.get(this), stack));
        }
    }

    @ModifyArg(method = "getArmorValue", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/Mth;floor(D)I"))
    private double armor(double value) {
        return LivingApi.Armor.armor(XAttr.get(this), value);
    }

    @ModifyVariable(method = "actuallyHurt", at = @At(value = "STORE", ordinal = 0), argsOnly = true)
    private float opHurtValue(float amount, DamageSource source) {
        LivingEntity target = XAttr.get(this);
        return (float) XAttr.opValue(amount,
                () -> LivingApi.Hurt.ADD.invoker().op(source, source.getEntity(), target),
                () -> LivingApi.Hurt.MULTIPLIER.invoker().op(source, source.getEntity(), target));
    }

    @ModifyVariable(method = "actuallyHurt", at = @At(value = "STORE", ordinal = 1), argsOnly = true)
    private float opHurtRealValue(float amount, DamageSource source) {
        LivingEntity target = XAttr.get(this);
        return (float) XAttr.opValue(amount,
                () -> LivingApi.Hurt.ADD.invoker().op(source, source.getEntity(), target),
                () -> LivingApi.Hurt.MULTIPLIER.invoker().op(source, source.getEntity(), target));
    }

    @Inject(method = "actuallyHurt", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/damagesource/CombatTracker;recordDamage(Lnet/minecraft/world/damagesource/DamageSource;FF)V"))
    private void onHurt(DamageSource source, float amount, CallbackInfo ci) {
        LivingApi.Hurt.ON_DAMAGED.invoker().call(source, source.getEntity(), XAttr.get(this), amount, false);
    }

    @Inject(method = "addEffect(Lnet/minecraft/world/effect/MobEffectInstance;Lnet/minecraft/world/entity/Entity;)Z", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/effect/MobEffectInstance;getEffect()Lnet/minecraft/world/effect/MobEffect;", ordinal = 0), cancellable = true)
    private void checkEffect(MobEffectInstance effect, Entity source, CallbackInfoReturnable<Boolean> cir) {
        if (LivingApi.IGNORED_APPLY_STATUS.invoker().ignored(XAttr.get(this), effect.getEffect(), source)) {
            cir.setReturnValue(true);
            cir.cancel();
        }
    }
}
