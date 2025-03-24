package org.ivangeevo.immovens.client;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.resource.Resource;
import net.minecraft.util.Identifier;
import org.apache.commons.lang3.ArrayUtils;
import org.ivangeevo.immovens.ImMovensMod;
import org.ivangeevo.immovens.config.ImMovensConfig;
import org.ivangeevo.immovens.config.ModSettings;

import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FieldOfViewHelper {

    public static float getFieldOfViewModifierWithoutSpeed(PlayerEntity player) {
        float fovModifier = player.getAbilities().flying ? 1.1F : 1.0F;
        EntityAttributeInstance attribute = player.getAttributeInstance(EntityAttributes.GENERIC_MOVEMENT_SPEED);
        if (attribute != null) {
            double movementSpeed = calculateAttributeValueSkipping(attribute,
                    Identifier.of(ImMovensMod.MOD_ID, "health_speed_modifier"),
                    Identifier.of(ImMovensMod.MOD_ID, "hunger_speed_modifier")
            );
            fovModifier *= ((float) movementSpeed / player.getAbilities().getWalkSpeed() + 1.0F) / 2.0F;
        }
        return fovModifier;
    }

    private static double calculateAttributeValueSkipping(EntityAttributeInstance attribute, Identifier... skippedModifiers) {

        double baseValue = attribute.getBaseValue();

        Map<EntityAttributeModifier.Operation, Set<EntityAttributeModifier>> operationToModifiers = Stream.of(
                EntityAttributeModifier.Operation.values()).collect(
                Collectors.toMap(Function.identity(), operation -> Sets.newHashSet(), (o1, o2) -> o1,
                        () -> Maps.newEnumMap(EntityAttributeModifier.Operation.class)
                ));
        attribute.getModifiers()
                .stream()
                .filter(modifier -> !ArrayUtils.contains(skippedModifiers, modifier.id()))
                .forEach(modifier -> operationToModifiers.get(modifier.operation()).add(modifier));

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