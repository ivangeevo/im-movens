package org.ivangeevo.im_movens.sound;

import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;
import org.ivangeevo.im_movens.ImMovensMod;

public class ImMovensSoundEvents {

    public static final SoundEvent PLAYER_HURT = registerSound("player.hurt");

    public static void register() {
    }

    private static SoundEvent registerSound(String path) {
        Identifier id = Identifier.of(ImMovensMod.MOD_ID, path);
        SoundEvent soundEvent = SoundEvent.of(id);
        Registry.register(Registries.SOUND_EVENT, id, soundEvent);
        return soundEvent;
    }

}