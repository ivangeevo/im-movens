package org.ivangeevo.im_movens.config;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;
import org.ivangeevo.im_movens.ImMovensMod;

import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;

public class SettingsGUI
{
    static ModSettings settingsCommon = ImMovensMod.getInstance().settings;
    public static Screen createConfigScreen(Screen parent) {
        ConfigBuilder builder = ConfigBuilder.create()
                .setParentScreen(parent).setTitle(Text.translatable("title.im_movens.config"));
        builder.setSavingRunnable(() -> { ImMovensMod.getInstance().saveSettings(); });

        ConfigEntryBuilder entryBuilder = builder.entryBuilder();

        ConfigCategory general = builder.getOrCreateCategory(Text.translatable("config.im_movens.category.general"));

        /** General Category **/
        general.addEntry(entryBuilder
                .startBooleanToggle(Text.translatable("config.im_movens.fov_scaling"), settingsCommon.doFOVScaling)
                .setDefaultValue(true)
                .setSaveConsumer(newValue -> settingsCommon.doFOVScaling = newValue)
                .setTooltip(Text.translatable("config.im_movens.tooltip.fov_scaling"))
                .build()
        );
        general.addEntry(entryBuilder
                .startBooleanToggle(Text.translatable("config.im_movens.pain_sounds"), settingsCommon.doPainSounds)
                .setDefaultValue(false)
                .setSaveConsumer(newValue -> settingsCommon.doPainSounds = newValue)
                .setTooltip(Text.translatable("config.im_movens.tooltip.pain_sounds"))
                .build()
        );

        general.addEntry(entryBuilder
                .startBooleanToggle(Text.translatable("config.im_movens.sneaking_pain_sounds"), settingsCommon.doSneakingPainSounds)
                .setRequirement(() -> settingsCommon.doPainSounds)
                .setDefaultValue(false)
                .setSaveConsumer(newValue -> settingsCommon.doSneakingPainSounds = newValue)
                .setTooltip(Text.translatable("config.im_movens.tooltip.sneaking_pain_sounds"))
                .build()
        );

        general.addEntry(entryBuilder
                .startFloatField(Text.translatable("config.im_movens.sprinting_disabled_hunger_level"), settingsCommon.getSprintingDisabledHungerLevel())
                .setDefaultValue(8.0f)
                .setSaveConsumer(newValue -> settingsCommon.sprintingDisabledHungerLevel = newValue)
                .setTooltip(Text.translatable("config.im_movens.tooltip.sprinting_disabled_hunger_level"))
                .build()
        );

        /** Gameplay Category **/
        ConfigCategory gameplay = builder.getOrCreateCategory(Text.translatable("config.im_movens.category.gameplay"));
        gameplay.addEntry(entryBuilder
                .startBooleanToggle(Text.translatable("config.im_movens.hardcore_exhaustion"), settingsCommon.doHardcoreExhaustion)
                .setDefaultValue(true)
                .setSaveConsumer(newValue -> settingsCommon.doHardcoreExhaustion = newValue)
                .setTooltip(Text.translatable("config.im_movens.tooltip.hardcore_exhaustion"))
                .build()
        );
        gameplay.addEntry(entryBuilder
                .startBooleanToggle(Text.translatable("config.im_movens.do_hunger_status"), settingsCommon.doHungerPenalties)
                .setDefaultValue(true)
                .setSaveConsumer(newValue -> settingsCommon.doHungerPenalties = newValue)
                .setTooltip(Text.translatable("config.im_movens.tooltip.do_hunger_status"))
                .build()
        );
        gameplay.addEntry(entryBuilder
                .startBooleanToggle(Text.translatable("config.im_movens.do_health_status"), settingsCommon.doHealthPenalties)
                .setDefaultValue(true)
                .setSaveConsumer(newValue -> settingsCommon.doHealthPenalties = newValue)
                .setTooltip(Text.translatable("config.im_movens.tooltip.do_health_status"))
                .build()
        );
        gameplay.addEntry(entryBuilder
                .startBooleanToggle(Text.translatable("config.im_movens.do_fat_status"), settingsCommon.doFatPenalties)
                .setDefaultValue(true)
                .setSaveConsumer(newValue -> settingsCommon.doFatPenalties = newValue)
                .setTooltip(Text.translatable("config.im_movens.tooltip.do_fat_status"))
                .setDisplayRequirement(() -> ImMovensMod.isHungerGranular)
                .build()
        );
        gameplay.addEntry(entryBuilder
                .startBooleanToggle(Text.translatable("config.im_movens.do_natural_regen"), settingsCommon.doNaturalRegen)
                .setDefaultValue(true)
                .setSaveConsumer(newValue -> settingsCommon.doNaturalRegen = newValue)
                .setTooltip(Text.translatable("config.im_movens.tooltip.do_natural_regen"))
                .build()
        );

        return builder.build();
    }

}