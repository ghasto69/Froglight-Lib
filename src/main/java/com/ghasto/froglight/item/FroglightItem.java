package com.ghasto.froglight.item;

import net.fabricmc.fabric.api.item.v1.FabricItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.registry.RegistryKey;

import java.util.Optional;

public interface FroglightItem {
    interface Settings {
        default Item.Settings group(RegistryKey<ItemGroup> group) {
            return new Item.Settings();
        }

        default Optional<RegistryKey<ItemGroup>> getGroup() {
            return Optional.empty();
        }
    }
}
