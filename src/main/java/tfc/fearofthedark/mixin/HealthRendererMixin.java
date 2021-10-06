package tfc.fearofthedark.mixin;

import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.util.math.MatrixStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import tfc.fearofthedark.client.MixinHandler;

@Mixin(InGameHud.class)
public class HealthRendererMixin {
	@Inject(at = @At("HEAD"), method = "drawHeart",cancellable = true)
	public void preDrawHeart(MatrixStack matrices, InGameHud.HeartType type, int x, int y, int v, boolean blinking, boolean halfHeart, CallbackInfo ci) {
		MixinHandler.preDrawHeart(matrices, type, x, y, v, blinking, halfHeart, ci);
	}
}
