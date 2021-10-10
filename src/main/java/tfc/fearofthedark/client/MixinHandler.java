package tfc.fearofthedark.client;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.*;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Matrix4f;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;
import tfc.fearofthedark.common.IHaveFear;
import tfc.fearofthedark.common.PlayerMixinHandler;
import tfc.stylesplusplus.api.Color;

import java.util.function.Function;

public class MixinHandler {
	public static void preDrawHeart(MatrixStack matrices, InGameHud.HeartType type, int x, int y, int v, boolean blinking, boolean halfHeart, CallbackInfo ci) {
		ClientPlayerEntity playerEntity = MinecraftClient.getInstance().player;
		
		if (((IHaveFear) playerEntity).FearOfTheDark_getPhase() <= 1) return;
		
		IHaveFear fearHolder = (IHaveFear) playerEntity;
		
		int x0 = x;
		int y0 = y;
		int x1 = x0 + 9;
		int y1 = y0 + 9;
		
		int z = 0;
		
		float u0 = type.getU(halfHeart, blinking) / 256f;
		float v0 = v / 256f;
		float u1 = u0 + (9 / 256f);
		float v1 = v0 + (9 / 256f);
		
		Matrix4f matrix = matrices.peek().getModel();
		
		int ticksParanoia = (int) (PlayerMixinHandler.getTimeFactor(2) * PlayerMixinHandler.getScaleFactor(((IHaveFear) playerEntity).FearOfTheDark_getFactor()));
		int ticksStress = (int) (PlayerMixinHandler.getTimeFactor(4) * PlayerMixinHandler.getScaleFactor(((IHaveFear) playerEntity).FearOfTheDark_getFactor()));
		ticksParanoia /= 100;
		ticksStress /= 100;
		
		int ticks = fearHolder.FearOfTheDark_getTicks();
		
		int ticksDamage = ticksStress + 40;
		float progressAlpha = ((ticks / 100f) - ticksStress) / (float) (ticksDamage - ticksStress);
//		if (progressAlpha > 1) progressAlpha = 1;
		if (progressAlpha > 1) {
			ci.cancel();
			return;
		}
		if (progressAlpha < 0) progressAlpha = 0;
		progressAlpha = 1 - progressAlpha;
		
		float progress = ((ticks / 100f) - ticksParanoia) / (float) (ticksStress - ticksParanoia);
		if (progress > 1) progress = 1;
		if (progress < 0) progress = 0;
		
		RenderSystem.setShader(GameRenderer::getPositionColorTexShader);
		BufferBuilder bufferBuilder = Tessellator.getInstance().getBuffer();
		bufferBuilder.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR_TEXTURE);
		bufferBuilder.vertex(matrix, (float) x0, (float) y1, (float) z).color(MathHelper.lerp(progress, 1f, 0f), MathHelper.lerp(progress, 1f, 0f), MathHelper.lerp(progress, 1f, 0.25f), progressAlpha).texture(u0, v1).next();
		bufferBuilder.vertex(matrix, (float) x1, (float) y1, (float) z).color(MathHelper.lerp(progress, 1f, 0f), MathHelper.lerp(progress, 1f, 0f), MathHelper.lerp(progress, 1f, 0f), progressAlpha).texture(u1, v1).next();
		bufferBuilder.vertex(matrix, (float) x1, (float) y0, (float) z).color(MathHelper.lerp(progress, 1f, 0f), MathHelper.lerp(progress, 1f, 0f), MathHelper.lerp(progress, 1f, 0.25f), progressAlpha).texture(u1, v0).next();
		bufferBuilder.vertex(matrix, (float) x0, (float) y0, (float) z).color(MathHelper.lerp(progress, 1f, 0f), MathHelper.lerp(progress, 1f, 0f), 1f, progressAlpha).texture(u0, v0).next();
		bufferBuilder.end();
		BufferRenderer.draw(bufferBuilder);
		
