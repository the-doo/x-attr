package com.doo.xattr.mixin;

import com.doo.xattr.XAttr;
import com.doo.xattr.events.CraftApi;
import net.minecraft.world.inventory.GrindstoneMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

import java.util.Map;

@Mixin(GrindstoneMenu.class)
public abstract class GrindstoneScreenHandlerMixin {

    @ModifyArg(at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/enchantment/EnchantmentHelper;setEnchantments(Ljava/util/Map;Lnet/minecraft/world/item/ItemStack;)V"), method = "removeNonCurses")
    public Map<Enchantment, Integer> enchantmentOnGrinds(Map<Enchantment, Integer> enchantments, ItemStack stack) {
        GrindstoneMenu handler = XAttr.get(this);
        CraftApi.ON_GRIND_ENCHANTMENT.invoker().handle(enchantments, handler.slots.get(0).getItem(), handler.slots.get(1).getItem(), stack);
        return enchantments;
    }
}
