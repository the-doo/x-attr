package com.doo.xattr.events;

import com.doo.xattr.XAttr;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import java.util.Arrays;

/**
 * Living Entity Api
 */
public interface LivingApi {

    Event<ServerTailTick> SEVER_TAIL_TICK = EventFactory.createArrayBacked(ServerTailTick.class, callback -> living -> Arrays.stream(callback).forEach(c -> c.tick(living)));

    Event<OpUseTime> REDUCE_USE_TIME = EventFactory.createArrayBacked(OpUseTime.class,
            callback -> (living, stack) -> Arrays.stream(callback).mapToInt(c -> c.get(living, stack)).sum());

    Event<IgnoredApplyStatus> IGNORED_APPLY_STATUS = EventFactory.createArrayBacked(IgnoredApplyStatus.class,
            callback -> (living, effect, source) -> Arrays.stream(callback).anyMatch(c -> c.ignored(living, effect, source)));

    @FunctionalInterface
    interface ServerTailTick {
        void tick(LivingEntity living);
    }

    @FunctionalInterface
    interface OpUseTime {
        int get(LivingEntity living, ItemStack stack);
    }

    @FunctionalInterface
    interface IgnoredApplyStatus {
        boolean ignored(LivingEntity living, MobEffect effect, Entity source);
    }

    static int useTime(int time, LivingEntity entity, ItemStack stack) {
        return time - REDUCE_USE_TIME.invoker().get(entity, stack);
    }

    /**
     * about hurt value
     */
    interface Hurt {

        /**
         * Add damage amount - like EnchantmentHelper.getAttackDamage but not display on tooltips
         * <p>
         * 1 -> amount + 1
         * <p>
         * 5 -> amount + 5
         * <p>
         * -2 -> amount - 2
         */
        Event<HurtValue> ADD = EventFactory.createArrayBacked(HurtValue.class, callback ->
                (source, attacker, target) -> Arrays.stream(callback).mapToDouble(c -> c.op(source, attacker, target)).sum());

        /**
         * Multiplier of total damage amount - return value in percentage
         * <p>
         * 100 -> total * (1 + 1)
         * <p>
         * 50 -> total * (1 + 0.5)
         * <p>
         * -20 -> total * (1 - 0.2)
         */
        Event<HurtValue> MULTIPLIER = EventFactory.createArrayBacked(HurtValue.class, callback ->
                (source, attacker, target) -> Arrays.stream(callback).mapToDouble(c -> c.op(source, attacker, target)).sum());

        /**
         * Add real damage amount - It will add after check armor and resistance
         * <p>
         * 1 -> amount + 1
         * <p>
         * 5 -> amount + 5
         * <p>
         * -2 -> amount - 2
         */
        Event<HurtValue> REAL_ADD = EventFactory.createArrayBacked(HurtValue.class, callback ->
                (source, attacker, target) -> Arrays.stream(callback).mapToDouble(c -> c.op(source, attacker, target)).sum());

        /**
         * Multiplier of total real damage amount - It will add after check armor and resistance
         * <p>
         * 100 -> total * (1 + 1)
         * <p>
         * 50 -> total * (1 + 0.5)
         * <p>
         * -20 -> total * (1 - 0.2)
         */
        Event<HurtValue> REAL_MULTIPLIER = EventFactory.createArrayBacked(HurtValue.class, callback ->
                (source, attacker, target) -> Arrays.stream(callback).mapToDouble(c -> c.op(source, attacker, target)).sum());

        @FunctionalInterface
        interface HurtValue {
            double op(DamageSource source, Entity attacker, LivingEntity target);
        }

        /**
         * Target health is changed
         * <p>
         * amount - value of target health is changed
         */
        Event<OnHurt> ON_DAMAGED = EventFactory.createArrayBacked(OnHurt.class, callback ->
                ((source, attacker, target, amount, isCritAttack) -> Arrays.stream(callback).forEach(c -> c.call(source, attacker, target, amount, isCritAttack))));

        @FunctionalInterface
        interface OnHurt {
            void call(DamageSource source, Entity attacker, LivingEntity target, float amount, boolean isCritAttack);
        }

        /**
         * Attack is Crit
         */
        Event<IsCrit> IS_CRIT = EventFactory.createArrayBacked(IsCrit.class, callback ->
                (attacker, target) -> Arrays.stream(callback).anyMatch(c -> c.crit(attacker, target)));

        @FunctionalInterface
        interface IsCrit {
            boolean crit(Player attacker, Entity target);
        }
    }

    /**
     * about armor value
     */
    interface Armor {
        Event<OpArmor> ADD = EventFactory.createArrayBacked(OpArmor.class,
                callback -> (living, base) -> Arrays.stream(callback).mapToDouble(c -> c.get(living, base)).sum());

        Event<OpArmor> MULTIPLIER = EventFactory.createArrayBacked(OpArmor.class,
                callback -> (living, base) -> Arrays.stream(callback).mapToDouble(c -> c.get(living, base)).sum());

        @FunctionalInterface
        interface OpArmor {
            double get(LivingEntity living, double base);
        }

        static double armor(LivingEntity living, double base) {
            return XAttr.opValue(base, () -> ADD.invoker().get(living, base), () -> MULTIPLIER.invoker().get(living, base));
        }
    }
}
