package org.ivangeevo.immovens.mixin.client;

import com.mojang.authlib.GameProfile;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.entity.player.HungerManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.ivangeevo.immovens.client.FieldOfViewHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(AbstractClientPlayerEntity.class)
abstract class AbstractClientPlayerEntityMixin extends PlayerEntity {

    public AbstractClientPlayerEntityMixin(World world, BlockPos pos, float yaw, GameProfile gameProfile) {
        super(world, pos, yaw, gameProfile);
    }

    @ModifyVariable(method = "getFovMultiplier", at = @At(value = "STORE", ordinal = 2), ordinal = 0)
    public float getFieldOfViewModifier(float fovModifier) {
        PlayerEntity player = MinecraftClient.getInstance().player;
        if (player != null) {
            return FieldOfViewHelper.getFieldOfViewModifierWithoutBlockSpeed(this);
        } else {
            return fovModifier;
        }
    }
}
