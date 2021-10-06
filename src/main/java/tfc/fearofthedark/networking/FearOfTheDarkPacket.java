package tfc.fearofthedark.networking;

import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.network.PacketByteBuf;
import tfc.fearofthedark.common.IHaveFear;

public class FearOfTheDarkPacket {
	public static PacketByteBuf writePhase(PacketByteBuf buf, int phase, int time) {
		buf.writeInt(0);
		buf.writeInt(phase);
		buf.writeInt(time);
		return buf;
	}
	
	public static PacketByteBuf writeSpawnInfo(PacketByteBuf buf, int phase, int time, float fearFactor) {
		buf.writeInt(1);
		buf.writeInt(phase);
		buf.writeInt(time);
		buf.writeFloat(fearFactor);
		return buf;
	}
	
	public static PacketByteBuf writeFearFactor(PacketByteBuf buf, float fearFactor) {		buf.writeInt(1);
		buf.writeInt(2);
		buf.writeFloat(fearFactor);
		return buf;
	}
	
	public static void handle(PacketByteBuf buf, ClientPlayNetworkHandler handler, PacketSender responseSender) {
		int type = buf.readInt();
		ClientPlayerEntity entity = MinecraftClient.getInstance().player;
		IHaveFear fearHolder = (IHaveFear) entity;
		if (fearHolder == null) return;
		switch (type) {
			case 0:
				int phase = buf.readInt();
				int time = buf.readInt();
				fearHolder.FearOfTheDark_setTicks(time);
				fearHolder.FearOfTheDark_setPhase(phase);
				break;
			case 1:
				phase = buf.readInt();
				time = buf.readInt();
				fearHolder.FearOfTheDark_setTicks(time);
				fearHolder.FearOfTheDark_setPhase(phase);
				float fearFactor = buf.readFloat();
				fearHolder.FearOfTheDark_setFactor(fearFactor);
				break;
			case 2:
				fearFactor = buf.readFloat();
				fearHolder.FearOfTheDark_setFactor(fearFactor);
				break;
			default:
				System.out.println("[FearOfTheDark] Unknown packet type " + type);
		}
	}
}
