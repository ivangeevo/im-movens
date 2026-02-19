package org.btwr.im_movens.util;

import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import org.btwr.im_movens.ImMovensMod;
import org.btwr.im_movens.config.ImMovensConfig;

public class StatusEffectUtils {

    public enum GenericState {
        SEVERE(0.25f, "severe_mod"),
        MAJOR(0.50f, "major_mod"),
        MINOR(0.75f, "minor_mod"),
        NORMAL(1.00f, null);

        private final EntityAttributeModifier modifier;

        GenericState(float multiplier, String name) {
            if (name == null) {
                this.modifier = null;
            }
            else {
                this.modifier = new EntityAttributeModifier(
                        Identifier.of(ImMovensMod.MOD_ID, name),
                        multiplier - 1f,
                        EntityAttributeModifier.Operation.ADD_MULTIPLIED_TOTAL
                );
            }
        }

        public EntityAttributeModifier getModifier() {
            return modifier;
        }

        public static GenericState getSeverityState(int severity) {
            return switch (severity) {
                case 1 -> MINOR;
                case 2 -> MAJOR;
                case 3 -> SEVERE;
	            default -> NORMAL;
            };
        }
        public static GenericState getStateFromPlayerStats(PlayerEntity player) {
            int foodLevel = player.getHungerManager().getFoodLevel();
            float healthLevel = player.getHealth();
            int severity = 0;
            if (ImMovensMod.isHungerGranular) {
                // this returns int.
                foodLevel = MathHelper.ceil(foodLevel / 3d);
                float fatLevel = player.getHungerManager().getSaturationLevel();
                severity = ImMovensConfig.fatPenalties.get() ?
                    switch (MathHelper.ceil(fatLevel/6f)) {
                    case 8 -> 1;
                    case 9 -> 2;
                    case 10 -> 3;
                    default -> 0;
                } : 0;
            }
            severity = Math.max(severity, ImMovensConfig.hungerPenalties.get() ?
                switch (foodLevel) {
                case 0, 1, 2 -> 3;
                case 3, 4 -> 2;
                case 5, 6 -> 1;
                default -> 0;
            } : 0);
            severity = Math.max(severity, ImMovensConfig.healthPenalties.get() ? switch (MathHelper.ceil(healthLevel)) {
                case 0, 1, 2, 3, 4 -> 3;
                case 5, 6 -> 2;
                case 7, 8 -> 1;
                default -> 0;
            } : 0);
            return getSeverityState(severity);
        }
    }

    public enum AttackPower {
        DYING(-1),
        CRIPPLED(0),
        WOUNDED(1),
        INJURED(2),
        HEALTHY(2);

        private final EntityAttributeModifier attackModifier;

        AttackPower(int value) {
            this.attackModifier = new EntityAttributeModifier(
                    Identifier.of(ImMovensMod.MOD_ID, "attack_damage_modifier"),
                    value,
                    EntityAttributeModifier.Operation.ADD_VALUE
            );
        }

        public EntityAttributeModifier getAttackModifier() {
            return attackModifier;
        }

        public static AttackPower fromHealthLevel(float healthLevel) {
            return switch (MathHelper.ceil(healthLevel)) {
                case 0, 1, 2 -> DYING;
                case 3, 4 -> CRIPPLED;
                case 5, 6 -> WOUNDED;
                case 7, 8, 9, 10 -> INJURED;
                default -> HEALTHY;
            };
        }
    }

}