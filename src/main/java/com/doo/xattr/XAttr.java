package com.doo.xattr;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;

public class XAttr implements ModInitializer {

    public static final String ID = "x_attr";

    public static boolean tweaker = false;

    @Override
    public void onInitialize() {
        // has craft tweaker
        tweaker = FabricLoader.getInstance().isModLoaded("crafttweaker");
    }

    @SuppressWarnings("unchecked")
    public static <T> T get(Object o) {
        return (T) o;
    }
}
