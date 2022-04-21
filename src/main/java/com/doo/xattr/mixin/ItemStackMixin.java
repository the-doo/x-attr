package com.doo.xattr.mixin;

import com.doo.xattr.XAttr;
import com.doo.xattr.events.ItemApi;
import com.google.common.collect.Multimap;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Random;

@Mixin(ItemStack.class)
public abstract class ItemStackMixin {

    @Inject(method = "getMaxDamage", at = @At(value = "RETURN"), cancellable = true)
    private void opMaxDamage(CallbackInfoReturnable<Integer> cir) {
        ItemStack stack = XAttr.get(this);
        cir.setReturnValue((int) XAttr.opValue(cir.getReturnValue(),
                () -> ItemApi.ADD_MAX_DAMAGE.invoker().op(stack),
                () -> ItemApi.TOTAL_MAX_DAMAGE.invoker().op(stack)));
    }

    @Inject(method = "hurt", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/enchantment/EnchantmentHelper;getItemEnchantmentLevel(Lnet/minecraft/world/item/enchantment/Enchantment;Lnet/minecraft/world/item/ItemStack;)I"))
    private void usedCallback(int amount, Random random, ServerPlayer player, CallbackInfoReturnable<Boolean> cir) {
        ItemApi.WILL_DAMAGE.invoker().call(player, XAttr.get(this), amount);
    }

    @ModifyVariable(method = "getAttributeModifiers", at = @At(value = "RETURN"))
    private Multimap<Attribute, AttributeModifier> opModifiers(Multimap<Attribute, AttributeModifier> map, EquipmentSlot slot) {
        ItemApi.OP_MODIFIER.invoker().mod(map, XAttr.get(this), slot);
        return map;
    }
}
