package org.ivangeevo.immovens.util;

import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.util.Identifier;
import org.ivangeevo.immovens.ImMovensMod;

public class StatusEffectUtils {


    public enum HungerState {
        STARVING(0.25f, 0.25f),
        FAMISHED(0.5f, 0.5f),
        HUNGRY(0.75f, 0.75f),
        PECKISH(1.0f, 1.0f),
        WELL_FED(1.0f, 1.0f);

        private final EntityAttributeModifier speedModifier;
        private final EntityAttributeModifier attackModifier;


        HungerState(float speedMultiplier, float attackModifier)
        {
            this.attackModifier = new EntityAttributeModifier(
                    Identifier.of(ImMovensMod.MOD_ID, "hunger_speed_modifier"),
                    speedMultiplier - 1.0f,
                    EntityAttributeModifier.Operation.ADD_MULTIPLIED_TOTAL
            );
            this.speedModifier = new EntityAttributeModifier(
                    Identifier.of(ImMovensMod.MOD_ID, "hunger_speed_modifier"),
                    attackModifier - 1.0f,
                    EntityAttributeModifier.Operation.ADD_MULTIPLIED_TOTAL
            );
        }

        public EntityAttributeModifier getSpeedModifier() {
            return speedModifier;
        }
        public EntityAttributeModifier getAttackModifier() {
            return attackModifier;
        }


        public static HungerState fromFoodLevel(int foodLevel) {
            return switch (foodLevel) {
                case 0, 1, 2 -> STARVING;
                case 3, 4 -> FAMISHED;
                case 5, 6 -> HUNGRY;
                case 7, 8 -> PECKISH;
                default -> WELL_FED;
            };
        }
    }

    public enum HealthState {
        DYING(0.25f),
        CRIPPLED(0.5f),
        WOUNDED(0.75f),
        INJURED(1.0f),
        HEALTHY(1.0f);

        private final EntityAttributeModifier speedModifier;

        HealthState(float speedMultiplier) {
            this.speedModifier = new EntityAttributeModifier(
                    Identifier.of(ImMovensMod.MOD_ID, "health_speed_modifier"),
                    speedMultiplier - 1.0f,
                    EntityAttributeModifier.Operation.ADD_MULTIPLIED_TOTAL
            );
        }

        public EntityAttributeModifier getSpeedModifier() {
            return speedModifier;
        }

        public static HealthState fromHealthLevel(float healthLevel) {
            return switch ((int) healthLevel) {
                case 0, 1, 2 -> DYING;
                case 3, 4 -> CRIPPLED;
                case 5, 6 -> WOUNDED;
                case 7, 8, 9, 10 -> INJURED;
                default -> HEALTHY;
            };
        }
    }

    public enum AttackPower {
        DYING(0.25f),
        CRIPPLED(0.5f),
        WOUNDED(0.75f),
        INJURED(1.0f),
        HEALTHY(1.0f);

        private final EntityAttributeModifier speedModifier;

        AttackPower(float speedMultiplier) {
            this.speedModifier = new EntityAttributeModifier(
                    Identifier.of(ImMovensMod.MOD_ID, "attack_damage_speed_modifier"),
                    speedMultiplier - 1.0f,
                    EntityAttributeModifier.Operation.ADD_MULTIPLIED_TOTAL
            );
        }

        public EntityAttributeModifier getSpeedModifier() {
            return speedModifier;
        }

        public static AttackPower fromHealthLevel(float healthLevel) {
            return switch ((int) healthLevel) {
                case 0, 1, 2 -> DYING;
                case 3, 4 -> CRIPPLED;
                case 5, 6 -> WOUNDED;
                case 7, 8, 9, 10 -> INJURED;
                default -> HEALTHY;
            };
        }
    }
}
