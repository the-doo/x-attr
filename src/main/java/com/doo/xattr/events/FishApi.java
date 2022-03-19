package com.doo.xattr.events;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.entity.player.PlayerEntity;

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
        void onCaught(PlayerEntity player);
    }
}
