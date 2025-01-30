package com.ghasto.froglight.registry;

import com.mojang.serialization.MapCodec;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.fabricmc.fabric.api.registry.FabricBrewingRecipeRegistryBuilder;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionBrewing;
import net.minecraft.world.item.enchantment.effects.EnchantmentEntityEffect;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Fluid;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.UnaryOperator;

@SuppressWarnings("unused")
public class FroglightRegistry {
    private final String modID;
    private Item.Properties defaultItemProperties;
    private BlockBehaviour.Properties defaultBlockProperties;

    public FroglightRegistry(String modID) {
        this.modID = modID;
    }

    public void setDefaultItemProperties(Item.Properties properties) {
        this.defaultItemProperties = properties;
    }

    public void setDefaultBlockProperties(BlockBehaviour.Properties properties) {
        this.defaultBlockProperties = properties;
    }

    /* Item Registry Helpers */
    public <T extends Item> T item(String name, Function<Item.Properties, T> factory) {
        return register(name, BuiltInRegistries.ITEM, factory.apply(
                Objects.requireNonNullElse(defaultItemProperties, new Item.Properties())
                        .setId(key(name, Registries.ITEM))
        ));
    }

    /* Block Registry Helpers */
    public <T extends Block> T block(String name, Function<BlockBehaviour.Properties, T> blockFactory) {
        return register(name, BuiltInRegistries.BLOCK, blockFactory.apply(
                Objects.requireNonNullElse(defaultBlockProperties, BlockBehaviour.Properties.of())
                        .setId(key(name, Registries.BLOCK))
        ));
    }

    public <T extends Block> T block(String name, Function<BlockBehaviour.Properties, T> blockFactory, BiFunction<T, Item.Settings, ? extends BlockItem> itemFactory) {
        T block = block(name, blockFactory);
        item(name, settings -> itemFactory.apply(block, settings));
        return block;
    }

    /* Fluids */
    public <T extends Fluid> T fluid(String name, T fluid) {
        return register(name, BuiltInRegistries.FLUID, fluid);
    }

    /* Block Entity Registry Helpers */
    public <T extends BlockEntity, R extends BlockEntityType<T>> R blockEntityType(String name, R blockEntityType) {
        return register(name, BuiltInRegistries.BLOCK_ENTITY_TYPE, blockEntityType);
    }

    public <T extends BlockEntity, R extends BlockEntityType<T>> R blockEntityType(String name, R blockEntityType, BlockEntityRendererProvider<T> renderer) {
        R blockEntity = blockEntityType(name, blockEntityType);
        blockEntityRenderer(blockEntity, renderer);
        return blockEntity;
    }

    public <T extends BlockEntity> void blockEntityRenderer(BlockEntityType<T> blockEntityType, BlockEntityRendererProvider<T> blockEntityRenderer) {
        BlockEntityRenderers.register(blockEntityType, blockEntityRenderer);
    }

    /* Entity Registry Helpers */
    public <T extends Entity> EntityType<T> entity(String name, MobCategory spawnGroup, EntityType.EntityFactory<T> entityFactory, Function<EntityType.Builder<T>, EntityType.Builder<T>> factory) {
        return register(name, BuiltInRegistries.ENTITY_TYPE, factory.apply(EntityType.Builder.of(entityFactory, spawnGroup)).build(key(name, Registries.ENTITY_TYPE)));
    }

    /* Item Group Registry Helpers */
    public ResourceKey<CreativeModeTab> itemGroup(String name, UnaryOperator<CreativeModeTab.Builder> factory) {
        register(name, BuiltInRegistries.CREATIVE_MODE_TAB, factory.apply(FabricItemGroup.builder().title(Component.translatable("itemGroup." + name))).build());
        return key(name, Registries.CREATIVE_MODE_TAB);
    }

    public <T extends EnchantmentEntityEffect> MapCodec<T> enchantmentEffect(String name, MapCodec<T> codec) {
        return register(name, BuiltInRegistries.ENCHANTMENT_ENTITY_EFFECT_TYPE, codec);
    }

    /* Data Components */
    public <T extends EnchantmentEntityEffect> DataComponentType<T> enchantmentComponent(String name, UnaryOperator<DataComponentType.Builder<T>> builderOperator) {
        return register(name, BuiltInRegistries.ENCHANTMENT_EFFECT_COMPONENT_TYPE, builderOperator.apply(DataComponentType.builder()).build());
    }

    /* Potions and Effects */
    public <T extends Potion> T potion(String name, T potion) {
        return register(name, BuiltInRegistries.POTION, potion);
    }

    public void potionRecipes(Consumer<PotionBrewing.Builder> factory) {
        FabricBrewingRecipeRegistryBuilder.BUILD.register(factory::accept);
    }

    public <T extends MobEffect> T effect(String name, T effect) {
        return register(name, BuiltInRegistries.MOB_EFFECT, effect);
    }

    /* Registry Helpers */
    public <T> ResourceKey<T> key(String name, ResourceKey<? extends Registry<T>> registry) {
        return ResourceKey.create(registry, asID(name));
    }

    public <T, R extends T> R register(String name, Registry<T> registry, R entry) {
        // Register the entry and return the specific type R
        return Registry.register(registry, asID(name), entry);
    }

    public ResourceLocation asID(String id) {
        return ResourceLocation.fromNamespaceAndPath(modID, id);
    }
}
