package com.doo.xattr.mixin;

import com.doo.xattr.XAttr;
import com.doo.xattr.events.ItemApi;
import com.google.common.collect.Multimap;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Random;

@Mixin(ItemStack.class)
public abstract class ItemStackMixin {

    @Inject(method = "damage(ILjava/util/Random;Lnet/minecraft/server/network/ServerPlayerEntity;)Z", at = @At(value = "INVOKE", target = "Lnet/minecraft/enchantment/EnchantmentHelper;getLevel(Lnet/minecraft/enchantment/Enchantment;Lnet/minecraft/item/ItemStack;)I"))
    private void usedCallback(int amount, Random random, ServerPlayerEntity player, CallbackInfoReturnable<Boolean> cir) {
        ItemApi.WILL_DAMAGE.invoker().call(player, XAttr.get(this), amount);
    }

    @ModifyVariable(method = "getAttributeModifiers", at = @At(value = "RETURN"))
    private Multimap<EntityAttribute, EntityAttributeModifier> opModifiers(Multimap<EntityAttribute, EntityAttributeModifier> map, EquipmentSlot slot) {
        ItemApi.OP_MODIFIER.invoker().mod(map, XAttr.get(this), slot);
        return map;
    }
}
