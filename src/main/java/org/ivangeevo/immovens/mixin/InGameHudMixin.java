package org.ivangeevo.immovens.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.entity.player.HungerManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.fabricmc.loader.api.FabricLoader;

@Mixin(InGameHud.class)
public abstract class InGameHudMixin {

    @Shadow
    public abstract TextRenderer getTextRenderer();

    @Inject(method = "render", at = @At("HEAD"))
    private void injectedRender(DrawContext context, RenderTickCounter tickCounter, CallbackInfo ci) {
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

            if (!healthStatus.isEmpty()) {
                renderWellbeingStatusText(context, healthStatus, player);
            } else if (!foodStatus.isEmpty()) {
                renderWellbeingStatusText(context, foodStatus, player);
            }
        }
    }

    @Unique
    private void renderWellbeingStatusText(DrawContext context, String text, PlayerEntity player) {
        TextRenderer textRenderer = getTextRenderer();
        Text statusText = Text.translatable(text);

        // Calculate the position of the hunger bar
        int hungerBarX = context.getScaledWindowWidth() / 2 + 91;  // Center of the hunger bar
        int hungerBarY = context.getScaledWindowHeight() - 39; // Hunger bar position vertically

        // Adjust the X position to render the text centered over the hunger bar
        int textX = hungerBarX - (textRenderer.getWidth(statusText) / 2) - 20; // 20 pixels to the left of the hunger bar

        // Adjust the Y position based on whether the player is underwater or TAN mod is loaded
        int textY = hungerBarY - 10; // Default Y position (above the hunger bar)

        // Adjust the Y position if the breath bar is displaying
        if (player.getAir() != player.getMaxAir() /**|| FabricLoader.getInstance().isModLoaded("toughasnails") **/) {
            textY -= 10; // Move the text higher if the player is underwater
        }

        // Draw the status text
        context.drawText(textRenderer, statusText, textX, textY, 0xFFFFFFFF, true);
    }
}

