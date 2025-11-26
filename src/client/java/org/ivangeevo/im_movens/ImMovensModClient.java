package org.ivangeevo.im_movens;

import net.fabricmc.api.ClientModInitializer;
import org.btwr.shared_library.event.EventHUDInitialized;
import org.ivangeevo.im_movens.util.ModPenalties;

public class ImMovensModClient implements ClientModInitializer {

    private static final ModPenalties penalties = new ModPenalties();

    @Override
    public void onInitializeClient() {
        // Initialize penalties
        EventHUDInitialized.register(penalties);
    }

}
