package org.btwr.im_movens.mixin;

import com.mojang.authlib.GameProfile;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.packet.c2s.common.SyncedClientOptions;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import org.btwr.im_movens.util.PlayerEffectsManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayerEntity.class)
public abstract class ServerPlayerEntityMixin {

    @Unique private PlayerEntity playerEntity = (PlayerEntity)(Object)this;
    @Unique private PlayerEffectsManager effectsManager = PlayerEffectsManager.getInstance();

    @Inject(method = "<init>", at = @At("TAIL"))
    public void calcDist(MinecraftServer server, ServerWorld world, GameProfile profile, SyncedClientOptions clientOptions, CallbackInfo ci) {
        effectsManager.setNextDistToHurtSound(playerEntity);
    }

    @Inject(method = "tick", at = @At("TAIL"))
    public void applyMovementEffectsOnTick(CallbackInfo info) {
        effectsManager.onServerTick(playerEntity);
    }

}