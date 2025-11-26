package org.ivangeevo.im_movens.util;

import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.random.Random;
import org.ivangeevo.im_movens.ImMovensMod;
import org.ivangeevo.im_movens.config.ImMovensConfig;
import org.ivangeevo.im_movens.sound.ImMovensSoundEvents;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

public class PlayerEffectsManager {

    private static final PlayerEffectsManager INSTANCE = new PlayerEffectsManager();

    private StatusEffectUtils.GenericState currentGenericState = StatusEffectUtils.GenericState.NORMAL;

    /**
     * Block distance threshold for pain sound to occur
     */
    private float distToNextHurtSound = 0.0f;
    /**
     * Health threshold for pain sound to occur
     */
    private final float THRESHOLD_FOR_NOISE = 6.0f;
    private static final int NAUSEA_TICKS = 100;

    private PlayerEffectsManager() {}

    public static PlayerEffectsManager getInstance() {
        return INSTANCE;
    }

    // gets called in PlayerEntity only
    public void onTick(PlayerEntity player) {
    }

    // gets called in ServerPlayerEntity only
    public void onServerTick(PlayerEntity player) {
        this.applyNauseaEffect(player);
        this.applyBlindnessEffect(player);
        this.updateAttributes(player);
        this.applySlowHealing(player);
        this.doHurtNoise(player);
    }

    // Determines if debuffs should affect player
    public boolean shouldBeAffected(PlayerEntity player) {
        return (!player.isCreative() && !player.isSpectator() && !player.isDead());
    }

    public void disableJumpIfLow(PlayerEntity player, CallbackInfo ci) {
        int foodLevel = player.getHungerManager().getFoodLevel();
        boolean fatCondition = false;
        if (ImMovensMod.isHungerGranular) {
            // this returns int.
            foodLevel = MathHelper.ceil(foodLevel / 3d);
            float fatLevel = player.getHungerManager().getSaturationLevel();
            fatCondition = fatLevel > 54
                    && ImMovensConfig.Settings.fatPenalties.get();
        }
        boolean hungerCondition = foodLevel <= 4
                && ImMovensConfig.Settings.hungerPenalties.get();
        boolean healthCondition = player.getHealth() <= 4
                && ImMovensConfig.Settings.healthPenalties.get();

        if ((hungerCondition || healthCondition || fatCondition) && shouldBeAffected(player))
        {
            ci.cancel();
        }
    }

    private void updateAttributes(PlayerEntity player) {
        EntityAttributeInstance movementSpeedAttribute = player.getAttributeInstance(EntityAttributes.GENERIC_MOVEMENT_SPEED);
        EntityAttributeInstance blockBreakSpeedAttribute = player.getAttributeInstance(EntityAttributes.PLAYER_BLOCK_BREAK_SPEED);
        EntityAttributeInstance attackDamageAttribute = player.getAttributeInstance(EntityAttributes.GENERIC_ATTACK_DAMAGE);

        // Get the player's current hunger and health states
        StatusEffectUtils.GenericState newGenericState = StatusEffectUtils.GenericState.getStateFromPlayerStats(player);

        if (movementSpeedAttribute != null) {
            // Update GenericState modifier
            if (newGenericState != currentGenericState) {
                movementSpeedAttribute.removeModifier(currentGenericState.getModifier());
                movementSpeedAttribute.addPersistentModifier(newGenericState.getModifier());
                setNextDistToHurtSound(player);

            }

            // Revert if player shouldn't be affected at this time
            if (!shouldBeAffected(player)) {
                movementSpeedAttribute.removeModifier(currentGenericState.getModifier());
            }
            else if (shouldBeAffected(player)) {
                if (!movementSpeedAttribute.hasModifier(currentGenericState.getModifier().id()))
                    movementSpeedAttribute.addPersistentModifier(newGenericState.getModifier());
            }

            Identifier legacyHungerModifierIdentifier = Identifier.of(ImMovensMod.MOD_ID, "hunger_speed_modifier");
            Identifier legacyHealthModifierIdentifier = Identifier.of(ImMovensMod.MOD_ID, "health_speed_modifier");
            //removes legacy modifiers
            if (movementSpeedAttribute.hasModifier(legacyHungerModifierIdentifier)) {
                movementSpeedAttribute.removeModifier(legacyHungerModifierIdentifier);
            }
            if (movementSpeedAttribute.hasModifier(legacyHealthModifierIdentifier)) {
                movementSpeedAttribute.removeModifier(legacyHealthModifierIdentifier);
            }
        }

        if (blockBreakSpeedAttribute != null) {
            // Update GenericState modifier
            if (newGenericState != currentGenericState) {
                blockBreakSpeedAttribute.removeModifier(currentGenericState.getModifier());
                blockBreakSpeedAttribute.addPersistentModifier(newGenericState.getModifier());
            }
        }

        if (attackDamageAttribute != null) {
            if (newGenericState != currentGenericState) {
                attackDamageAttribute.removeModifier(currentGenericState.getModifier());
                attackDamageAttribute.addPersistentModifier(newGenericState.getModifier());
            }
        }

        currentGenericState = newGenericState;
    }

