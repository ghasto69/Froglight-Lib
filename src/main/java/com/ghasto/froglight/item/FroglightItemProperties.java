package com.ghasto.froglight.item;

import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;

import java.util.Optional;

@SuppressWarnings("unused")
public interface FroglightItemProperties {
    default Item.Properties froglight_lib$tab(ResourceKey<CreativeModeTab> group) {
        return new Item.Properties();
    }

    default Optional<ResourceKey<CreativeModeTab>> froglight_lib$getTab() {
        return Optional.empty();
    }

    default Item.Properties froglight_lib$fuel(int time) {
        return new Item.Properties();
    }

    default Optional<Integer> froglight_lib$getBurnTime() {
        return Optional.empty();
    }
}
