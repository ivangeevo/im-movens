package org.ivangeevo.immovens.mixin;

import com.mojang.authlib.GameProfile;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.packet.c2s.common.SyncedClientOptions;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.GameMode;
import org.ivangeevo.immovens.ImMovensMod;
import org.ivangeevo.immovens.client.ImMovensSound;
import org.ivangeevo.immovens.config.ModSettings;
import org.ivangeevo.immovens.util.PlayerEffectsManager;
import org.ivangeevo.immovens.util.StatusEffectUtils;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ServerPlayerEntity.class)
public abstract class ServerPlayerEntityMixin
{
    @Unique private PlayerEntity playerEntity = (PlayerEntity) (Object) this;

    @Unique private PlayerEffectsManager effectsManager = PlayerEffectsManager.getInstance();

    @Unique private float distToNextHurtSound = 1.6f;

    @Inject(method = "<init>", at = @At("TAIL"))
    public void calcDist(MinecraftServer server, ServerWorld world, GameProfile profile,
                         SyncedClientOptions clientOptions, CallbackInfo ci) {
        setNextDistToHurtSound();
    }

    @Inject(method = "tick", at = @At("TAIL"))
    public void applyMovementEffectsOnTick(CallbackInfo info)
    {
        effectsManager.onServerTick(playerEntity);
        boolean doHurt = (int) playerEntity.distanceTraveled > distToNextHurtSound &&
                ImMovensMod.getInstance().settings.hasPainSounds() &&
                playerEntity.getHealth() < 8.0 && shouldBeAffected(playerEntity);
        if (doHurt && !playerEntity.getWorld().isClient) {
            playerEntity.playSoundToPlayer(ImMovensSound.PLAYER_HURT, SoundCategory.PLAYERS,
                    0.5f, pitchFromHealth() + Random.create().nextFloat() * 0.1f);
            setNextDistToHurtSound();
        }
    }

    @Unique
    private boolean shouldBeAffected(PlayerEntity player) {
        return PlayerEffectsManager.getInstance().shouldBeAffected(player);
    }

    @Unique
    private void setNextDistToHurtSound() {
        distToNextHurtSound = playerEntity.distanceTraveled + (
                10.0f * (playerEntity.getHealth() / (playerEntity.getMaxHealth() * 2)));
    }

    @Unique
    private float pitchFromHealth() {
        return 0.65f + (playerEntity.getHealth() / (playerEntity.getMaxHealth() * 4));
    }
}
