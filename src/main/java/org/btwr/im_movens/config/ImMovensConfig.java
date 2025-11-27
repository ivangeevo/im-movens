package org.btwr.im_movens.config;

import com.google.common.reflect.Reflection;
import com.supermartijn642.configlib.api.ConfigBuilders;
import com.supermartijn642.configlib.api.IConfigBuilder;
import org.btwr.im_movens.ImMovensMod;

import java.util.function.Supplier;

public class ImMovensConfig {

    public static void register() {
        Reflection.initialize(Settings.class);
    }

    public static class Settings {
        public static final Supplier<Boolean> fovScaling;
        public static final Supplier<Boolean> painSounds;
        public static final Supplier<Boolean> sneakingPainSounds;
        public static final Supplier<Boolean> hardcoreExhaustion;
        public static final Supplier<Boolean> hungerPenalties;
        public static final Supplier<Boolean> healthPenalties;
        public static final Supplier<Boolean> fatPenalties;
        public static final Supplier<Boolean> naturalRegen;
        public static final Supplier<Double> sprintingDisabledHungerLevel;

        static {
            // construct a new config builder
            IConfigBuilder builder = ConfigBuilders.newTomlConfig(ImMovensMod.MOD_ID, "im_movens_common", true);

            // Boolean checks
            fovScaling = builder
                    .comment("Accessibility option that disables FOV from\nchanging when under speed penalties from this mod")
                    .define("doFOVScaling", true);
            sprintingDisabledHungerLevel = builder
                    .comment("Set the hunger level at which sprinting should be disabled.\nEach whole point equals half a hunger shank, up to maximum20.0 (10 shanks)")
                    .define("sprintingDisabledHungerLevel", 8.0d, 0.0d, 20d);
            painSounds = builder
                    .comment("Player makes pain sounds when health is very low,\nemulating behavior from earlier versions of BTW")
                    .define("doPainSounds", false);
            sneakingPainSounds = builder
                    .comment("Toggles whether pain sounds should play when the player is sneaking")
                    .define("doSneakingPainSounds", false);
            hardcoreExhaustion = builder
                    .comment("Alters action exhaustion values to better\nmatch those seen in Better Than Wolves")
                    .define("doHardcoreExhaustion", true);
            hungerPenalties = builder
                    .comment("Introduces debuffs as your hunger gets lower.")
                    .define("doHungerPenalties", true);
            healthPenalties = builder
                    .comment("Introduces debuffs as your health gets lower.")
                    .define("doHealthPenalties", true);
            fatPenalties = builder
                    .comment("Introduces debuffs as your fat increases.\nThis only works if Granular Hunger mod is installed.")
                    .define("doFatPenalties", true);
            naturalRegen = builder
                    .comment("Naturally regenerate half a heart every 600 ticks.\nThis operates on top of vanilla regen/saturation mechanics")
                    .define("doNaturalRegen", true);

            // build the config
            builder.build();
        }
    }

}