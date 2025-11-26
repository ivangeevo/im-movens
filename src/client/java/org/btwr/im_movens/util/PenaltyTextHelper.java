package org.btwr.im_movens.util;

import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.MathHelper;
import org.btwr.shared_library.gui.hud.PenaltyDisplayManager;

import java.util.function.BooleanSupplier;
import java.util.function.Supplier;

public final class PenaltyTextHelper {

    // ------------------------------------------------------------
    // Player access helpers
    // ------------------------------------------------------------

    public static boolean shouldPlayerBeAffected(MinecraftClient client) {
        PlayerEntity player = client.player;
        assert player != null;
        return (!player.isCreative() && !player.isSpectator() && !player.isDead());
    }

    public static PlayerEntity player(MinecraftClient client) {
        return (client != null) ? client.player : null;
    }

    /** Wraps a text lambda so it returns "" when no player exists. */
    public static Supplier<String> playerText(MinecraftClient client, Supplier<String> text) {
        return () -> {
            PlayerEntity p = player(client);
            return (p == null) ? "" : text.get();
        };
    }

    /** Wraps visibility so it returns false when no player exists. */
    public static BooleanSupplier isVisibleToClientPlayer(MinecraftClient client, BooleanSupplier visible) {
        return () -> {
            PlayerEntity p = player(client);
            return p != null && visible.getAsBoolean();
        };
    }

    // ------------------------------------------------------------
    // Tier helpers (just arithmetic)
    // ------------------------------------------------------------

    public static int foodTier(int food, boolean granular) {
        return granular ? MathHelper.ceil(food / 3d) : food;
    }

    public static int fatTier(float saturation) {
        return MathHelper.ceil(saturation / 6f);
    }

    public static int healthTier(float health) {
        return MathHelper.ceil(health);
    }

    // ------------------------------------------------------------
    // Penalty builder
    // ------------------------------------------------------------

    /** Creates a penalty instance with specified priority, text and conditions under which it should be visible**/
    public static PenaltyDisplayManager.Penalty make(
            int priority,
            Supplier<String> text,
            BooleanSupplier visible
    ) {
        return new PenaltyDisplayManager.Penalty(priority, text::get, visible::getAsBoolean);
    }

}