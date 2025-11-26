package org.ivangeevo.im_movens.datagen.provider;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.DataWriter;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.CompletableFuture;

/**
 * Base class for generating JSON files for Fabric data gen.
 */
public abstract class AbstractJsonProvider implements DataProvider {

    protected final FabricDataOutput output;

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    public AbstractJsonProvider(FabricDataOutput output) {
        this.output = output;
    }

    /**
     * Returns the relative path for the JSON file to generate.
     * E.g., "assets/mymod/sounds1.json"
     */
    protected abstract Path getOutputPath();

    /**
     * Builds the JSON object to write.
     */
    protected abstract JsonElement generateJson();

    @Override
    public CompletableFuture<?> run(DataWriter writer) {
        Path path = getOutputPath();

        try {
            Files.createDirectories(path.getParent());
        } catch (IOException e) {
            throw new RuntimeException("Failed to create directories for " + path, e);
        }

        return DataProvider.writeToPath(writer, generateJson(), path);
    }

    @Override
    public String getName() {
        return this.getClass().getSimpleName();
    }

    protected static Gson gson() {
        return GSON;
    }

}