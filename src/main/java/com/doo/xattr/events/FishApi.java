package com.doo.xattr.events;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.world.entity.player.Player;

import java.util.Arrays;

/**
 * Fish Api
 */
public interface FishApi {

    /**
     * fish is caught
     */
    Event<OnCaught> ON_CAUGHT = EventFactory.createArrayBacked(OnCaught.class,
            call -> player -> Arrays.stream(call).forEach(l -> l.onCaught(player)));

    @FunctionalInterface
    interface OnCaught {
        void onCaught(Player player);
    }
}
