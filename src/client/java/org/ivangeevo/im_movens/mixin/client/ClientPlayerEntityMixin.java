package org.ivangeevo.im_movens.mixin.client;

import net.minecraft.client.network.ClientPlayerEntity;
import org.ivangeevo.im_movens.config.ImMovensConfig;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(ClientPlayerEntity.class)
public abstract class ClientPlayerEntityMixin {

    @ModifyConstant(method = "canSprint", constant = @Constant(floatValue = 6.0f))
    private float setHungerRequired(float constant) {
        double dValue = ImMovensConfig.Settings.sprintingDisabledHungerLevel.get();
        return (float) dValue;
    }

}
