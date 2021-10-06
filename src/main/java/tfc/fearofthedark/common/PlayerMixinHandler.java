package tfc.fearofthedark.common;

import net.minecraft.entity.attribute.AttributeContainer;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.player.PlayerEntity;

import java.util.UUID;

public class PlayerMixinHandler {
	public static int getTimeFactor(int phase) {
		return switch (phase) {
//			case 4 -> 160;
			case 4 -> 200;
//			case 4 -> 320;
//			case 3 -> 80;
//			case 3 -> 160;
			case 3 -> 140;
//			case 2 -> 40;
			case 2 -> 80;
			case 1 -> 40;
//			case 1 -> 20;
			default -> -1;
		};
	}
	
	public static float getScaleFactor(float fearFactor) {
		return 120 * fearFactor;
	}
	
	public static void addAttributes(PlayerEntity entity) {
		AttributeContainer container = (entity).getAttributes();
		if (container == null) return;
		
		int ticksUneasy = (int) (PlayerMixinHandler.getTimeFactor(1) * PlayerMixinHandler.getScaleFactor(((IHaveFear)entity).FearOfTheDark_getFactor()));
		int ticksStress = (int) (PlayerMixinHandler.getTimeFactor(4) * PlayerMixinHandler.getScaleFactor(((IHaveFear)entity).FearOfTheDark_getFactor()));
		double ticksElapse = ticksStress - ticksUneasy;
		
		ticksElapse = (((IHaveFear)entity).FearOfTheDark_getTicks() - ticksUneasy) / ticksElapse;
		
		
		EntityAttributeModifier modifier = new EntityAttributeModifier(UUID.fromString("4d9d898d-8589-40d1-aeb3-f286bbf39d32"), "fearofthedark:speed", -((ticksElapse / 10f)), EntityAttributeModifier.Operation.MULTIPLY_TOTAL);
		EntityAttributeInstance speed = container.getCustomInstance(EntityAttributes.GENERIC_MOVEMENT_SPEED);
		if (speed != null) {
			if (speed.hasModifier(modifier)) speed.removeModifier(modifier);
			speed.addTemporaryModifier(modifier);
		}
		
		EntityAttributeModifier modifierHaste = new EntityAttributeModifier(UUID.fromString("4d9d898d-8589-40d1-aeb3-f286bbf39d32"), "fearofthedark:attack_damage", -((ticksElapse / 10f)), EntityAttributeModifier.Operation.MULTIPLY_TOTAL);
		EntityAttributeInstance attackSpeed = container.getCustomInstance(EntityAttributes.GENERIC_ATTACK_DAMAGE);
		if (attackSpeed != null) {
			if (attackSpeed.hasModifier(modifierHaste)) attackSpeed.removeModifier(modifierHaste);
			attackSpeed.addTemporaryModifier(modifierHaste);
		}
	}
}
