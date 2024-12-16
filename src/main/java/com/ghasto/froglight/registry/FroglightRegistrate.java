package com.ghasto.froglight.registry;

import com.ghasto.froglight.FroglightLib;
import com.mojang.serialization.MapCodec;
import net.fabricmc.fabric.api.item.v1.FabricItem;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.fabricmc.fabric.api.registry.FabricBrewingRecipeRegistryBuilder;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactories;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.component.ComponentType;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.effect.EnchantmentEntityEffect;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.potion.Potion;
import net.minecraft.recipe.BrewingRecipeRegistry;
import net.minecraft.registry.*;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

import java.util.*;
import java.util.function.BiFunction;
import java.util.function.Consumer;
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

    public <T extends BlockEntity> void blockEntityRenderer(BlockEntityType<T> blockEntityType, BlockEntityRendererFactory<T> blockEntityRenderer) {
        BlockEntityRendererFactories.register(blockEntityType, blockEntityRenderer);
    }

    /* Entity Registry Helpers */
    public EntityType<?> entity(String name, SpawnGroup spawnGroup, Function<EntityType.Builder<?>, EntityType.Builder<?>> factory) {
        return register(name, Registries.ENTITY_TYPE, factory.apply(EntityType.Builder.create(spawnGroup)).build(key(name, RegistryKeys.ENTITY_TYPE)));
    }

    /* Item Group Registry Helpers */
    public RegistryKey<ItemGroup> itemGroup(String name, Function<ItemGroup.Builder, ItemGroup.Builder> factory) {
        register(name, Registries.ITEM_GROUP, factory.apply(FabricItemGroup.builder().displayName(Text.translatable("itemGroup." + name))).build());
        return key(name, RegistryKeys.ITEM_GROUP);
    }

    /* Enchantment Registry Helpers */
    public void enchantment(Enchantment.Definition definition, Function<Enchantment.Builder, Enchantment.Builder> factory, Registerable<Enchantment> registry, RegistryKey<Enchantment> key) {
        registry.register(key, factory.apply(new Enchantment.Builder(definition)).build(key.getValue()));
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

    public void potionRecipes(Consumer<BrewingRecipeRegistry.Builder> factory) {
        FabricBrewingRecipeRegistryBuilder.BUILD.register(factory::accept);
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
