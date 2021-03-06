package tfc.fearofthedark.common;

import com.mojang.datafixers.util.Pair;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.network.packet.s2c.play.PlaySoundS2CPacket;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.*;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;
import tfc.fearofthedark.styles.*;
import tfc.stylesplusplus.api.ExtraStyle;
import tfc.stylesplusplus.api.ExtraStyleData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class ServerPlayerEntityMixinHandler {
	public static int getRecoveryFactor(int sky, int block) {
		int brightness = Math.max(sky + 1, block);
//		brightness = 15 - brightness;
//		brightness = 8 - brightness;
		brightness -= 8;
		return (int) (100 * (brightness / 7f));
	}
	
	private static final HashMap<Identifier, ExtraStyle> styleMap = new HashMap<>();
	
	static {
		styleMap.put(new Identifier("nervousness:0"), new TorchStyle());
		styleMap.put(new Identifier("nervousness:1"), new ShakyStyle());
		styleMap.put(new Identifier("nervousness:2"), new ShakyStyle());
		
		styleMap.put(new Identifier("paranoia:0"), new FocusStyle());
		styleMap.put(new Identifier("paranoia:2"), new ShakyStyle());
		
		{
			styleMap.put(new Identifier("fear:0"), new FearStyle());
			styleMap.put(new Identifier("fear:2"), new WantStyle());
			styleMap.put(new Identifier("fear:3"), new TorchStyle());
			TorchStyle style = new TorchStyle();
			style.speed *= 2;
			styleMap.put(new Identifier("fear:4"), style);
			styleMap.put(new Identifier("fear:5"), new FollowStyle());
		}
		
		styleMap.put(new Identifier("stress:1"), new HeartStyle());
		styleMap.put(new Identifier("stress:5"), new AloneStyle());
	}
	
	public static boolean checkPhase(ServerPlayerEntity player, int phase, int ticksInDark, int phaseNumber, int ticks, String name, int messageCount) {
		if (phase != phaseNumber && ticksInDark >= ticks) {
			if (phase < phaseNumber) {
				MutableText text = new LiteralText("").setStyle(Style.EMPTY);
				
				{
					MutableText itIsDark = new TranslatableText("fearofthedark.dark.prompt");
//					Style style = Style.EMPTY.withColor(TextColor.fromRgb(4210752));
					Style style = Style.EMPTY.withColor(Formatting.DARK_GRAY);
//					new Color(2105376);
					// TODO: randomize style
					SwingingLightStyle lightStyle = new SwingingLightStyle();
					((ExtraStyleData) style).addStyle(lightStyle);
					itIsDark.setStyle(style);
					
					text.append(itIsDark);
				}

//				text.setStyle(Style.EMPTY).withFormatting(Formatting.RESET);
				{
					Random random = new Random();
					int index = random.nextInt(messageCount + 1);
					MutableText child = new TranslatableText("fearofthedark." + name + "." + index);
					Style style = Style.EMPTY.withBold(null).withColor(Formatting.DARK_GRAY);
					ExtraStyle extraStyle = styleMap.getOrDefault(new Identifier(name, "" + index), null);
					if (extraStyle != null) ((ExtraStyleData) style).getExtraStyles().add(extraStyle.copy());
					child.setStyle(style);
					{
						// TODO: figure this out
						HoverEvent event = new HoverEvent(
								HoverEvent.Action.SHOW_TEXT,
								new TranslatableText("fearofthedark." + name + "." + index)
						);
						style.withHoverEvent(event);
					}
					text.append(child);
				}
				
				player.sendMessage(text, false);
			}
			((IHavePhase) player).FearOfTheDark_setPhase(phaseNumber);
			return true;
		}
		return phaseNumber == phase && ticksInDark >= ticks;
	}
	
	private static final ArrayList<DamageSource> damageSources = new ArrayList<>();
	
	public static final ArrayList<Pair<SoundEvent, Float>> hostileEvents = new ArrayList<>();
	
	static {
		for (int i = 0; i < 3; i++) {
			damageSources.add(new DamageSource("fearofthedark.death." + i));
		}
		
		hostileEvents.add(new Pair<>(SoundEvents.ENTITY_ZOMBIE_AMBIENT, 0.5f));
		hostileEvents.add(new Pair<>(SoundEvents.ENTITY_SKELETON_AMBIENT, 0.3f));
		hostileEvents.add(new Pair<>(SoundEvents.ENTITY_SKELETON_SHOOT, 0.1f));
		hostileEvents.add(new Pair<>(SoundEvents.ENTITY_SPIDER_AMBIENT, 0.3f));
		hostileEvents.add(new Pair<>(SoundEvents.ENTITY_SPIDER_STEP, 0.34f));
		hostileEvents.add(new Pair<>(SoundEvents.ENTITY_CREEPER_PRIMED, 0.1f));
		hostileEvents.add(new Pair<>(SoundEvents.ENTITY_ENDERMAN_AMBIENT, 0.25f));
		hostileEvents.add(new Pair<>(SoundEvents.ENTITY_ENDERMAN_SCREAM, 0.1f));
	}
	
	public static DamageSource getSource() {
		return damageSources.get(new Random().nextInt(damageSources.size()));
	}
	
	public static SoundEvent getSound() {
		for (Pair<SoundEvent, Float> hostileEvent : hostileEvents) {
			float chance = hostileEvent.getSecond();
			if (new Random().nextFloat() <= chance) return hostileEvent.getFirst();
		}
		// no reason adding it as a 100% chance when I can just return it without using rng
		return SoundEvents.ENTITY_ZOMBIE_AMBIENT;
	}
	
	public static void doEffects(int phase, int ticksInDark, int age, int sky, int block, ServerPlayerEntity entity) {
		switch (phase) {
			case 4:
				if ((20 / 40f) - (entity.getHealth() / 40f) > 0 && (age % 10 == 0))
					entity.damage(getSource(), Math.max((1) - (entity.getHealth() / 20f), 0.25f));
				else {
					int ticksStress = ticksInDark - (int) (PlayerMixinHandler.getTimeFactor(4) * PlayerMixinHandler.getScaleFactor(((IHaveFear) entity).FearOfTheDark_getFactor()));
					if (ticksStress >= 4000) entity.damage(getSource(), (20 / 40f));
				}
			case 3:
				if (age % 400 == 0 && new Random().nextInt(30) == 1) {
//					entity.playSound(SoundEvents.AMBIENT_CAVE, SoundCategory.AMBIENT, ((new Random().nextFloat() / 2f) - 0.25f + 1), ((new Random().nextFloat() / 2f) - 0.25f + 1));
					// do nothing
				} else if (age % 500 == 0 && new Random().nextInt(34) == 1) {
					Vec3d look = entity.getRotationVector();
					look = look.multiply(1, 0, 1).normalize();
					Random r = new Random();
					look = look.rotateY((float) Math.toRadians(r.nextInt(90) - 45));
					look = look.multiply(r.nextInt(8) + 8);
					entity.networkHandler.sendPacket(new PlaySoundS2CPacket(getSound(), SoundCategory.HOSTILE, entity.getX() - look.x, entity.getY(), entity.getZ() - look.z, ((new Random().nextFloat() / 2f) - 0.25f + 1), ((new Random().nextFloat() / 2f) - 0.25f + 1)));
//				} else if (age % 500 == 0 && new Random().nextInt(34) == 1) {
				} else if (age % 500 == 0 && new Random().nextInt(24) == 1) {
					if (sky == 15 && block <= 4 && entity.getServerWorld().isNight()) {
						Vec3d look = entity.getRotationVector();
						look = look.multiply(1, 0, 1).normalize();
						Random r = new Random();
						look = look.rotateY((float) Math.toRadians(r.nextInt(90) - 45));
						look = look.multiply(r.nextInt(4) + 4).normalize().multiply(16);
						entity.networkHandler.sendPacket(new PlaySoundS2CPacket(SoundEvents.ENTITY_PHANTOM_SWOOP, SoundCategory.HOSTILE, entity.getX() - look.x, entity.getY() + 64, entity.getZ() - look.z, ((new Random().nextFloat() / 2f) - 0.25f + 7.25f), ((new Random().nextFloat() / 2f) - 0.25f + 1)));
					}
				}
				if (ticksInDark == (phase * 30) && new Random().nextInt(30) <= 3) {
					if (entity.getHealth() > 1) entity.damage(getSource(), 0.1f);
				}
			case 2:
			case 1:
			case 0:
		}
	}
}
