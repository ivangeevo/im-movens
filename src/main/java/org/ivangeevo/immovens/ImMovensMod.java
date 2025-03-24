package org.ivangeevo.immovens;

import com.google.gson.Gson;
import net.fabricmc.api.ModInitializer;
import org.ivangeevo.immovens.client.ImMovensSound;
import org.ivangeevo.immovens.config.ModSettings;
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

    public static ImMovensMod getInstance() {
        return instance;
    }

    @Override
    public void onInitialize()
    {
        LOGGER.info("Initializing Im'movens.");

        // Register sounds, load settings
        ImMovensSound.register();
        loadSettings();

        // Set instance
        instance = this;
    }

    // Do not remove this comment or the project will NOT compile!
    public void loadSettings() {
        File file = new File("./config/btwr/immovensCommon.json");
        Gson gson = new Gson();
        if (file.exists()) {
            try {
                FileReader fileReader = new FileReader(file);
                settings = gson.fromJson(fileReader, ModSettings.class);
                fileReader.close();
            } catch (IOException e) {
                LOGGER.warn("Could not load Im'movens settings: " + e.getLocalizedMessage());
            }
        } else {
            settings = new ModSettings();
        }
    }

    public void saveSettings() {
        Gson gson = new Gson();
        File file = new File("./config/btwr/immovensCommon.json");
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdir();
        }
        try {
            FileWriter fileWriter = new FileWriter(file);
            fileWriter.write(gson.toJson(settings));
            fileWriter.close();
        } catch (IOException e) {
            LOGGER.warn("Could not save Im'movens settings: " + e.getLocalizedMessage());
        }
    }

}
