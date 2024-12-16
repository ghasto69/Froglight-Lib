package com.ghasto.froglight;

import com.ghasto.froglight.item.ItemGroupEntries;
import com.ghasto.froglight.registry.FroglightRegistrate;
import net.fabricmc.api.ModInitializer;

import net.minecraft.item.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FroglightLib implements ModInitializer {
	public static final String MOD_ID = "froglight-lib";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
	public static final FroglightRegistrate REGISTRATE = new FroglightRegistrate(MOD_ID);
	@Override
	public void onInitialize() {
		ItemGroupEntries.register();
	}
}