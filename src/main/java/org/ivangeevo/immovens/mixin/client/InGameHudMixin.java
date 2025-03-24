package org.ivangeevo.immovens.mixin.client;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.entity.player.HungerManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InGameHud.class)
public abstract class InGameHudMixin {

    @Shadow
    public abstract TextRenderer getTextRenderer();

    @Unique
    private boolean isRenderingFood = false;

    @Inject(method = "renderStatusBars", at = @At("HEAD"))
    private void injectedRender(DrawContext context, CallbackInfo ci) {
        PlayerEntity player = MinecraftClient.getInstance().player;

        if (player != null && !player.getAbilities().creativeMode) {
            HungerManager hungerManager = player.getHungerManager();
            int foodLevel = hungerManager.getFoodLevel();
            float healthLevel = player.getHealth();

            String foodStatus = "";
            String healthStatus = "";

            switch (foodLevel) {
                case 0, 1 -> foodStatus = "Starving";
                case 2, 3 -> foodStatus = "Famished";
                case 4, 5, 6 -> foodStatus = "Hungry";
                case 7, 8 -> foodStatus = "Peckish";
            }

            switch ((int) healthLevel) {
                case 0, 1, 2 -> healthStatus = "Dying";
                case 3, 4 -> healthStatus = "Crippled";
                case 5, 6 -> healthStatus = "Wounded";
                case 7, 8 -> healthStatus = "Injured";
                case 9, 10 -> healthStatus = "Hurt";
            }

            renderWellbeingStatusText(context, healthStatus, foodStatus, player);
        }
    }

    @Inject(method = "renderFood", at = @At("TAIL"))
    private void renderFoodCheck(DrawContext context, PlayerEntity player, int top, int right, CallbackInfo ci) {
        // We're rendering food, so set this variable
        isRenderingFood = true;
    }

    @Unique
    private void renderWellbeingStatusText(DrawContext context, String healthStatus, String foodStatus, PlayerEntity player) {
        TextRenderer textRenderer = getTextRenderer();
        Text healthStatusText = Text.translatable(healthStatus);
        Text foodStatusText = Text.translatable(foodStatus);

        // Calculate the position of the hunger bar
        int hungerBarX = context.getScaledWindowWidth() / 2 + 91;  // Center of the hunger bar
        int hungerBarY = context.getScaledWindowHeight() - 39; // Hunger bar position vertically

        // Adjust the X position to render the text centered over the hunger bar
        int fStatusX = hungerBarX - textRenderer.getWidth(foodStatusText); // 20 pixels to the left of the hunger bar
        int hStatusX = hungerBarX - textRenderer.getWidth(healthStatusText);

        // Adjust the Y position based on whether the player is underwater or TAN mod is loaded
        int textY = isRenderingFood ? hungerBarY - 10 : hungerBarY; // Default Y position (above the hunger bar)

        // Adjust the Y position under certain conditions
        if (
                (player.getAir() != player.getMaxAir()) || // Player is underwater
                (player.getArmor() > 0 && !isRenderingFood) // Player is wearing armor and hunger is not displayed
        ) {
            textY -= 10; // Move the text higher if the player is underwater
        }

        // Draw both status text
        context.drawText(textRenderer, healthStatusText, hStatusX, textY, 0xFFFFFFFF, true);
        if (isRenderingFood) // Only render food status if food is being rendered
            context.drawText(textRenderer, foodStatusText, fStatusX, (!healthStatus.isEmpty() ? textY - 10 : textY), 0xFFFFFFFF, true);

        // Set render booleans to false in-case of runtime config changes
        isRenderingFood = false;
    }
}

