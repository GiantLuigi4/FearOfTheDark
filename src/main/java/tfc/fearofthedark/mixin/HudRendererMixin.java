package tfc.fearofthedark.mixin;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.render.*;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.Matrix4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import tfc.fearofthedark.client.MixinHandler;

@Mixin(InGameHud.class)
public class HudRendererMixin {
	@Inject(at = @At("HEAD"), method = "drawHeart",cancellable = true)
	public void preDrawHeart(MatrixStack matrices, InGameHud.HeartType type, int x, int y, int v, boolean blinking, boolean halfHeart, CallbackInfo ci) {
		MixinHandler.preDrawHeart(matrices, type, x, y, v, blinking, halfHeart, ci);
	}
	
//	@Redirect(at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/hud/InGameHud;drawTexture(Lnet/minecraft/client/util/math/MatrixStack;IIIIII)V", ordinal = 3), method = "renderStatusBars", allow = -1, require = 0, expect = 1)
//	public void preDrawHunger0(InGameHud inGameHud, MatrixStack matrices, int x, int y, int u, int v, int width, int height) {
//		drawTexture(matrices, x, x + width, y, y + height, ((InGameHud)(Object)this).getZOffset(), width, height, u, v);
//	}
	
	@Redirect(at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/hud/InGameHud;drawTexture(Lnet/minecraft/client/util/math/MatrixStack;IIIIII)V", ordinal = 4), method = "renderStatusBars", allow = -1, require = 0, expect = 1)
	public void preDrawHunger1(InGameHud inGameHud, MatrixStack matrices, int x, int y, int u, int v, int width, int height) {
		drawTexture(matrices, x, x + width, y, y + height, ((InGameHud)(Object)this).getZOffset(), width, height, u, v);
	}
	
	@Redirect(at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/hud/InGameHud;drawTexture(Lnet/minecraft/client/util/math/MatrixStack;IIIIII)V", ordinal = 5), method = "renderStatusBars", allow = -1, require = 0, expect = 1)
	public void preDrawHunger2(InGameHud inGameHud, MatrixStack matrices, int x, int y, int u, int v, int width, int height) {
		drawTexture(matrices, x, x + width, y, y + height, ((InGameHud)(Object)this).getZOffset(), width, height, u, v);
	}
	
	private static void drawTexture(MatrixStack matrices, int x0, int x1, int y0, int y1, int z, int regionWidth, int regionHeight, float u, float v) {
		drawHunger(matrices.peek().getModel(), x0, x1, y0, y1, z, (u + 0.0F) / (float) 256, (u + (float)regionWidth) / (float) 256, (v + 0.0F) / (float) 256, (v + (float)regionHeight) / (float) 256);
	}
	
	private static void drawHunger(Matrix4f matrix, int x0, int x1, int y0, int y1, int z, float u0, float u1, float v0, float v1) {
		MixinHandler.drawHunger(matrix, x0, x1, y0, y1, z, u0, u1, v0, v1);
	}
}
