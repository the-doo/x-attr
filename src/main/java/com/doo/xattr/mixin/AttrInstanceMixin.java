package com.doo.xattr.mixin;

import com.doo.xattr.interfaces.Cacheable;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Environment(EnvType.CLIENT)
@Mixin(AttributeInstance.class)
public abstract class AttrInstanceMixin implements Cacheable {

    @Shadow
    private boolean dirty;

    @Shadow
    protected abstract double calculateValue();

    private double cachedAdd;

    private double cachedTotal;

    @ModifyVariable(at = @At(value = "STORE", ordinal = 1), method = "calculateValue", ordinal = 1)
    public double saveCachedAdd(double e) {
        return cachedAdd = e;
    }

    @Override
    public double getAdd() {
        if (dirty) {
            calculateValue();
        }
        return cachedAdd;
    }

    @Override
    public double getMulti() {
        if (dirty) {
            calculateValue();
        }
        return cachedTotal;
    }
}
