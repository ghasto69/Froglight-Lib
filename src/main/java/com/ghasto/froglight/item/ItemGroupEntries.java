package com.ghasto.froglight.item;

import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.registry.RegistryKey;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ItemGroupEntries {
    private static final Map<RegistryKey<ItemGroup>, List<Item>> itemGroupMap = new HashMap<>();

    public static void addItemToGroup(RegistryKey<ItemGroup> group, Item item) {
        itemGroupMap.computeIfAbsent(group, k -> new ArrayList<>()).add(item);
    }

    public static void register() {
        itemGroupMap.forEach((k, v) -> {
            ItemGroupEvents.modifyEntriesEvent(k).register(
                    entries -> v.forEach(entries::add)
            );
        });
    }
}
