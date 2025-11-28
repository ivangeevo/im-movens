package org.btwr.im_movens.config;

import org.btwr.im_movens.ImMovensMod;
import org.btwr.shared_library.api.config.ConfigBuilder;
import org.btwr.shared_library.api.config.ConfigGroup;
import org.btwr.shared_library.api.config.ConfigSetting;
import org.btwr.shared_library.api.config.TomlConfigManager;

public class ImMovensConfig {

    /** Replace with your MOD_ID for easy adaptation **/
    private static final String MOD_ID = ImMovensMod.MOD_ID;

    public static final ConfigGroup CONFIG;

    /** Call this method in your mod initializer so the class can initialize **/
    public static void register() {}

    public static final ConfigSetting<Boolean> fovScaling =
            ConfigBuilder.booleanSetting("fovScaling")
                    .defaultValue(false)
                    .comment("Accessibility option that disables FOV from\nchanging when under speed penalties from this mod")
                    .build();

    public static final ConfigSetting<Boolean> painSounds =
            ConfigBuilder.booleanSetting("painSounds")
                    .defaultValue(false)
                    .comment("Player makes pain sounds when health is very low,\nemulating behavior from earlier versions of BTW")
                    .build();

    public static final ConfigSetting<Boolean> sneakingPainSounds =
            ConfigBuilder.booleanSetting("sneakingPainSounds")
                    .defaultValue(false)
                    .comment("Toggles whether pain sounds should play when the player is sneaking")
                    .build();

    public static final ConfigSetting<Boolean> hardcoreExhaustion =
            ConfigBuilder.booleanSetting("hardcoreExhaustion")
                    .defaultValue(true)
                    .comment("Alters action exhaustion values to better\nmatch those seen in Better Than Wolves")
                    .build();

    public static final ConfigSetting<Boolean> hungerPenalties =
            ConfigBuilder.booleanSetting("hungerPenalties")
                    .defaultValue(true)
                    .comment("Introduces debuffs as your hunger gets lower.")
                    .build();

    public static final ConfigSetting<Boolean> healthPenalties =
            ConfigBuilder.booleanSetting("healthPenalties")
                    .defaultValue(true)
                    .comment("Introduces debuffs as your health gets lower.")
                    .build();

    public static final ConfigSetting<Boolean> fatPenalties =
            ConfigBuilder.booleanSetting("fatPenalties")
                    .defaultValue(true)
                    .comment("Introduces debuffs as your fat increases.\nThis only works if Granular Hunger mod is installed.")
                    .build();

    public static final ConfigSetting<Boolean> naturalRegen =
            ConfigBuilder.booleanSetting("naturalRegen")
                    .defaultValue(true)
                    .comment("Naturally regenerate half a heart every 600 ticks.\nThis operates on top of vanilla regen/saturation mechanics")
                    .build();

    public static final ConfigSetting<Double> sprintingDisabledHungerLevel =
            ConfigBuilder.doubleSetting("sprintingDisabledHungerLevel")
                    .defaultValue(8.0)
                    .comment("Set the hunger level at which sprinting should be disabled.\nEach whole point equals half a hunger shank, up to maximum 20.0 (10 shanks)")
                    .build();

    static {
        CONFIG = new ConfigGroup(String.format("%s/%s_common.toml", MOD_ID, MOD_ID));
        CONFIG.add(fovScaling);
        CONFIG.add(painSounds);
        CONFIG.add(sneakingPainSounds);
        CONFIG.add(hardcoreExhaustion);
        CONFIG.add(hungerPenalties);
        CONFIG.add(healthPenalties);
        CONFIG.add(fatPenalties);
        CONFIG.add(naturalRegen);
        CONFIG.add(sprintingDisabledHungerLevel);
        TomlConfigManager.registerGroup(CONFIG); // auto init/load/save
    }

}