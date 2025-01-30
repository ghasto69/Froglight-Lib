package com.ghasto.froglight.mixin;

import com.ghasto.froglight.item.FroglightItemProperties;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

import java.util.Optional;

@Mixin(Item.Properties.class)
public class ItemPropertiesMixin implements FroglightItemProperties {
    @Unique
    private ResourceKey<CreativeModeTab> tab;

    @Unique
    private Integer burnTime;

    @Override
    public Item.Properties froglight_lib$tab(ResourceKey<CreativeModeTab> tab) {
        this.tab = tab;
        return (Item.Properties) (Object) this;
    }

    @Override
    public Optional<ResourceKey<CreativeModeTab>> froglight_lib$getTab() {
        return this.tab == null ? Optional.empty() : Optional.of(this.tab);
    }

    @Override
    public Item.Properties froglight_lib$fuel(int burnTime) {
        this.burnTime = burnTime;
        return (Item.Properties) (Object) this;
    }

    @Override
    public Optional<Integer> froglight_lib$getBurnTime() {
        return this.burnTime == null ? Optional.empty() : Optional.of(this.burnTime);
    }
}
