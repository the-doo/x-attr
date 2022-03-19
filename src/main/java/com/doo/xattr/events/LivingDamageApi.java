package com.doo.xattr.events;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;

import java.util.Arrays;

/**
 * Entity is damaged
 */
public interface LivingDamageApi {

    /**
     * Add damage amount - like EnchantmentHelper.getAttackDamage but not display on tooltips
     * <p>
     * 1 -> amount + 1
     * <p>
     * 5 -> amount + 5
     * <p>
     * -2 -> amount - 2
     */
    Event<OpDamageAmount> ADD = EventFactory.createArrayBacked(OpDamageAmount.class,
            callback -> ((source, attacker, target) -> (float) Arrays.stream(callback).mapToDouble(c -> c.get(source, attacker, target)).sum()));

    /**
     * Multiplier of total damage amount - return value in percentage
     * <p>
     * 100 -> total * (1 + 1)
     * <p>
     * 50 -> total * (1 + 0.5)
     * <p>
     * -20 -> total * (1 - 0.2)
     */
    Event<OpDamageAmount> MULTIPLIER = EventFactory.createArrayBacked(OpDamageAmount.class, callback ->
            ((source, attacker, target) -> (float) Arrays.stream(callback).mapToDouble(c -> c.get(source, attacker, target)).sum()));

    /**
     * Add real damage amount - It will add after check armor and resistance
     * <p>
     * 1 -> amount + 1
     * <p>
     * 5 -> amount + 5
     * <p>
     * -2 -> amount - 2
     */
    Event<OpDamageAmount> REAL_ADD = EventFactory.createArrayBacked(OpDamageAmount.class, callback ->
            ((source, attacker, target) -> (float) Arrays.stream(callback).mapToDouble(c -> c.get(source, attacker, target)).sum()));

    /**
     * Multiplier of total real damage amount - It will add after check armor and resistance
     * <p>
     * 100 -> total * (1 + 1)
     * <p>
     * 50 -> total * (1 + 0.5)
     * <p>
     * -20 -> total * (1 - 0.2)
     */
    Event<OpDamageAmount> REAL_MULTIPLIER = EventFactory.createArrayBacked(OpDamageAmount.class, callback ->
            ((source, attacker, target) -> (float) Arrays.stream(callback).mapToDouble(c -> c.get(source, attacker, target)).sum()));

    /**
     * Target health is changed
     * <p>
     * amount - value of target health is changed
     */
    Event<OnDamaged> ON_DAMAGED = EventFactory.createArrayBacked(OnDamaged.class, callback ->
            ((source, attacker, target, amount) -> Arrays.stream(callback).forEach(c -> c.call(source, attacker, target, amount))));

    @FunctionalInterface
    interface OpDamageAmount {
        float get(DamageSource source, Entity attacker, LivingEntity target);
    }

    @FunctionalInterface
    interface OnDamaged {
        void call(DamageSource source, Entity attacker, LivingEntity target, float amount);
    }

    static float damage(float amount, DamageSource source, LivingEntity target) {
        amount += LivingDamageApi.ADD.invoker().get(source, source.getAttacker(), target);
        if (amount <= 0) {
            return 0;
        }

        amount *= (1 + LivingDamageApi.MULTIPLIER.invoker().get(source, source.getAttacker(), target) / 100F);
        if (amount <= 0) {
            return 0;
        }
        return amount;
    }

    static float realDamage(float amount, DamageSource source, LivingEntity target) {
        amount += LivingDamageApi.REAL_ADD.invoker().get(source, source.getAttacker(), target);
        if (amount <= 0) {
            return 0;
        }

        amount *= (1 + LivingDamageApi.REAL_MULTIPLIER.invoker().get(source, source.getAttacker(), target) / 100F);
        if (amount <= 0) {
            return 0;
        }
        return amount;
    }
}
