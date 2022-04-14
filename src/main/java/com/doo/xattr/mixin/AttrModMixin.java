package com.doo.xattr.mixin;

import com.doo.xattr.XAttr;
import com.doo.xattr.interfaces.Expirable;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Environment(EnvType.CLIENT)
@Mixin(AttributeModifier.class)
public abstract class AttrModMixin implements Expirable {

    private static final String RE_KEY = "remaining";

    private Long remaining;

    @Inject(at = @At("TAIL"), method = "save")
    public void saveRemaining(CallbackInfoReturnable<CompoundTag> cir) {
        if (remaining != null) {
            cir.getReturnValue().putLong(RE_KEY, remaining);
        }
    }

    @Inject(at = @At(value = "RETURN", ordinal = 0), method = "load")
    private static void loadRemaining(CompoundTag tag, CallbackInfoReturnable<AttributeModifier> cir) {
        if (tag.contains(RE_KEY)) {
            ((AttrModMixin) XAttr.get(cir.getReturnValue())).expired(tag.getLong(RE_KEY));
        }
    }

    @Override
    public void expired(long remaining) {
        this.remaining = remaining;
    }

    @Override
    public boolean isExpired() {
        return remaining != null && --remaining < 0;
    }
}
