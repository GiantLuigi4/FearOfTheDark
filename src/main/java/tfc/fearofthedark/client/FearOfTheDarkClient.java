package tfc.fearofthedark.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.util.Identifier;
import tfc.fearofthedark.networking.FearOfTheDarkPacket;

@Environment(net.fabricmc.api.EnvType.CLIENT)
public class FearOfTheDarkClient implements ClientModInitializer {
	public static final boolean shaderUtilIntegration;
	
	static {
		shaderUtilIntegration = FabricLoader.getInstance().isModLoaded("shaderutil");
	}
	
	@Override
	public void onInitializeClient() {
		ClientPlayNetworking.registerGlobalReceiver(new Identifier("fearofthedark:networking"), (client, handler, buf, responseSender) -> {
			FearOfTheDarkPacket.handle(buf, handler, responseSender);
		});
	}
}
