package com.ghasto.froglight.mixin;

import com.ghasto.froglight.item.FroglightItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.registry.RegistryKey;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

import java.util.Optional;

@Mixin(Item.Settings.class)
public class ItemSettingsMixin implements FroglightItem.Settings {
    @Unique
    private Optional<RegistryKey<ItemGroup>> group = Optional.empty();

    @Override
    public Item.Settings group(RegistryKey<ItemGroup> group) {
        this.group = Optional.of(group);
        return (Item.Settings) (Object) this;
    }

    @Override
    public Optional<RegistryKey<ItemGroup>> getGroup() {
        return this.group;
    }
}
