package org.ivangeevo.im_movens.util;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.HungerManager;
import org.btwr.shared_library.gui.hud.HUDInitializeListener;
import org.btwr.shared_library.gui.hud.PenaltyDisplayManager;
import org.ivangeevo.im_movens.ImMovensMod;
import org.ivangeevo.im_movens.compat.GranularHungerCompat;
import org.ivangeevo.im_movens.config.ImMovensConfig;

import java.util.function.BooleanSupplier;

import static org.ivangeevo.im_movens.util.PenaltyTextHelper.*;


public class ModPenalties implements HUDInitializeListener {
    
    public static final String PENALTY_TEXT_BASE_PATH = "penalty_text." + ImMovensMod.MOD_ID + ".";

    @Override
    public void init(MinecraftClient client, PenaltyDisplayManager dm) {
        BooleanSupplier showHungerPenalties = () -> ImMovensConfig.Settings.hungerPenalties.get() && shouldPlayerBeAffected(client);
        BooleanSupplier showHealthPenalties = () -> ImMovensConfig.Settings.healthPenalties.get() && shouldPlayerBeAffected(client);

        // ---------------- HUNGER
        dm.addPenalty(make(
                PenaltyDisplayManager.HUNGER_PRIORITY,
                playerText(client, () -> hungerText(player(client))),
                isVisibleToClientPlayer(client, showHungerPenalties)
        ));

        // ---------------- FAT
        GranularHungerCompat.addPenaltyTextForFat();

        // ---------------- HEALTH
        dm.addPenalty(make(
                PenaltyDisplayManager.HEALTH_PRIORITY,
                playerText(client, () -> healthText(player(client))),
                isVisibleToClientPlayer(client, showHealthPenalties)
        ));

        // ---------------- DEBUG
        dm.addPenalty(make(
                100,
                playerText(client, () -> {
                    PlayerEntity p = player(client);
                    HungerManager hm = p.getHungerManager();
                    return "FOOD %d HEALTH %.2f".formatted(hm.getFoodLevel(), p.getHealth());
                }),
                () ->
                        client.getDebugHud().shouldShowDebugHud()
                                && FabricLoader.getInstance().isDevelopmentEnvironment()
        ));
    }

    private String hungerText(PlayerEntity p) {
        int tier = foodTier(p.getHungerManager().getFoodLevel(), ImMovensMod.isHungerGranular);
        String path = PENALTY_TEXT_BASE_PATH;
        return switch (tier) {
            case 0 -> path + "starving";
            case 1, 2 -> path + "emaciated";
            case 3, 4 -> path + "famished";
            case 5, 6 -> path + "hungry";
            case 7, 8 -> path + "peckish";
            default -> "";
        };
    }

    private String healthText(PlayerEntity p) {
        int tier = healthTier(p.getHealth());
        String path = PENALTY_TEXT_BASE_PATH;

        return switch (tier) {
            case 0, 1, 2 -> path + "dying";
            case 3, 4 -> path + "crippled";
            case 5, 6 -> path + "wounded";
            case 7, 8 -> path + "injured";
            case 9, 10 -> path + "hurt";
            default -> "";
        };
    }

}