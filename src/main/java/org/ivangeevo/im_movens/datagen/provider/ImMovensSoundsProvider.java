package org.ivangeevo.im_movens.datagen.provider;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import org.ivangeevo.im_movens.ImMovensMod;

import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Generates sounds1.json for the mod.
 */
public class ImMovensSoundsProvider extends AbstractJsonProvider {

    /** Replace with your mod_id for easier adaptation **/
    private static final String MOD_ID = ImMovensMod.MOD_ID;

    private final Map<String, SoundEntry> sounds = new LinkedHashMap<>();

    public ImMovensSoundsProvider(FabricDataOutput output) {
        super(output);

        // Hurt Sound
        this.addSound("hurt", "player");
    }

    /**
     * Add sounds with name and category only
     *      * <p>Existing sound categories can be found in {@link net.minecraft.sound.SoundCategory}</p>
     * @param path The literal name of the sound file
     * @param category The category of the sound
     *  **/
    private void addSound(String path, String category) {
        this.putToMap(path, category);
    }

    private void putToMap(String path, String category) {
        String idToPath = path.replace(".", "/");
        String subtitle = "sound." + MOD_ID + "." + path;
        sounds.put(category + "." + path, new SoundEntry(
                category, subtitle, List.of(MOD_ID + ":" + idToPath))
        );
    }

    @Override
    protected Path getOutputPath() {
        return output.getPath().resolve("assets/" + MOD_ID + "/sounds.json");
    }

    @Override
    protected JsonElement generateJson() {
        JsonObject root = new JsonObject();

        for (Map.Entry<String, SoundEntry> entry : sounds.entrySet()) {
            JsonObject obj = new JsonObject();
            obj.addProperty("subtitle", entry.getValue().subtitle);

            JsonArray arr = new JsonArray();
            for (String s : entry.getValue().sounds) arr.add(s);

            obj.add("sounds", arr);
            root.add(entry.getKey(), obj);
        }

        return root;
    }

    private record SoundEntry(String category, String subtitle, List<String> sounds) {}

}