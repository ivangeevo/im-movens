package org.ivangeevo.crippled.mixin;

import net.minecraft.block.BlockState;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.HungerManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin extends LivingEntity
{
    @Shadow public abstract boolean isPlayer();
    @Shadow public abstract void jump();
    @Shadow public abstract HungerManager getHungerManager();
    @Shadow public abstract boolean damage(DamageSource source, float amount);

    protected PlayerEntityMixin(EntityType<? extends LivingEntity> entityType, World world)
    {
        super(entityType, world);
    }

    @Inject(method = "getBlockBreakingSpeed", at = @At(value = "TAIL"), cancellable = true)
    private void redirectGetBlockBreakingSpeed(BlockState block, CallbackInfoReturnable<Float> cir) {
        float originalSpeed = cir.getReturnValue(); // Get original speed

        int foodLevel = this.getHungerManager().getFoodLevel();
        float healthLevel = this.getHealth();
        float speedMultiplier = 1.0f;

        switch (foodLevel)
        {
            case 0,1:
            case 2: speedMultiplier *= 0.25f; break; // Starving
            case 3:
            case 4: speedMultiplier *= 0.5f; break; // Famished
            case 5:
            case 6: speedMultiplier *= 0.75f; break; // Hungry
            case 7:
            case 8: speedMultiplier *= 1.0f; break; // Peckish
            default: break;
        }

        // Logic for modifying speed based on health level
        switch ((int) healthLevel)
        {
            case 0,1:
            case 2: speedMultiplier *= 0.25f; break; // Dying
            case 3:
            case 4: speedMultiplier *= 0.5f; break; // Crippled
            case 5:
            case 6: speedMultiplier *= 0.75f; break; // Wounded
            case 7,8,9:
            case 10: speedMultiplier *= 1.0f; break; // Injured or Hurt
            default: break;
        }

        // Calculate the modified return value
        float modifiedSpeed = originalSpeed * speedMultiplier;

        cir.setReturnValue(modifiedSpeed); // Set the modified return value
    }

    @Inject(method = "getMovementSpeed", at = @At("RETURN"), cancellable = true)
    public void modifyMovementSpeed(CallbackInfoReturnable<Float> cir)
    {
        PlayerEntity player = (PlayerEntity) (Object) this;
        int foodLevel = player.getHungerManager().getFoodLevel();
        float healthLevel = player.getHealth();
        float speedMultiplier = 1.0f;

        // Logic for modifying speed based on hunger level
        switch (foodLevel)
        {
            case 0,1:
            case 2: speedMultiplier *= 0.25f; break; // Starving
            case 3:
            case 4: speedMultiplier *= 0.5f; break; // Famished
            /** Additional effects for famished **/
            // Loss of jump (in the jump method)
            case 5:
            case 6: speedMultiplier *= 0.75f; break; // Hungry
            // TODO: finish implementing this below
            /** Additional effects for hungry **/
            // player.disableHandCrank();  // Inability to use the Hand Crank

            case 7:
            case 8: speedMultiplier *= 1.0f; break; // Peckish
            /** Additional effects for peckish **/
            //TODO: set sprinting to false in another place
            //player.setSprinting(false); // Loss of sprint

            default: break;
        }

        // Logic for modifying speed based on health level
        if (healthLevel <= 2) // Dying
        {
            speedMultiplier *= 0.25f;

            // Additional effects for dying
            if (!this.getWorld().isClient)
            {
                player.addStatusEffect(new StatusEffectInstance(StatusEffects.BLINDNESS, 200, 1));
            }
        }
        else if (healthLevel <= 4) // Crippled
        {
            speedMultiplier *= 0.5f;
        }
        else if (healthLevel <= 6) // Wounded
        {
            speedMultiplier *= 0.75f;
        }
        else if (healthLevel <= 8) // Injured
        {
            speedMultiplier *= 1.0f;
        }
        else if (healthLevel <= 10) // Hurt
        {
            speedMultiplier *= 1.0f;
            // Additional effects for hurt
            //player.setSprinting(false); // Loss of sprint
        }

        cir.setReturnValue(cir.getReturnValue() * speedMultiplier);
    }

    @Inject(method = "jump", at = @At("HEAD"), cancellable = true)
    public void onJump(CallbackInfo ci)
    {
        PlayerEntity player = (PlayerEntity) (Object) this;
        int hungerLevel = player.getHungerManager().getFoodLevel();
        float healthLevel = player.getHealth();

        // Prevent jumping if hunger level or health is below 4 (2 shanks/hearts)
        if (hungerLevel < 4 || healthLevel <= 4)
        {
            ci.cancel();
        }
    }

    // Slowly healing the player every "x" ticks
    @Inject(method = "tick", at = @At("TAIL"))
    private void injectedTick(CallbackInfo ci)
    {
        PlayerEntity player = (PlayerEntity)(Object)this;

        int healTicks = 600;
        if (player.age % healTicks == 0 && player.getHealth() < player.getMaxHealth()
                && player.getHungerManager().getFoodLevel() >= 9)
        {
            player.heal(1.0F);
        }
    }

    @Inject(method = "tick", at = @At("HEAD"))
    private void injectedTickNausea(CallbackInfo ci)
    {
        int foodLevel = this.getHungerManager().getFoodLevel();

        int nauseaTicks = 100;

        if (foodLevel <= 0 && this.age % nauseaTicks == 0)
        {
            if ((PlayerEntity)(Object)this instanceof ServerPlayerEntity)
            {
                this.addStatusEffect(new StatusEffectInstance(StatusEffects.NAUSEA, 50, 7, true, true));

                //this.damage(this.getDamageSources().starve(),1);
            }
        }

    }



    /** --------------------------------------------------------------------- **/
}
