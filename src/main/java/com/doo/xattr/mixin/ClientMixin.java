package com.doo.xattr.mixin;

import com.doo.xattr.XAttr;
import com.doo.xattr.attribute.ExAttributes;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Environment(EnvType.CLIENT)
@Mixin(Minecraft.class)
public abstract class ClientMixin {

    @ModifyConstant(constant = @Constant(intValue = 10), method = "startAttack")
    public int opMissTime(int interval) {
        LocalPlayer player = Minecraft.getInstance().player;
        if (player != null) {
            return (int) XAttr.opValue(interval, player.getAttribute(ExAttributes.Living.Hurt.INTERVAL));
        }
        return interval;
    }
}
