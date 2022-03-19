package com.doo.xattr.events;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.entity.LivingEntity;

import java.util.Arrays;

/**
 * Entity get Armor - like EntityDamageApi
 */
public interface EntityArmorApi {

    Event<OpArmor> ADD = EventFactory.createArrayBacked(OpArmor.class,
            callback -> (living, base) -> (float) Arrays.stream(callback).mapToDouble(c -> c.get(living, base)).sum());

    Event<OpArmor> MULTIPLIER = EventFactory.createArrayBacked(OpArmor.class,
            callback -> (living, base) -> (float) Arrays.stream(callback).mapToDouble(c -> c.get(living, base)).sum());

    @FunctionalInterface
    interface OpArmor {
        float get(LivingEntity living, double base);
    }

    static double armor(double base, LivingEntity living) {
        base += EntityArmorApi.ADD.invoker().get(living, base);
        if (base <= 0) {
            return 0;
        }

        base *= (1 + EntityArmorApi.MULTIPLIER.invoker().get(living, base) / 100F);
        if (base <= 0) {
            return 0;
        }
        return base;
    }
}
