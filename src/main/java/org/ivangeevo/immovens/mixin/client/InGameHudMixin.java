package org.ivangeevo.immovens.mixin.client;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.entity.player.HungerManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;
import org.ivangeevo.immovens.ImMovensMod;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InGameHud.class)
public abstract class InGameHudMixin {

    @Shadow
    public abstract TextRenderer getTextRenderer();

    /**
     * Indicates if the hunger bar is currently rendered
     */
    @Unique
    private boolean isRenderingFood = false;

    /**
     * Injects penalty status rendering into the vanilla status bar renderer
     */
    @Inject(method = "renderStatusBars", at = @At("HEAD"))
    private void injectedRender(DrawContext context, CallbackInfo ci) {
        PlayerEntity player = MinecraftClient.getInstance().player;

        if (player != null && !player.getAbilities().creativeMode) {
            HungerManager hungerManager = player.getHungerManager();
            int foodLevel = hungerManager.getFoodLevel();
            float healthLevel = player.getHealth();

            String foodStatus = "";
            String healthStatus = "";

            // Set food status text if hunger penalties are enabled
            if (ImMovensMod.getInstance().settings.isHungerPenaltiesEnabled()) {
                switch (foodLevel) {
                    case 0, 1 -> foodStatus = "Starving";
                    case 2, 3 -> foodStatus = "Famished";
                    case 4, 5, 6 -> foodStatus = "Hungry";
                    case 7, 8 -> foodStatus = "Peckish";
                }
            }

            // Set health status text if health penalties are enabled
            if (ImMovensMod.getInstance().settings.isHealthPenaltiesEnabled()) {
                switch ((int) healthLevel) {
                    case 0, 1, 2 -> healthStatus = "Dying";
                    case 3, 4 -> healthStatus = "Crippled";
                    case 5, 6 -> healthStatus = "Wounded";
                    case 7, 8 -> healthStatus = "Injured";
                    case 9, 10 -> healthStatus = "Hurt";
                }
            }

            renderWellbeingStatusText(context, healthStatus, foodStatus, player);
        }
    }

    /**
     * Checks to see if renderFood was called and not intercepted,
     * indicating that hunger has been successfully rendered
     */
    @Inject(method = "renderFood", at = @At("TAIL"))
    private void renderFoodCheck(DrawContext context, PlayerEntity player, int top, int right, CallbackInfo ci) {
        isRenderingFood = true;
    }

    /**
     * Draws health and hunger statuses (if applicable) onto provided context
     * @param context DrawContext to draw to
     * @param healthStatus Health status string
     * @param foodStatus Food status string
     * @param player PlayerEntity to get information from
     */
    @Unique
    private void renderWellbeingStatusText(DrawContext context, String healthStatus, String foodStatus, PlayerEntity player) {
        TextRenderer textRenderer = getTextRenderer();
        Text healthStatusText = Text.translatable(healthStatus);
        Text foodStatusText = Text.translatable(foodStatus);

        // Calculate the position of the hunger bar
        int hungerBarX = context.getScaledWindowWidth() / 2 + 91;  // Center of the hunger bar
        int hungerBarY = context.getScaledWindowHeight() - 39; // Hunger bar position vertically

        // Adjust the X position to render the text right aligned to the hot-bar
        int fStatusX = hungerBarX - textRenderer.getWidth(foodStatusText);
        int hStatusX = hungerBarX - textRenderer.getWidth(healthStatusText);

        // Adjust the Y position based on status bar context
        int textY = getTextY(player, hungerBarY);

        // Draw both status text
        context.drawText(textRenderer, healthStatusText, hStatusX, textY, 0xFFFFFFFF, true);
        context.drawText(textRenderer, foodStatusText, fStatusX, (!healthStatus.isEmpty() ? textY - 10 : textY), 0xFFFFFFFF, true);

        // Set render boolean to false in-case of HUD changes at runtime
        isRenderingFood = false;
    }

    /**
     * Calculates proper Y position for HUD elements
     * @param player PlayerEntity to get information from
     * @param hungerBarY Y position of the hunger bar
     * @return Calculated Y position
     */
    @Unique
    private int getTextY(PlayerEntity player, int hungerBarY) {
        // Get additional context
        boolean isRenderingAir = player.getAir() != player.getMaxAir();
        boolean isRenderingArmor = player.getArmor() > 0;

        // Default Y position (above the hunger bar, alternatively above hot-bar)
        int textY = isRenderingFood ? hungerBarY - 10 : hungerBarY;

        // Adjust the Y position under certain conditions
        if (
                (isRenderingAir && isRenderingFood) ||  // Player is underwater and food is rendered
                (isRenderingArmor && !isRenderingFood)) // Player is wearing armor and food is NOT rendered
                // Future explicit compatibility checks could be done here
        {
            textY -= 10;
        }

        return textY;
    }
}