    private void applyNauseaEffect(PlayerEntity player) {
        if (player.getHungerManager().getFoodLevel() <= 0
                && player.age % NAUSEA_TICKS == 0
                && ImMovensConfig.Settings.hungerPenalties.get())
        {
            player.addStatusEffect(
                    new StatusEffectInstance(StatusEffects.NAUSEA, 50, 7, true, true)
            );
        }
    }

    private void applyBlindnessEffect(PlayerEntity player) {
        if (player instanceof ServerPlayerEntity
                && player.getHealth() <= 2
                && ImMovensConfig.Settings.healthPenalties.get()
                && shouldBeAffected(player))
        {
            // Additional effects for dying
            player.addStatusEffect(new StatusEffectInstance(StatusEffects.BLINDNESS, 60, 0));
        }
    }


    private void applySlowHealing(PlayerEntity player) {
        if (player.age % 600 == 0 && player.getHealth() < player.getMaxHealth()
                && player.getHungerManager().getFoodLevel() >= 9
                && ImMovensConfig.Settings.naturalRegen.get()
                && shouldBeAffected(player))
        {
            player.heal(1.0f);
        }
    }

    /**
     * Emits a pain sound from provided PlayerEntity when certain conditions are met
     * @param player PlayerEntity to play sound from
     */
    private void doHurtNoise(PlayerEntity player) {
        // Process hurt sound check
        boolean doPainSound =
                // Pain sounds enabled
                ImMovensConfig.Settings.painSounds.get()
                    // Player is over the sound threshold
                    && (int) player.distanceTraveled > distToNextHurtSound
                    // Are health statuses enabled?
                    && ImMovensConfig.Settings.healthPenalties.get()
                    // Player's health is low enough
                    && player.getHealth() <= THRESHOLD_FOR_NOISE
                    // Player is not in creative/spectator mode
                    && shouldBeAffected(player);

        // Should the player do hurt sound when sneaking?
        boolean sneakingBlocks =
                player.isSneaking() && !ImMovensConfig.Settings.sneakingPainSounds.get();

        boolean flyingBlocks = player.isFallFlying();

        // General restriction check for when the player is moving
        // Should play hurt sound if sneaking or if not elytra flying
        boolean isRestrictedByMovement = sneakingBlocks || flyingBlocks;


        if ((doPainSound && !isRestrictedByMovement) && !player.getWorld().isClient) {
            // Player meets criteria for making hurt sound
            player.playSoundToPlayer(ImMovensSoundEvents.PLAYER_HURT, SoundCategory.PLAYERS,
                    0.5f, pitchFromHealth(player) + Random.create().nextFloat() * 0.1f);
            setNextDistToHurtSound(player);
        }
    }

    /**
     * Determines distance for hurt sound based on player's health
     * @param player PlayerEntity to pull distance from
     */
    public void setNextDistToHurtSound(PlayerEntity player) {
        distToNextHurtSound = player.distanceTraveled + (4.0f * (player.getHealth() / THRESHOLD_FOR_NOISE));
    }

    /**
     * Determines pitch of hurt sound based on player's health
     * @param player PlayerEntity to pull health info from
     * @return Pitch value
     */
    private float pitchFromHealth(PlayerEntity player) {
        return 0.55f + ((player.getHealth() / THRESHOLD_FOR_NOISE) * 0.45f);
    }

}