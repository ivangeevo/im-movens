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
                .startBooleanToggle(Text.translatable("config.immovens.fov_scaling"), settingsCommon.doFOVScaling)
                .setDefaultValue(true)
                .setSaveConsumer(newValue -> settingsCommon.doFOVScaling = newValue)
                .setTooltip(Text.translatable("config.immovens.tooltip.fov_scaling"))
                .build());
        general.addEntry(entryBuilder
                .startBooleanToggle(Text.translatable("config.immovens.pain_sounds"), settingsCommon.doPainSounds)
                .setDefaultValue(false)
                .setSaveConsumer(newValue -> settingsCommon.doPainSounds = newValue)
                .setTooltip(Text.translatable("config.immovens.tooltip.pain_sounds"))
                .build());

        /** Gameplay Category **/
        ConfigCategory gameplay = builder.getOrCreateCategory(Text.translatable("config.immovens.category.gameplay"));
        gameplay.addEntry(entryBuilder
                .startBooleanToggle(Text.translatable("config.immovens.hardcore_exhaustion"), settingsCommon.doHardcoreExhaustion)
                .setDefaultValue(true)
                .setSaveConsumer(newValue -> settingsCommon.doHardcoreExhaustion = newValue)
                .setTooltip(Text.translatable("config.immovens.tooltip.hardcore_exhaustion"))
                .build());
        gameplay.addEntry(entryBuilder
                .startBooleanToggle(Text.translatable("config.immovens.do_hunger_status"), settingsCommon.doHungerPenalties)
                .setDefaultValue(true)
                .setSaveConsumer(newValue -> settingsCommon.doHungerPenalties = newValue)
                .setTooltip(Text.translatable("config.immovens.tooltip.do_hunger_status"))
                .build());
        gameplay.addEntry(entryBuilder
                .startBooleanToggle(Text.translatable("config.immovens.do_health_status"), settingsCommon.doHealthPenalties)
                .setDefaultValue(true)
                .setSaveConsumer(newValue -> settingsCommon.doHealthPenalties = newValue)
                .setTooltip(Text.translatable("config.immovens.tooltip.do_health_status"))
                .build());
        gameplay.addEntry(entryBuilder
                .startBooleanToggle(Text.translatable("config.immovens.do_natural_regen"), settingsCommon.doNaturalRegen)
                .setDefaultValue(true)
                .setSaveConsumer(newValue -> settingsCommon.doNaturalRegen = newValue)
                .setTooltip(Text.translatable("config.immovens.tooltip.do_natural_regen"))
                .build());

        return builder.build();
    }

}