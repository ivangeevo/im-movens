package org.ivangeevo.im_movens;

import com.google.gson.Gson;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import org.ivangeevo.im_movens.config.ModSettings;
import org.ivangeevo.im_movens.sound.ImMovensSound;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class ImMovensMod implements ModInitializer
{
    public static final String MOD_ID = "im_movens";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    private static ImMovensMod instance;
    public ModSettings settings;

    public static boolean isHungerGranular = false;

    private static final String CONFIG_FILE_LOCATION = "./config/btwr/immovensCommon.json";

    /**
     * Getter for current ImMovensMod instance
     */
    public static ImMovensMod getInstance() {
        return instance;
    }

    /**
     * Getter for ModSettings instance
     */
    public static ModSettings getSettings() {
        return getInstance().settings;
    }

    @Override
    public void onInitialize() {
        LOGGER.info("Initializing Im'movens.");

        // Register sounds
        ImMovensSound.register();
        // Load config settings
        loadSettings();

        //this is done to reduce unneeded compute cost
        if (FabricLoader.getInstance().isModLoaded("granular_hunger")) {
            isHungerGranular = true;
        }
        // Set instance
        instance = this;
    }

    public void loadSettings() {
        File file = new File(CONFIG_FILE_LOCATION);
        Gson gson = new Gson();
        if (file.exists()) {
            try {
                FileReader fileReader = new FileReader(file);
                settings = gson.fromJson(fileReader, ModSettings.class);
                fileReader.close();
            } catch (IOException e) {
                LOGGER.warn("Could not load Im 'movens settings: " + e.getLocalizedMessage());
            }
        } else {
            settings = new ModSettings();
        }
    }

    public void saveSettings() {
        Gson gson = new Gson();
        File file = new File(CONFIG_FILE_LOCATION);
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdir();
        }
        try {
            FileWriter fileWriter = new FileWriter(file);
            fileWriter.write(gson.toJson(settings));
            fileWriter.close();
        } catch (IOException e) {
            LOGGER.warn("Could not save Im 'movens settings: " + e.getLocalizedMessage());
        }
    }

}
