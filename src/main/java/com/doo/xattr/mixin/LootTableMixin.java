package com.doo.xattr.mixin;

import com.doo.xattr.events.LootApi;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.context.LootContext;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import java.util.function.Consumer;

@Mixin(value = LootTable.class, priority = Integer.MAX_VALUE)
public abstract class LootTableMixin {

    @ModifyVariable(method = "generateLoot(Lnet/minecraft/loot/context/LootContext;Ljava/util/function/Consumer;)V", at = @At("LOAD"), argsOnly = true)
    public Consumer<ItemStack> changeLootConsumer(Consumer<ItemStack> lootConsumer, LootContext context) {
        // loot consumer point
        return LootApi.lootConsumer(lootConsumer, context);
    }
}
