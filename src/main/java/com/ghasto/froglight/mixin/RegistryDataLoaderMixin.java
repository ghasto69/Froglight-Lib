package com.ghasto.froglight.mixin;

import com.ghasto.froglight.registry.FroglightDynamicRegistry;
import com.mojang.serialization.Decoder;
import net.minecraft.core.WritableRegistry;
import net.minecraft.resources.RegistryDataLoader;
import net.minecraft.resources.RegistryOps;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.packs.resources.ResourceManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Map;

@Mixin(RegistryDataLoader.class)
public class RegistryDataLoaderMixin {
    @Inject(method = "loadContentsFromManager", at = @At("TAIL"))
    private static <T> void loadContentsFromManager(ResourceManager resourceManager, RegistryOps.RegistryInfoLookup lookup, WritableRegistry<T> dynamicRegistry, Decoder<T> decoder, Map<ResourceKey<T>, Exception> exceptions, CallbackInfo ci) {
        FroglightDynamicRegistry.registerAll(dynamicRegistry, lookup);
    }
}
