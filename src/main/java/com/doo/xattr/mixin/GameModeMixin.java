package com.doo.xattr.mixin;

import com.doo.xattr.XAttr;
import com.doo.xattr.attribute.ExAttributes;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.MultiPlayerGameMode;
import net.minecraft.client.player.LocalPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Environment(EnvType.CLIENT)
@Mixin(MultiPlayerGameMode.class)
public abstract class GameModeMixin {

    @Inject(at = @At("RETURN"), method = "getPickRange", cancellable = true)
    public void opReachValue(CallbackInfoReturnable<Float> cir) {
        LocalPlayer player = Minecraft.getInstance().player;
        if (player != null) {
            cir.setReturnValue((float) XAttr.opValue(cir.getReturnValue(), player.getAttribute(ExAttributes.Hurt.REACH)));
        }
    }
}
