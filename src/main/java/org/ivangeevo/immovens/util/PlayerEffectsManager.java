package org.ivangeevo.immovens.util;

import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

public class PlayerEffectsManager {

    private static final PlayerEffectsManager INSTANCE = new PlayerEffectsManager();

    private StatusEffectUtils.HungerState currentHungerState = StatusEffectUtils.HungerState.WELL_FED;
    private StatusEffectUtils.HealthState currentHealthState = StatusEffectUtils.HealthState.HEALTHY;
    private StatusEffectUtils.AttackPower currentAttackPower = StatusEffectUtils.AttackPower.HEALTHY;

    private static final int NAUSEA_TICKS = 100;

    private PlayerEffectsManager() {}

    public static PlayerEffectsManager getInstance() {
        return INSTANCE;
    }

    // gets called in PlayerEntity only
    public void onTick(PlayerEntity player)
    {

    }

    // gets called in ServerPlayerEntity only
    public void onServerTick(PlayerEntity player)
    {
        this.applyNauseaEffect(player);
        this.applyBlindnessEffect(player);
        this.updateSpeedAttributes(player);
        this.applySlowHealing(player);
    }

    public void disableJumpIfLow(PlayerEntity player, CallbackInfo ci)
    {
        if (player.getHungerManager().getFoodLevel() < 4 || player.getHealth() <= 4) { ci.cancel(); }
    }

    private void updateSpeedAttributes(PlayerEntity player) {
        EntityAttributeInstance movementSpeedAttribute = player.getAttributeInstance(EntityAttributes.GENERIC_MOVEMENT_SPEED);
        EntityAttributeInstance attackDamageAttribute = player.getAttributeInstance(EntityAttributes.GENERIC_ATTACK_DAMAGE);

        if (movementSpeedAttribute != null && !player.isCreative())
        {
            // Get the player's current hunger and health states
            StatusEffectUtils.HungerState newHungerState = StatusEffectUtils.HungerState.fromFoodLevel(player.getHungerManager().getFoodLevel());
            StatusEffectUtils.HealthState newHealthState = StatusEffectUtils.HealthState.fromHealthLevel(player.getHealth());
            StatusEffectUtils.AttackPower newAttackPower = StatusEffectUtils.AttackPower.fromHealthLevel(player.getHealth());

            // Update HungerState modifier
            if (newHungerState != currentHungerState) {
                movementSpeedAttribute.removeModifier(currentHungerState.getSpeedModifier());
                movementSpeedAttribute.addPersistentModifier(newHungerState.getSpeedModifier());
                currentHungerState = newHungerState;
            }

            // Update HealthState modifier
            if (newHealthState != currentHealthState) {
                movementSpeedAttribute.removeModifier(currentHealthState.getSpeedModifier());
                movementSpeedAttribute.addPersistentModifier(newHealthState.getSpeedModifier());
                currentHealthState = newHealthState;
            }

            // TODO: Add attack power modifier for the health states
            // Update AttackPower modifier
            /**
            if (newAttackPower != currentAttackPower) {
                attackDamageAttribute.removeModifier(currentAttackPower.getSpeedModifier());
                attackDamageAttribute.addPersistentModifier(newAttackPower.getSpeedModifier());
                currentAttackPower = newAttackPower;
            }
             **/
        }
    }

    private void applyNauseaEffect(PlayerEntity player)
    {
        if (player.getHungerManager().getFoodLevel() <= 0 && player.age % NAUSEA_TICKS == 0)
        {
            player.addStatusEffect(
                    new StatusEffectInstance(StatusEffects.NAUSEA, 50, 7, true, true)
            );
        }
    }

    private void applyBlindnessEffect(PlayerEntity player)
    {
        if (player instanceof ServerPlayerEntity && player.getHealth() <= 2)
        {
            // Additional effects for dying
            player.addStatusEffect(new StatusEffectInstance(StatusEffects.BLINDNESS, 200, 1));
        }
    }


    private void applySlowHealing(PlayerEntity player)
    {
        if (player.age % 600 == 0 && player.getHealth() < player.getMaxHealth()
                && player.getHungerManager().getFoodLevel() >= 9)
        {
            player.heal(1.0f);
        }
    }


}
