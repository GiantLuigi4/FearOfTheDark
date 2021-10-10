package tfc.fearofthedark.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.LightmapTextureManager;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;
import tfc.fearofthedark.client.MixinHandler;
import tfc.stylesplusplus.api.Color;

@Mixin(LightmapTextureManager.class)
public abstract class LightmapTextureManagerMixin {
	@Shadow protected abstract float getBrightness(World world, int lightLevel);
	
	@Shadow @Final private MinecraftClient client;
	
	@ModifyArgs(method = "update", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/texture/NativeImage;setColor(III)V"))
	public void setColorChannels(Args args) {
		MixinHandler.modifyLight(args, (x) -> getBrightness(client.world, x));
	}
}
