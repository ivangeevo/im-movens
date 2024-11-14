package org.ivangeevo.immovens.mixin;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import org.ivangeevo.immovens.util.PlayerEffectsManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayerEntity.class)
public abstract class ServerPlayerEntityMixin
{
    @Unique private PlayerEntity playerEntity = (PlayerEntity) (Object) this;

    @Unique private PlayerEffectsManager effectsManager = PlayerEffectsManager.getInstance();

    @Inject(method = "tick", at = @At("TAIL"))
    public void applyMovementEffectsOnTick(CallbackInfo info)
    {
        effectsManager.onServerTick(playerEntity);
    }

}
