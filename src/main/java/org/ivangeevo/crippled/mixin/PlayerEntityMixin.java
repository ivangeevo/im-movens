package org.ivangeevo.crippled.mixin;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.HungerManager;
import net.minecraft.entity.player.PlayerAbilities;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Final;
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



    @Inject(method = "getMovementSpeed", at = @At("RETURN"), cancellable = true)
    public void modifyMovementSpeed(CallbackInfoReturnable<Float> cir)
    {
        PlayerEntity player = (PlayerEntity) (Object) this;
        int hungerLevel = player.getHungerManager().getFoodLevel();
        float healthLevel = player.getHealth();
        float speedMultiplier = 1.0f;

        // Logic for modifying speed based on hunger level
        if (hungerLevel <= 2) // Starving
        {
            speedMultiplier *= 0.25f;
            /** Additional effects for starving **/
        }
        else if (hungerLevel <= 4) // Famished
        {
            speedMultiplier *= 0.5f;
            /** Additional effects for famished **/
            // Loss of jump (in the jump method)
        }
        else if (hungerLevel <= 6)  // Hungry
        {
            speedMultiplier *= 0.75f;
            // TODO: finish implementing this below
            /** Additional effects for hungry **/
            // player.disableHandCrank();  // Inability to use the Hand Crank
        }
        else if (hungerLevel <= 8) // Peckish
        {
            speedMultiplier *= 1.0f;
            /** Additional effects for peckish **/
            player.setSprinting(false); // Loss of sprint
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
    public void onJump(CallbackInfo ci) {
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
    private void injectedTick(CallbackInfo ci) {
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

        if (foodLevel <= 0 && !this.getWorld().isClient)
        {
            if (this.age % nauseaTicks == 0)
            {
                this.addStatusEffect(new StatusEffectInstance(StatusEffects.NAUSEA, 40, 5,true, true));

                this.damage(this.getDamageSources().starve(),1);
            }
        }

    }



    /** --------------------------------------------------------------------- **/
}
