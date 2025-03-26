package org.ivangeevo.immovens.mixin;

import net.minecraft.entity.player.PlayerEntity;
import org.ivangeevo.immovens.ImMovensMod;
import org.ivangeevo.immovens.util.PlayerEffectsManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin
{

    @Unique private PlayerEntity playerEntity = (PlayerEntity) (Object) this;

    @Unique private PlayerEffectsManager effectsManager = PlayerEffectsManager.getInstance();

    /**
     * Disables jumping if health or hunger is low
     */
    @Inject(method = "jump", at = @At("HEAD"), cancellable = true)
    public void onJump(CallbackInfo ci) {
        effectsManager.disableJumpIfLow(playerEntity, ci);
    }

    /**
     * Modify food exhaustion value for sprint jumping,
     * if Hardcore Exhaustion is enabled
     **/
    @ModifyConstant(method = "jump", constant = @Constant(floatValue = 0.2f))
    private float modifySprintJump(float constant){
        return (ImMovensMod.getSettings().isHardcoreHungerEnabled()) ? 1.00f : constant;
    }

    /**
     * Modify food exhaustion value for normal jumping,
     * if Hardcore Exhaustion is enabled
     **/
    @ModifyConstant(method = "jump", constant = @Constant(floatValue = 0.05f))
    private float modifyJump(float constant){
        return (ImMovensMod.getSettings().isHardcoreHungerEnabled()) ? 0.40f : constant;
    }
}
