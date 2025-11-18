package org.ivangeevo.im_movens.mixin.client;

import com.mojang.authlib.GameProfile;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.ivangeevo.im_movens.ImMovensMod;
import org.ivangeevo.im_movens.client.FieldOfViewHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

/**
 * Mixin priority needs to be set high, otherwise there may be
 * issues with mods that modify getFovMultiplier as well. This
 * should run last when possible
 */
@Mixin(value = AbstractClientPlayerEntity.class, priority = 99999)
abstract class AbstractClientPlayerEntityMixin extends PlayerEntity {

    public AbstractClientPlayerEntityMixin(World world, BlockPos pos, float yaw, GameProfile gameProfile) {
        super(world, pos, yaw, gameProfile);
    }

    /**
     * Offsets the player's FOV based on what attribute modifiers are active
     * @param fovModifier FOV modifier affected by Im'movens
     * @return FOV modifier not affected by Im'movens, if conditions pass
     */
    @ModifyVariable(method = "getFovMultiplier", at = @At(value = "STORE", ordinal = 2), ordinal = 0)
    public float getFieldOfViewModifier(float fovModifier) {
        PlayerEntity player = MinecraftClient.getInstance().player;
        // FOV scaling disabled
        if (player != null && !ImMovensMod.getSettings().isFOVScalingEnabled()) {
            // Figure out the modifier without Im'movens, apply difference to existing modifier
            float newFov = FieldOfViewHelper.getFieldOfViewModifierWithoutSpeed(this);
            return fovModifier - (fovModifier - newFov);

        // FOV scaling enabled
        } else {
            return fovModifier;
        }
    }
}
