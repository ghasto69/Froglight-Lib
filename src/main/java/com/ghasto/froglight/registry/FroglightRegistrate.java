package com.ghasto.froglight.registry;

import com.ghasto.froglight.FroglightLib;
import com.mojang.serialization.MapCodec;
import net.fabricmc.fabric.api.item.v1.FabricItem;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.fabricmc.fabric.api.registry.FabricBrewingRecipeRegistryBuilder;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.component.ComponentType;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.effect.EnchantmentEntityEffect;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.potion.Potion;
import net.minecraft.registry.*;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.UnaryOperator;

public class FroglightRegistrate {
    private final String modID;
    private Optional<Item.Settings> defaultItemSettings = Optional.empty();
    private Optional<AbstractBlock.Settings> defaultBlockSettings = Optional.empty();

    public FroglightRegistrate(String modID) {
        this.modID = modID;
    }

    public void setDefaultItemSettings(Item.Settings settings) {
        this.defaultItemSettings = Optional.of(settings);
    }

    public void setDefaultBlockSettings(AbstractBlock.Settings settings) {
        this.defaultBlockSettings = Optional.of(settings);
    }

    /* Item Registry Helpers */
    public Item item(String name, Function<Item.Settings, Item> factory) {
        return register(name, Registries.ITEM, factory.apply(
                defaultItemSettings.orElse(new Item.Settings())
                        .registryKey(key(name, RegistryKeys.ITEM))
        ));
    }

    /* Block Registry Helpers */
    public Block block(String name, Function<AbstractBlock.Settings, Block> blockFactory) {
        return register(name, Registries.BLOCK, blockFactory.apply(
                defaultBlockSettings.orElse(AbstractBlock.Settings.create())
                        .registryKey(key(name, RegistryKeys.BLOCK))
        ));
    }

    public Block block(String name, Function<AbstractBlock.Settings, Block> blockFactory, BiFunction<Block, Item.Settings, Item> itemFactory) {
        Block block = block(name, blockFactory);
        register(name, Registries.ITEM, itemFactory.apply(block, defaultItemSettings.orElse(new Item.Settings())
                .registryKey(key(name, RegistryKeys.ITEM))));
        return block;
    }

    /* Block Entity Registry Helpers */
    public BlockEntityType<?> blockEntityType(String name, BlockEntityType<?> blockEntityType) {
        return register(name, Registries.BLOCK_ENTITY_TYPE, blockEntityType);
    }

    /* Item Group Registry Helpers */
    public RegistryKey<ItemGroup> itemGroup(String name, ItemGroup itemGroup) {
        register(name, Registries.ITEM_GROUP, itemGroup);
        return key(name, RegistryKeys.ITEM_GROUP);
    }

    /* Enchantment Registry Helpers */
    public void enchantment(Registerable<Enchantment> registry, RegistryKey<Enchantment> key, Enchantment.Builder builder) {
        registry.register(key, builder.build(key.getValue()));
    }

    public MapCodec<? extends EnchantmentEntityEffect> enchantmentEffect(String name, MapCodec<? extends EnchantmentEntityEffect> codec) {
        return register(name, Registries.ENCHANTMENT_ENTITY_EFFECT_TYPE, codec);
    }

    /* Data Components */
    public ComponentType<?> enchantmentComponent(String name, UnaryOperator<ComponentType.Builder<?>> builderOperator) {
        return register(name, Registries.ENCHANTMENT_EFFECT_COMPONENT_TYPE, builderOperator.apply(ComponentType.builder()).build());
    }

    /* Potions and Effects */
    public Potion potion(String name, Potion potion) {
        return register(name, Registries.POTION, potion);
    }

    public void potionRecipe(RegistryEntry<Potion> input, Item ingredient, RegistryEntry<Potion> output) {
        FabricBrewingRecipeRegistryBuilder.BUILD.register(builder ->
                builder.registerPotionRecipe(input, ingredient, output)
        );
    }

    public StatusEffect effect(String name, StatusEffect effect) {
        return register(name, Registries.STATUS_EFFECT, effect);
    }

    /* Registry Helpers */
    public <T> RegistryKey<T> key(String name, RegistryKey<Registry<T>> registry) {
        return RegistryKey.of(registry, asID(name));
    }

    public <T> T register(String name, Registry<T> registry, T entry) {
        return Registry.register(registry, asID(name), entry);
    }

    public Identifier asID(String id) {
        return Identifier.of(modID, id);
    }

}
