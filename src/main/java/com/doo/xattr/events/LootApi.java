package com.doo.xattr.events;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.FishingBobberEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.context.LootContext;
import net.minecraft.loot.context.LootContextParameters;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * Looting Api
 */
public interface LootApi {

    /**
     * source from player's operation
     */
    Event<OpLoot> OP_LOOT = EventFactory.createArrayBacked(OpLoot.class, callback -> ((trigger, stack, base, context) ->
            Arrays.stream(callback).map(c -> c.handle(trigger, stack, base, context)).filter(Objects::nonNull).reduce(Function::andThen).orElse(null)));

    @FunctionalInterface
    interface OpLoot {
        @Nullable
        Function<ItemStack, ItemStack> handle(LivingEntity trigger, ItemStack stack, Consumer<ItemStack> baseConsumer, LootContext context);
    }

    static Consumer<ItemStack> lootConsumer(Consumer<ItemStack> lootConsumer, LootContext context) {
        // default is tool loot
        ItemStack stack = context.get(LootContextParameters.TOOL);
        Entity entity = Optional.ofNullable(context.get(LootContextParameters.KILLER_ENTITY)).orElse(context.get(LootContextParameters.THIS_ENTITY));

        // if it is attack loot, try to get on entity
        if (stack == null && entity instanceof LivingEntity) {
            stack = ((LivingEntity) entity).getMainHandStack();
        }

        // if it is rod loot, try to get owner
        if (entity instanceof FishingBobberEntity) {
            entity = ((FishingBobberEntity) entity).getOwner();
        }

        if (stack == null || stack.isEmpty() || !(entity instanceof LivingEntity)) {
            return lootConsumer;
        }

        Function<ItemStack, ItemStack> handle = LootApi.OP_LOOT.invoker().handle((LivingEntity) entity, stack, lootConsumer, context);
        if (handle == null) {
            return lootConsumer;
        }

//        return XAttr.tweaker ? LootCapturingConsumer.of(lootConsumer.andThen(handle::apply)) : lootConsumer.andThen(handle::apply);
        return lootConsumer.andThen(handle::apply);
    }
}
