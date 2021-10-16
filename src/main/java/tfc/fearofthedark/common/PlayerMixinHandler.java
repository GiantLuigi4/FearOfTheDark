package tfc.fearofthedark.common;

import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.sound.BiomeEffectSoundPlayer;
import net.minecraft.client.util.ClientPlayerTickable;
import net.minecraft.entity.attribute.AttributeContainer;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.LiteralText;
import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;
import net.minecraft.util.Util;
import tfc.fearofthedark.styles.FadeStyle;
import tfc.fearofthedark.styles.SwingingLightStyle;
import tfc.stylesplusplus.api.ExtraStyle;
import tfc.stylesplusplus.api.ExtraStyleData;

import java.util.Random;
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
	
	public static void onTick(PlayerEntity entity) {
		IHaveFear fearHolder = (IHaveFear) entity;
		
		if (fearHolder.FearOfTheDark_getPhase() >= 3) {
			if (entity.age % 300 == 3 && new Random().nextInt(13) == 3) {
				if (entity instanceof ClientPlayerInfo) {
					if (((ClientPlayerInfo) entity).FearOfTheDark_isPocketAware()) {
						((ClientPlayerInfo) entity).FearOfTheDark_setPocketAware(false);
						{
							MutableText text = new LiteralText("").setStyle(Style.EMPTY);
							
							{
								MutableText itIsDark = new TranslatableText("fearofthedark.dark.prompt");
								Style style = Style.EMPTY.withColor(Formatting.DARK_GRAY);
								// TODO: randomize style
								SwingingLightStyle lightStyle = new SwingingLightStyle();
								((ExtraStyleData) style).addStyle(lightStyle);
								itIsDark.setStyle(style);
								
								text.append(itIsDark);
							}
							
							{
								MutableText child = new TranslatableText("fearofthedark.event.track");
								Style style = Style.EMPTY.withBold(null).withColor(Formatting.DARK_GRAY);
								ExtraStyle extraStyle = new FadeStyle();
								((ExtraStyleData) style).getExtraStyles().add(extraStyle.copy());
								child.setStyle(style);
								text.append(child);
							}
							
							entity.sendSystemMessage(text, Util.NIL_UUID);
						}
					}
				}
			}
		} else {
			if (!((ClientPlayerInfo) entity).FearOfTheDark_isPocketAware()) ((ClientPlayerInfo) entity).FearOfTheDark_setPocketAware(true);
		}
		
		{
			ClientPlayerTickable clientPlayerTickable = null;
			for (ClientPlayerTickable tickable : ((ClientPlayerEntity) entity).tickables)
				if (tickable instanceof BiomeEffectSoundPlayer)
					clientPlayerTickable = tickable;
			
			if (clientPlayerTickable instanceof BiomeEffectSoundPlayer)
				((BiomeEffectSoundPlayer) clientPlayerTickable).moodPercentage += (getTimeFactor(fearHolder.FearOfTheDark_getPhase()) / (float) getTimeFactor(4)) / 1000f;
		}
		
//		if (FearOfTheDarkClient.shaderUtilIntegration) {
//			if (entity.getEntityWorld().getLightLevel(LightType.SKY, (entity).getBlockPos().up()) >= 5 && entity.world.isDay()) ((ClientPlayerInfo)entity).setTicksOutOfSun(((ClientPlayerInfo)entity).getTicksOutOfSun() - 10);
//			else if (entity.getEntityWorld().getLightLevel(LightType.BLOCK, (entity).getBlockPos().up()) >= 6) ((ClientPlayerInfo)entity).setTicksOutOfSun(((ClientPlayerInfo)entity).getTicksOutOfSun() - 1);
//			else ((ClientPlayerInfo)entity).setTicksOutOfSun(((ClientPlayerInfo)entity).getTicksOutOfSun() + 2);
//
//			((ClientPlayerInfo)entity).setTicksOutOfSun(MathHelper.clamp(((ClientPlayerInfo)entity).getTicksOutOfSun(), 0, (2000 * 2)));
//
//			PostProcessShader shader;
//
//			float t = ((ClientPlayerInfo)entity).getTicksOutOfSun() / (2000f * 2);
//			float blurAmt = t * 10f;
//			if (entity.getEntityWorld().getLightLevel(LightType.SKY, (entity).getBlockPos().up()) <= 8 || !entity.world.isDay()) blurAmt = 0;
//
//			float lblur = ((ClientPlayerInfo)entity).getLastBlur();
//			if (blurAmt != 10) lblur = MathHelper.lerp((10 - (blurAmt - lblur)) / 1.5f, lblur, blurAmt);
//
//			blurAmt = lblur;
//
//			if (PostProcessUtils.hasPass(new Identifier("fearofthedark:blur_x"))) shader = PostProcessUtils.getPass(new Identifier("fearofthedark:blur_x"));
//			else shader = PostProcessUtils.addPass(new Identifier("fearofthedark:blur_x"), new Identifier("minecraft:blur"));
//			shader.getProgram().enable();
//			shader.getProgram().getUniformByNameOrDummy("BlurDir").set(1f, 0f);
//			shader.getProgram().getUniformByNameOrDummy("Radius").set((float) (int) blurAmt);
//			shader.getProgram().disable();
//
//			if (PostProcessUtils.hasPass(new Identifier("fearofthedark:blur_y"))) shader = PostProcessUtils.getPass(new Identifier("fearofthedark:blur_y"));
//			else shader = PostProcessUtils.addPass(new Identifier("fearofthedark:blur_y"), new Identifier("minecraft:blur"));
//			shader.getProgram().enable();
//			shader.getProgram().getUniformByNameOrDummy("BlurDir").set(0f, 1f);
//			shader.getProgram().getUniformByNameOrDummy("Radius").set(0f);
//			shader.getProgram().disable();
//		}
		
		AttributeContainer container = (entity).getAttributes();
		if (container == null) return;
		
		int ticksUneasy = (int) (PlayerMixinHandler.getTimeFactor(1) * PlayerMixinHandler.getScaleFactor(((IHaveFear)entity).FearOfTheDark_getFactor()));
		int ticksStress = (int) (PlayerMixinHandler.getTimeFactor(4) * PlayerMixinHandler.getScaleFactor(((IHaveFear)entity).FearOfTheDark_getFactor()));
		double ticksElapse = ticksStress - ticksUneasy;
		
		ticksElapse = (((IHaveFear)entity).FearOfTheDark_getTicks() - ticksUneasy) / ticksElapse;
		
		// https://www.uuidgenerator.net/version4
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
