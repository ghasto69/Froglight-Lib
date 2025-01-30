package com.ghasto.froglight.util;

import net.minecraft.core.HolderGetter;
import net.minecraft.core.Registry;
import net.minecraft.resources.RegistryOps;
import net.minecraft.resources.ResourceKey;

public record DynamicRegistryContext(RegistryOps.RegistryInfoLookup infoLookup) {
    public <T> HolderGetter<T> lookup(ResourceKey<Registry<T>> registry)  {
        return this.infoLookup.lookup(registry).orElseThrow().getter();
    }
}
