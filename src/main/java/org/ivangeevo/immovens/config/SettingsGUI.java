package org.ivangeevo.immovens.config;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;
import org.ivangeevo.immovens.ImMovensMod;

import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;

public class SettingsGUI
{
    static ModSettings settingsCommon = ImMovensMod.getInstance().settings;
    public static Screen createConfigScreen(Screen parent) {
        ConfigBuilder builder = ConfigBuilder.create()
                .setParentScreen(parent).setTitle(Text.translatable("title.immovens.config"));
        builder.setSavingRunnable(() -> { ImMovensMod.getInstance().saveSettings(); });

        ConfigEntryBuilder entryBuilder = builder.entryBuilder();

        ConfigCategory general = builder.getOrCreateCategory(Text.translatable("config.immovens.category.general"));

        /** General Category **/
        general.addEntry(entryBuilder
                .startBooleanToggle(Text.translatable("config.immovens.fov_scaling"), settingsCommon.disableFOVScaling)
                .setDefaultValue(false)
                .setSaveConsumer(newValue -> settingsCommon.disableFOVScaling = newValue)
                .setTooltip(Text.translatable("config.immovens.tooltip.fov_scaling"))
                .build());

        general.addEntry(entryBuilder
                .startBooleanToggle(Text.translatable("config.immovens.pain_sounds"), settingsCommon.painSounds)
                .setDefaultValue(true)
                .setSaveConsumer(newValue -> settingsCommon.painSounds = newValue)
                .setTooltip(Text.translatable("config.immovens.tooltip.pain_sounds"))
                .build());

        return builder.build();
    }

}