package tfc.fearofthedark.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.util.Identifier;
import tfc.fearofthedark.networking.FearOfTheDarkPacket;

@Environment(net.fabricmc.api.EnvType.CLIENT)
public class FearOfTheDarkClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		ClientPlayNetworking.registerGlobalReceiver(new Identifier("fearofthedark:networking"), (client, handler, buf, responseSender) -> {
			FearOfTheDarkPacket.handle(buf, handler, responseSender);
		});
	}
}
