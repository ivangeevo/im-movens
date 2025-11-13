package org.ivangeevo.immovens.client;

import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;
import org.ivangeevo.immovens.ImMovensMod;

public class ImMovensSound {
    private static final Identifier PLAYER_HURT_SOUND_ID = Identifier.of(ImMovensMod.MOD_ID, "hurt");
    public static final SoundEvent PLAYER_HURT = SoundEvent.of(PLAYER_HURT_SOUND_ID);

    public static void register() {
        Registry.register(Registries.SOUND_EVENT, PLAYER_HURT_SOUND_ID, PLAYER_HURT);
    }
}
