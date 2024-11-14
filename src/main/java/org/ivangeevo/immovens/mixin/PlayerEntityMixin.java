package org.ivangeevo.immovens.mixin;

import net.minecraft.entity.player.PlayerEntity;
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

    @Inject(method = "jump", at = @At("HEAD"), cancellable = true)
    public void onJump(CallbackInfo ci) {
        effectsManager.disableJumpIfLow(playerEntity, ci);
    }

    /** Modify food exhaustion values for jumping and jump sprinting **/
    // Sprint jumping
    @ModifyConstant(method = "jump", constant = @Constant(floatValue = 0.2f))
    private float modifySprintJump(float constant){
        return 1.00f;
    }

    // Regular jumping
    @ModifyConstant(method = "jump", constant = @Constant(floatValue = 0.05f))
    private float modifyJump(float constant){
        return 0.40f;
    }



    /** --------------------------------------------------------------------- **/
}
