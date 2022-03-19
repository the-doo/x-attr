package com.doo.xattr.events;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;

import java.util.Arrays;
import java.util.Map;

/**
 * Entity get Armor - like EntityDamageApi
 */
public interface CraftApi {

    Event<OnGrind> ON_GRIND_ENCHANTMENT = EventFactory.createArrayBacked(OnGrind.class,
            callback -> ((map, first, second, result) -> Arrays.stream(callback).forEach(c -> c.handle(map, first, second, result))));

    Event<OnAnvil> ON_ANVIL_ENCHANTMENT = EventFactory.createArrayBacked(OnAnvil.class,
            callback -> ((player, map, first, second, result) -> Arrays.stream(callback).forEach(c -> c.handle(player, map, first, second, result))));

    @FunctionalInterface
    interface OnGrind {
        void handle(Map<Enchantment, Integer> map, ItemStack first, ItemStack second, ItemStack result);
    }

    @FunctionalInterface
    interface OnAnvil {
        void handle(PlayerEntity player, Map<Enchantment, Integer> map, ItemStack first, ItemStack second, ItemStack result);
    }
}
