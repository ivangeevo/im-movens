package org.ivangeevo.immovens.client;

import btwr.btwr_sl.lib.event.EventHUDInitialized;
import btwr.btwr_sl.lib.gui.PenaltyDisplayManager;
import net.fabricmc.api.ClientModInitializer;

public class ImMovensModClient implements ClientModInitializer
{
    private static final ModPenalties penalties = new ModPenalties();

    @Override
    public void onInitializeClient() {
        EventHUDInitialized.register(penalties);
    }
}
