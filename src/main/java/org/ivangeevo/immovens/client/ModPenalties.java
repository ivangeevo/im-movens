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
    public ModPenalties() {
    }

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
                        case 0, 1 -> {
                            return "Starving";
                        }
                        case 2, 3 -> {
                            return "Famished";
                        }
                        case 4, 5, 6 -> {
                            return "Hungry";
                        }
                        case 7, 8 -> {
                            return "Peckish";
                        }
                    }
                    return "";
                },
                // Draw conditions
                () -> {
                    PlayerEntity player = MinecraftClient.getInstance().player;
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
                    // Get food level
                    PlayerEntity player = client.player;
                    if (player == null) return "";

                    float healthLevel = player.getHealth();
                    switch (MathHelper.ceil(healthLevel)) {
                        case 0, 1, 2 -> {
                            return "Dying";
                        }
                        case 3, 4 -> {
                            return "Crippled";
                        }
                        case 5, 6 -> {
                            return "Wounded";
                        }
                        case 7, 8 -> {
                            return "Injured";
                        }
                        case 9, 10 -> {
                            return "Hurt";
                        }
                    }
                    return "";
                },
                // Draw conditions
                () -> {
                    PlayerEntity player = MinecraftClient.getInstance().player;
                    if (player == null) return false;
                    return (ImMovensMod.getSettings().isHealthPenaltiesEnabled() &&
                            PlayerEffectsManager.getInstance().shouldBeAffected(player));
                }
        ));
    }
}
