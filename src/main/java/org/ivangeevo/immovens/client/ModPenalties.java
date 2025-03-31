package org.ivangeevo.immovens.client;

import btwr.btwr_sl.lib.gui.HUDInitializeListener;
import btwr.btwr_sl.lib.gui.PenaltyDisplayManager;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.HungerManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.MathHelper;
import org.ivangeevo.immovens.ImMovensMod;
import org.ivangeevo.immovens.util.PlayerEffectsManager;

public class ModPenalties implements HUDInitializeListener {
    /**
     * Initializes hunger and health penalties
     */
    @Override
    public void init(MinecraftClient client, PenaltyDisplayManager dm) {
        dm.addPenalty(new PenaltyDisplayManager.Penalty(
                // Priority
                PenaltyDisplayManager.HUNGER_PRIORITY,
                // Text conditions
                () -> {
                    // Get food level
                    PlayerEntity player = client.player;
                    if (player == null) return "";

                    HungerManager hungerManager = player.getHungerManager();
                    int foodLevel = hungerManager.getFoodLevel();

                    switch (foodLevel) {
                        case 0, 1, 2 -> {
                            return "penalty.im_movens.starving";
                        }
                        case 3, 4 -> {
                            return "penalty.im_movens.famished";
                        }
                        case 5, 6 -> {
                            return "penalty.im_movens.hungry";
                        }
                        case 7, 8 -> {
                            return "penalty.im_movens.peckish";
                        }
                    }
                    return "";
                },
                // Draw conditions
                () -> {
                    PlayerEntity player = client.player;
                    if (player == null) return false;
                    return (ImMovensMod.getSettings().isHungerPenaltiesEnabled() &&
                            PlayerEffectsManager.getInstance().shouldBeAffected(player));
                }
        ));
        dm.addPenalty(new PenaltyDisplayManager.Penalty(
                // Priority
                PenaltyDisplayManager.HEALTH_PRIORITY,
                // Text conditions
                () -> {
                    // Get health level
                    PlayerEntity player = client.player;
                    if (player == null) return "";

                    float healthLevel = player.getHealth();
                    switch (MathHelper.ceil(healthLevel)) {
                        case 0, 1, 2 -> {
                            return "penalty.im_movens.dying";
                        }
                        case 3, 4 -> {
                            return "penalty.im_movens.crippled";
                        }
                        case 5, 6 -> {
                            return "penalty.im_movens.wounded";
                        }
                        case 7, 8 -> {
                            return "penalty.im_movens.injured";
                        }
                        case 9, 10 -> {
                            return "penalty.im_movens.hurt";
                        }
                    }
                    return "";
                },
                // Draw conditions
                () -> {
                    PlayerEntity player = client.player;
                    if (player == null) return false;
                    return (ImMovensMod.getSettings().isHealthPenaltiesEnabled() &&
                            PlayerEffectsManager.getInstance().shouldBeAffected(player));
                }
        ));
    }
}
