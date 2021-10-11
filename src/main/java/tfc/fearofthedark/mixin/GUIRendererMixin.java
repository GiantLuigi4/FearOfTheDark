package tfc.fearofthedark.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import tfc.fearofthedark.common.ClientPlayerInfo;

@Mixin(ItemRenderer.class)
public class GUIRendererMixin {
	@Inject(at = @At("HEAD"), method = "innerRenderInGui(Lnet/minecraft/entity/LivingEntity;Lnet/minecraft/item/ItemStack;IIII)V", cancellable = true)
	public void preRenderIntoGUI0(LivingEntity entity, ItemStack itemStack, int x, int y, int seed, int depth, CallbackInfo ci) {
		if (entity instanceof ClientPlayerInfo) {
			if (!((ClientPlayerInfo) entity).FearOfTheDark_isPocketAware()) {
				ci.cancel();
			}
		}
	}
	
	@Inject(at = @At("HEAD"), method = "renderGuiItemOverlay(Lnet/minecraft/client/font/TextRenderer;Lnet/minecraft/item/ItemStack;IILjava/lang/String;)V", cancellable = true)
	public void preRenderIntoGUI1(TextRenderer renderer, ItemStack stack, int x, int y, String countLabel, CallbackInfo ci) {
		LivingEntity entity = MinecraftClient.getInstance().player;
		if (entity instanceof ClientPlayerInfo) {
			if (!((ClientPlayerInfo) entity).FearOfTheDark_isPocketAware()) {
				ci.cancel();
			}
		}
	}
}
