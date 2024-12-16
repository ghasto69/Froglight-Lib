package com.ghasto.froglight.mixin;

import com.ghasto.froglight.item.ItemGroupEntries;
import com.ghasto.froglight.registry.FroglightRegistrate;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.registry.RegistryKey;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Optional;
import java.util.Set;

@Mixin(Item.class)
public class ItemMixin {
    @Inject(at = @At("TAIL"), method = "<init>")
    private void init(Item.Settings settings, CallbackInfo ci) {
        if(!settings.getGroup().isEmpty())
            ItemGroupEntries.addItemToGroup(settings.getGroup().get(), (Item) (Object) this);
    }
}
