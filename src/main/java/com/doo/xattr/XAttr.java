package com.doo.xattr;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;

import java.util.function.Supplier;

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

    @SuppressWarnings("unchecked")
    public static <T> T get(Object o, Class<T> t) {
        return (T) o;
    }

    /**
     * Operation Value
     *
     * @param base     base value
     * @param instance attribute instance
     * @return value after operation
     */
    public static double opValue(double base, AttributeInstance instance) {
        if (instance == null) {
            return base;
        }

        return base + instance.getValue();
    }

    /**
     * Operation Value
     *
     * @param base     base value
     * @param addition add value
     * @param total    multiplier value
     * @return value after operation
     */
    public static double opValue(double base, Supplier<Double> addition, Supplier<Double> total) {
        base += addition.get();
        if (base <= 0) {
            return 0;
        }

        base *= (1 + total.get() / 100F);
        if (base <= 0) {
            return 0;
        }
        return base;
    }
}