		ci.cancel();
	}
	
	public static void drawHunger(Matrix4f matrix, int x0, int x1, int y0, int y1, int z, float u0, float u1, float v0, float v1) {
		PlayerEntity playerEntity = MinecraftClient.getInstance().player;
		int ticksFear = (int) (PlayerMixinHandler.getTimeFactor(3) * PlayerMixinHandler.getScaleFactor(((IHaveFear) playerEntity).FearOfTheDark_getFactor()));
		int ticksStress = (int) (PlayerMixinHandler.getTimeFactor(4) * PlayerMixinHandler.getScaleFactor(((IHaveFear) playerEntity).FearOfTheDark_getFactor()));
		ticksFear /= 100;
		ticksStress /= 100;
		
		IHaveFear fearHolder = (IHaveFear) playerEntity;
		
		int ticks = fearHolder.FearOfTheDark_getTicks();
		float progress = ((ticks / 100f) - ticksFear) / (float) (ticksStress - ticksFear);
//		if (progress > 1) progress = 1;
		if (progress > 1) return;
		if (progress < 0) progress = 0;
		progress = 1 - progress;
		
		RenderSystem.setShader(GameRenderer::getPositionColorTexShader);
		BufferBuilder bufferBuilder = Tessellator.getInstance().getBuffer();
		bufferBuilder.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR_TEXTURE);
		bufferBuilder.vertex(matrix, (float) x0, (float) y1, (float) z).color(progress, progress, progress, progress).texture(u0, v1).next();
		bufferBuilder.vertex(matrix, (float) x1, (float) y1, (float) z).color(progress, progress, progress, progress).texture(u1, v1).next();
		bufferBuilder.vertex(matrix, (float) x1, (float) y0, (float) z).color(progress, progress, progress, progress).texture(u1, v0).next();
		bufferBuilder.vertex(matrix, (float) x0, (float) y0, (float) z).color(progress, progress, progress, progress).texture(u0, v0).next();
		bufferBuilder.end();
		BufferRenderer.draw(bufferBuilder);
	}
	
	public static void modifyLight(Args args, Function<Integer, Float> brightnessGetter) {
		int v = args.get(2);
		Color lightColor = new Color(v);
//		float mul = 1 - ((Integer) args.get(0) / 15f);
		float mul;
		
		ClientWorld clientWorld = MinecraftClient.getInstance().world;
		
		float sky = brightnessGetter.apply(args.get(0));
		float block = brightnessGetter.apply(args.get(0));
		float f = clientWorld.method_23783(1.0F);
		if (((Integer) args.get(0)).intValue() == 15) return;
//		if (((Integer) args.get(0)).intValue() == 0) return;
		if (clientWorld.getLightningTicksLeft() > 0) f = 1f;
		else f = f * 0.95F + 0.05F;
		sky = 1 - sky;
		sky *= f;
		
//		mul = 1 - f;
		mul = sky;
		mul -= 0.23;
		mul = Math.max(mul, 0);
		mul += 0.23;
		
		float gamma = (float) MinecraftClient.getInstance().options.gamma / 3f;
//		{
//			float f1 = 1.0F - mul;
//			mul *= 1.0F - f1 * f1 * f1 * f1;
//		}
		
		mul = ((1 - ((mul + gamma) / 2f)) + (0.5f)) / 2f;
//		mul += (gamma / 1);
//		mul /= 2;
//		mul = 1 - mul;
////		mul += 0.25f * f;
//		mul += 0.5;
//		mul /= 2;
		
		Color newColor = new Color(
				Math.min((int) (lightColor.getRed() * (1.1 / (mul * 2.4))), lightColor.getRed()),
				Math.min((int) (lightColor.getGreen() * (1.2 / (mul * 2.5))), lightColor.getGreen()),
				Math.min((int) (lightColor.getBlue() * (1.5 / (mul * 2.6))), lightColor.getBlue()),
				lightColor.getAlpha()
		);
		args.set(2, newColor.getRGB());
	}
}
