package tfc.fearofthedark.client;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.*;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Style;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Matrix4f;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import tfc.fearofthedark.common.IHaveFear;
import tfc.fearofthedark.common.PlayerMixinHandler;
import tfc.stylesplusplus.api.ExtraStyleData;

public class MixinHandler {
	public static void preDrawHeart(MatrixStack matrices, InGameHud.HeartType type, int x, int y, int v, boolean blinking, boolean halfHeart, CallbackInfo ci) {
		ClientPlayerEntity playerEntity = MinecraftClient.getInstance().player;
		
		if (((IHaveFear)playerEntity).FearOfTheDark_getPhase() <= 1) return;
		
		IHaveFear fearHolder = (IHaveFear)playerEntity;
		
		RenderSystem.setShader(GameRenderer::getPositionColorTexShader);
		BufferBuilder bufferBuilder = Tessellator.getInstance().getBuffer();
		bufferBuilder.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR_TEXTURE);
		
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
		
		int ticksParanoia = (int) (PlayerMixinHandler.getTimeFactor(2) * PlayerMixinHandler.getScaleFactor(((IHaveFear)playerEntity).FearOfTheDark_getFactor()));
		int ticksStress = (int) (PlayerMixinHandler.getTimeFactor(4) * PlayerMixinHandler.getScaleFactor(((IHaveFear)playerEntity).FearOfTheDark_getFactor()));
		ticksParanoia /= 100;
		ticksStress /= 100;
		
		int ticks = fearHolder.FearOfTheDark_getTicks();
		
		float progress = ((ticks / 100f) - ticksParanoia) / (float) (ticksStress - ticksParanoia);
		if (progress > 1) progress = 1;
		if (progress < 0) progress = 0;
		
 		int ticksDamage = ticksStress + 40;
		float progressAlpha = ((ticks / 100f) - ticksStress) / (float) (ticksDamage - ticksStress);
		if (progressAlpha > 1) progressAlpha = 1;
		if (progressAlpha < 0) progressAlpha = 0;
		progressAlpha = 1-progressAlpha;
		
		bufferBuilder.vertex(matrix, (float) x0, (float) y1, (float) z).color(MathHelper.lerp(progress, 1f, 0f), MathHelper.lerp(progress, 1f, 0f), MathHelper.lerp(progress, 1f, 0.25f), progressAlpha).texture(u0, v1).next();
		bufferBuilder.vertex(matrix, (float) x1, (float) y1, (float) z).color(MathHelper.lerp(progress, 1f, 0f), MathHelper.lerp(progress, 1f, 0f), MathHelper.lerp(progress, 1f, 0f), progressAlpha).texture(u1, v1).next();
		bufferBuilder.vertex(matrix, (float) x1, (float) y0, (float) z).color(MathHelper.lerp(progress, 1f, 0f), MathHelper.lerp(progress, 1f, 0f), MathHelper.lerp(progress, 1f, 0.25f), progressAlpha).texture(u1, v0).next();
		bufferBuilder.vertex(matrix, (float) x0, (float) y0, (float) z).color(MathHelper.lerp(progress, 1f, 0f), MathHelper.lerp(progress, 1f, 0f), 1f, progressAlpha).texture(u0, v0).next();
		bufferBuilder.end();
		BufferRenderer.draw(bufferBuilder);
		
		ci.cancel();
	}
}
