package com.ghasto.froglight.registry;

import com.ghasto.froglight.util.DynamicRegistryContext;
import net.minecraft.core.RegistrationInfo;
import net.minecraft.core.Registry;
import net.minecraft.core.WritableRegistry;
import net.minecraft.resources.RegistryOps;
import net.minecraft.resources.ResourceKey;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class FroglightDynamicRegistry {
    //if only we could <T> Map<ResourceKey<Registry<T>>, Map<ResourceKey<T>, Function<DynamicRegistryContext, T>>>
    private static final Map<ResourceKey<?>, Key2FunctionMap<?>> MAP = new HashMap<>();

    public static <T> void register(ResourceKey<Registry<T>> registry, ResourceKey<T> key, Function<DynamicRegistryContext, T> function) {
        Key2FunctionMap<T> map = getOrCreateRegistry(registry);
        if(map.containsKey(key)) {
            throw new RuntimeException("Key "+key.location()+" has already been registered to registry "+registry.location());
        } else {
            map.put(key, function);
        }
    }

    @SuppressWarnings("unchecked")
    public static <T> void registerAll(WritableRegistry<T> writableRegistry, RegistryOps.RegistryInfoLookup lookup) {
        DynamicRegistryContext context = new DynamicRegistryContext(lookup);
        MAP.entrySet().stream()
                .filter(entry -> entry.getKey() == writableRegistry.key()) // Filter by matching registry key
                .findFirst() // Stop after finding the first matching registry
                .ifPresent(entry -> entry.getValue().forEach((key, value) -> {
                    if (((ResourceKey<T>) key).registryKey() == writableRegistry.key()) {
                        writableRegistry.register(
                                (ResourceKey<T>) key,
                                ((Function<DynamicRegistryContext, T>) value).apply(context),
                                RegistrationInfo.BUILT_IN
                        );
                    }
                }));
    }

    @SuppressWarnings("unchecked")
    public static <T> Key2FunctionMap<T> getOrCreateRegistry(ResourceKey<Registry<T>> registry) {
        return (Key2FunctionMap<T>) MAP.computeIfAbsent(registry, k -> new Key2FunctionMap<>());
    }

    public static class Key2FunctionMap<T> extends HashMap<ResourceKey<T>, Function<DynamicRegistryContext, T>> {}
}