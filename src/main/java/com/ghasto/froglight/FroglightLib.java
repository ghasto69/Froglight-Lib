package com.ghasto.froglight;

import com.ghasto.froglight.registry.FroglightDynamicRegistry;
import com.ghasto.froglight.registry.FroglightRegistry;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.item.enchantment.Enchantment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SuppressWarnings("unused")
public class FroglightLib implements ModInitializer {
    public static final String MOD_ID = "froglight-lib";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
    public static final FroglightRegistry REGISTRATE = new FroglightRegistry(MOD_ID);
    public static final ResourceKey<Enchantment> TEST_ENCHANTMENT = REGISTRATE.key("test_enchantment", Registries.ENCHANTMENT);

    @Override
    public void onInitialize() {
        if (FabricLoader.getInstance().isDevelopmentEnvironment()) {
            FroglightDynamicRegistry.register(Registries.ENCHANTMENT, TEST_ENCHANTMENT, context -> Enchantment.enchantment(
                    Enchantment.definition(
                            context.lookup(Registries.ITEM).getOrThrow(ItemTags.ARMOR_ENCHANTABLE),
                            3,
                            2,
                            Enchantment.dynamicCost(1, 10),
                            Enchantment.dynamicCost(1, 15),
                            4,
                            EquipmentSlotGroup.ARMOR
                    )).build(TEST_ENCHANTMENT.location()));
        }
    }
}