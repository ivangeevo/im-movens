package org.ivangeevo.im_movens;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import org.ivangeevo.im_movens.config.ImMovensConfig;
import org.ivangeevo.im_movens.sound.ImMovensSoundEvents;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ImMovensMod implements ModInitializer {

    public static final String MOD_ID = "im_movens";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    public static boolean isHungerGranular = false;

    @Override
    public void onInitialize() {
        LOGGER.info("Initializing Im'movens.");

        // Register config
        ImMovensConfig.register();

        // Register sounds
        ImMovensSoundEvents.register();

        //this is done to reduce unneeded compute cost
        if (FabricLoader.getInstance().isModLoaded("granular_hunger")) {
            isHungerGranular = true;
        }
    }

}