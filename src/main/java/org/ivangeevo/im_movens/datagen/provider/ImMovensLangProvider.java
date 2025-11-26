package org.ivangeevo.im_movens.datagen.provider;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider;
import net.minecraft.registry.RegistryWrapper;
import org.ivangeevo.im_movens.ImMovensMod;

import java.util.concurrent.CompletableFuture;

public class ImMovensLangProvider extends FabricLanguageProvider {

    public ImMovensLangProvider(FabricDataOutput dataOutput, CompletableFuture<RegistryWrapper.WrapperLookup> registryLookup) {
        super(dataOutput, registryLookup);
    }

    @Override
    public void generateTranslations(RegistryWrapper.WrapperLookup registryLookup, TranslationBuilder tb) {
        this.generateBlockTranslations(tb);
        this.generateItemTranslations(tb);
        this.generateConfigTranslations(tb);
        this.generatePenaltyTextTranslations(tb);
        tb.add("sound.im_movens.hurt", "Player Hurts");
    }

    private void generateBlockTranslations(TranslationBuilder tb) {
    }

    private void generateItemTranslations(TranslationBuilder tb) {
    }

    private void generateConfigTranslations(TranslationBuilder tb) {
        this.addConfigMenuDefaults(tb);
        this.addConfigMenuTitle("Im 'movens Configuration Menu", tb);
        this.addConfigCategory("general", "General", tb);
        this.addConfig("fov_scaling", "FOV Scaling", tb);
        this.addConfigTooltip("fov_scaling", "Accessibility option that disables FOV from changing\n when under speed penalties from this mod", tb);

        //this.addConfig("pain_sounds", "Ambient Pain Sounds", tb);
        //this.addConfig("sneaking_pain_sounds", "Sneaking Ambient Pain Sounds", tb);
        //this.addConfig("sprinting_disabled_hunger_level", "Sprinting Disabled Hunger Level", tb);
        //this.addConfig("do_hunger_status", "Do Hunger Penalties", tb);
        //this.addConfig("do_health_status", "Do Health Penalties", tb);
        //this.addConfig("do_fat_status", "Do Fat Penalties", tb);
        //this.addConfig("do_natural_regen", "Regenerate Health Over Time", tb);
        //this.addConfig("hardcore_exhaustion", "Hardcore Exhaustion", tb);
        //this.addConfigTooltip("pain_sounds", "Player makes pain sounds when health is very low,\nemulating behavior from earlier versions of BTW", tb);
        //this.addConfigTooltip("sneaking_pain_sounds", "Toggles whether pain sounds should play when the player is sneaking", tb);
        //this.addConfigTooltip("sprinting_disabled_hunger_level", "Set the hunger level at which sprinting should be disabled.\n Each whole point equals half a hunger shank, up to maximum20.0 (10 shanks)", tb);
        //this.addConfigTooltip("do_hunger_status", "Introduces debuffs as your hunger gets lower.", tb);
        //this.addConfigTooltip("do_health_status", "Introduces debuffs as your health gets lower.", tb);
        //this.addConfigTooltip("do_fat_status", "Introduces debuffs as your fat increases.\nThis only works if Granular Hunger mod is installed.", tb);
        //this.addConfigTooltip("do_natural_regen", "Naturally regenerate half a heart every 600 ticks.\nThis operates on top of vanilla regen/saturation mechanics", tb);
        //this.addConfigTooltip("hardcore_exhaustion", "Alters action exhaustion values to better \nmatch those seen in Better Than Wolves", tb);
    }

    private void generatePenaltyTextTranslations(TranslationBuilder tb) {
        addPenaltyText("hurt", "Hurt", tb);
        addPenaltyText("injured", "Injured", tb);
        addPenaltyText("wounded", "Wounded", tb);
        addPenaltyText("crippled", "Crippled", tb);
        addPenaltyText("dying", "Dying", tb);
        addPenaltyText("peckish", "Peckish", tb);
        addPenaltyText("hungry", "Hungry", tb);
        addPenaltyText("famished", "Famished", tb);
        addPenaltyText("emaciated", "Emaciated", tb);
        addPenaltyText("starving", "Starving", tb);
        addPenaltyText("plump", "Plump", tb);
        addPenaltyText("chubby", "Chubby", tb);
        addPenaltyText("fat", "Fat", tb);
        addPenaltyText("obese", "Obese", tb);
    }

    private void addConfigMenuDefaults(TranslationBuilder tb) {
        this.addSimpleText("clientSettingsText", "Client Settings:", tb);
        this.addSimpleText("emptyClientConfigText", "§eNote:§r There are currently no client config settings.", tb);
        this.addSimpleText("serverSettingsText", "Server Settings:", tb);
        this.addSimpleText("serverSettingsNoAccessText", "§eNote:§r Server settings are not accessible in menus." +
                "\nThey can only be changed by editing the config file manually and require a world reload to take effect.", tb
        );
    }

    private void addConfigMenuTitle(String translation, TranslationBuilder tb) {
        tb.add("title." + ImMovensMod.MOD_ID + ".config", translation);
    }

    private void addItemGroup(String path, String translation, TranslationBuilder tb) {
        tb.add("itemgroup." + path, translation);
    }

    private void addPenaltyText(String path, String translation, TranslationBuilder tb) {
        tb.add("penalty_text." + ImMovensMod.MOD_ID + "." + path, translation);
    }

    private void addConfigCategory(String path, String translation, TranslationBuilder tb) {
        tb.add("config." + ImMovensMod.MOD_ID + ".category." + path, translation);
    }

    private void addSimpleText(String path, String translation, TranslationBuilder tb) {
        tb.add("config." + ImMovensMod.MOD_ID + ".text." + path, translation);
    }

    private void addConfig(String path, String translation, TranslationBuilder tb) {
        tb.add("config." + ImMovensMod.MOD_ID + "." + path, translation);
    }

    private void addConfigTooltip(String path, String translation, TranslationBuilder tb) {
        tb.add("config." + ImMovensMod.MOD_ID + ".tooltip." + path, translation);
    }

}