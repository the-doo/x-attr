package com.doo.xattr.mixin;

import com.doo.xattr.XAttr;
import com.doo.xattr.events.CraftApi;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.GrindstoneScreenHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

import java.util.Map;

@Mixin(GrindstoneScreenHandler.class)
public abstract class GrindstoneScreenHandlerMixin {

    @ModifyArg(at = @At(value = "INVOKE", target = "Lnet/minecraft/enchantment/EnchantmentHelper;set(Ljava/util/Map;Lnet/minecraft/item/ItemStack;)V"), method = "grind")
    public Map<Enchantment, Integer> enchantmentOnGrinds(Map<Enchantment, Integer> enchantments, ItemStack stack) {
        GrindstoneScreenHandler handler = XAttr.get(this);
        CraftApi.ON_GRIND_ENCHANTMENT.invoker().handle(enchantments, handler.slots.get(0).getStack(), handler.slots.get(1).getStack(), stack);
        return enchantments;
    }
}
