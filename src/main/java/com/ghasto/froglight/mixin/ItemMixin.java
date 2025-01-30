package com.ghasto.froglight.mixin;

import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.fabricmc.fabric.api.registry.FuelRegistry;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Item.class)
public class ItemMixin {
    @Inject(at = @At("TAIL"), method = "<init>")
    private void init(Item.Properties settings, CallbackInfo ci) {
        settings.froglight_lib$getBurnTime().ifPresent(burnTime -> FuelRegistry.INSTANCE.add((ItemLike) this, burnTime));
        settings.froglight_lib$getTab().ifPresent(itemGroup -> ItemGroupEvents.modifyEntriesEvent(itemGroup).register(entries -> entries.accept((ItemLike) this)));
    }
}
