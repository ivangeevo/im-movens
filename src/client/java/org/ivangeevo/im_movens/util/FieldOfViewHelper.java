package org.ivangeevo.im_movens.util;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.player.PlayerEntity;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Code adapted and modified from BlockRunner (MPL-2.0 License)
 * @link <a href="https://github.com/Fuzss/blockrunner/blob/main/1.21.1/Common/src/main/java/fuzs/blockrunner/client/helper/FieldOfViewHelper.java">Source</a>
 */

public class FieldOfViewHelper {

    /**
     * Returns calculated field of view excluding mod attribute modifiers
     * @param player PlayerEntity to pull abilities from
     * @return FOV modifier without Im'movens, if conditions pass
     */
    public static float getFieldOfViewModifierWithoutSpeed(PlayerEntity player) {
        float fovModifier = player.getAbilities().flying ? 1.1F : 1.0F;
        EntityAttributeInstance attribute = player.getAttributeInstance(EntityAttributes.GENERIC_MOVEMENT_SPEED);
        if (attribute != null) {
            double movementSpeed = calculateAttributeValueSkipping(attribute);
            fovModifier *= ((float) movementSpeed / player.getAbilities().getWalkSpeed() + 1.0F) / 2.0F;
        }
        return fovModifier;
    }

    /**
     * Calculates attribute value without modifiers outside the Minecraft namespace
     * @param attribute Generic movement speed attribute instance
     * @return Movement speed attribute value, with those from Im'movens excluded
     */
    private static double calculateAttributeValueSkipping(EntityAttributeInstance attribute) {
        double baseValue = attribute.getBaseValue();

        Map<EntityAttributeModifier.Operation, Set<EntityAttributeModifier>> operationToModifiers = Stream.of(
                EntityAttributeModifier.Operation.values()).collect(
                Collectors.toMap(Function.identity(), operation -> Sets.newHashSet(), (o1, o2) -> o1,
                        () -> Maps.newEnumMap(EntityAttributeModifier.Operation.class)
                ));

        // Gets all movement modifiers, excluding those outside of the Minecraft namespace (so anything from Im'movens)
        attribute.getModifiers()
                .stream()
                .filter(modifier -> StringUtils.equals(modifier.id().getNamespace(), "minecraft"))
                .forEach(modifier -> operationToModifiers.get(modifier.operation()).add(modifier));

        // Append all modifiers
        for (EntityAttributeModifier attributeModifier : operationToModifiers.get(EntityAttributeModifier.Operation.ADD_VALUE)) {
            baseValue += attributeModifier.value();
        }

        double baseValueCopy = baseValue;

        for (EntityAttributeModifier attributeModifier : operationToModifiers.get(
                EntityAttributeModifier.Operation.ADD_MULTIPLIED_BASE)) {
            baseValueCopy += baseValue * attributeModifier.value();
        }

        for (EntityAttributeModifier attributeModifier : operationToModifiers.get(
                EntityAttributeModifier.Operation.ADD_MULTIPLIED_TOTAL)) {
            baseValueCopy *= 1.0 + attributeModifier.value();
        }

        return attribute.getAttribute().value().clamp(baseValueCopy);
    }

}