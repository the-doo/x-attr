package com.doo.xattr.events;

import com.google.common.collect.Multimap;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.ItemStack;

import javax.annotation.Nullable;
import java.util.Arrays;

/**
 * Item is damaged
 */
public interface ItemApi {

    Event<OpDamage> ADD_MAX_DAMAGE = EventFactory.createArrayBacked(OpDamage.class,
            callback -> stack -> Arrays.stream(callback).mapToDouble(c -> c.op(stack)).sum());

    Event<OpDamage> TOTAL_MAX_DAMAGE = EventFactory.createArrayBacked(OpDamage.class,
            callback -> stack -> Arrays.stream(callback).mapToDouble(c -> c.op(stack)).sum());

    @FunctionalInterface
    interface OpDamage {
        double op(ItemStack stack);
    }

    Event<OpModifier> OP_MODIFIER = EventFactory.createArrayBacked(OpModifier.class,
            callback -> (map, stack, slot) -> Arrays.stream(callback).forEach(c -> c.mod(map, stack, slot)));

    @FunctionalInterface
    interface OpModifier {
        void mod(Multimap<Attribute, AttributeModifier> map, ItemStack stack, EquipmentSlot slot);
    }

    Event<WillDamage> WILL_DAMAGE = EventFactory.createArrayBacked(WillDamage.class,
            callback -> (owner, stack, amount) -> Arrays.stream(callback).forEach(c -> c.call(owner, stack, amount)));

    @FunctionalInterface
    interface WillDamage {
        void call(@Nullable LivingEntity owner, ItemStack stack, float amount);
    }
}
