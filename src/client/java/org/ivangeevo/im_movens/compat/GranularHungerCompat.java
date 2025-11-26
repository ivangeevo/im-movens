package org.ivangeevo.im_movens.compat;

import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;
import org.btwr.shared_library.gui.hud.PenaltyDisplayManager;
import org.ivangeevo.im_movens.ImMovensMod;
import org.ivangeevo.im_movens.config.ImMovensConfig;
import org.ivangeevo.im_movens.util.ModPenalties;

import java.util.function.BooleanSupplier;

import static org.ivangeevo.im_movens.util.PenaltyTextHelper.*;

public class GranularHungerCompat {

    public static final int FAT_PRIORITY = 40;

    public static void addPenaltyTextForFat() {
        PenaltyDisplayManager dm = PenaltyDisplayManager.getInstance();
        MinecraftClient client = MinecraftClient.getInstance();
        BooleanSupplier showFatPenalties = () -> ImMovensConfig.Settings.fatPenalties.get() && shouldPlayerBeAffected(client);

        if (ImMovensMod.isHungerGranular) {
            dm.addPenalty(make(
                    FAT_PRIORITY,
                    playerText(client, () -> fatText(player(client))),
                    isVisibleToClientPlayer(client, showFatPenalties)
            ));
        }
    }

    private static String fatText(PlayerEntity p) {
        int tier = fatTier(p.getHungerManager().getSaturationLevel());
        String path = ModPenalties.PENALTY_TEXT_BASE_PATH;
        return switch (tier) {
            case 7 -> path + "plump";
            case 8 -> path + "chubby";
            case 9 -> path + "fat";
            case 10 -> path + "obese";
            default -> "";
        };
    }

}