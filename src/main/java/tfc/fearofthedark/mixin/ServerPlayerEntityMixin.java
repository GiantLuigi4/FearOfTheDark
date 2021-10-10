package tfc.fearofthedark.mixin;

import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.client.render.LightmapTextureManager;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.world.LightType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import tfc.fearofthedark.common.DualFactored;
import tfc.fearofthedark.common.PlayerMixinHandler;
import tfc.fearofthedark.common.ServerPlayerEntityMixinHandler;
import tfc.fearofthedark.networking.FearOfTheDarkPacket;
import tfc.stylesplusplus.mixin.SerializerMixin;

import java.util.Random;

@Mixin(ServerPlayerEntity.class)
public abstract class ServerPlayerEntityMixin implements DualFactored {
	@Shadow
	public abstract ServerWorld getServerWorld();
	
	@Shadow
	public abstract void sendMessage(Text message, boolean actionBar);
	
	@Shadow
	public ServerPlayNetworkHandler networkHandler;
	@Shadow
	private float syncedHealth;
	@Unique
	protected int ticksInDark;
	
	@Override
	public void setFactorA(float val) {
		fearFactorA = val;
	}
	
	@Override
	public float getFactorA() {
		return fearFactorA;
	}
	
	@Override
	public void setFactorB(float val) {
		fearFactorB = val;
	}
	
	@Override
	public float getFactorB() {
		return fearFactorB;
	}
	
	@Override
	public float FearOfTheDark_getFactor() {
		return fearFactorA * fearFactorB;
	}
	
	@Override
	public void FearOfTheDark_setFactor(float amt) {
	}
	
	@Override
	public int FearOfTheDark_getTicks() {
		return ticksInDark;
	}
	
	@Override
	public void FearOfTheDark_setTicks(int amt) {
		ticksInDark = amt;
	}
	
	@Unique
	protected int phase = 0;
	
	// randomized upon respawn
	@Unique
	protected float fearFactorA = new Random().nextFloat() + 0.5f;
	
	// kept when you die and respawn, increments upon respawn
	@Unique
	protected float fearFactorB = 1f;
	
	@Unique
	protected byte tickNumber = 0;
//	protected byte tickNumber = -128;
	
	protected float lastHealth;
	
	@Inject(at = @At("HEAD"), method = "tick")
	public void preTick(CallbackInfo ci) {
		if (fearFactorB >= 2f) fearFactorB = 2f;
		float knownFactor = fearFactorA * fearFactorB;
//		if  (tickNumber == 125) {
		if (tickNumber == 30) {
			ServerPlayNetworking.send(
					(ServerPlayerEntity) (Object) this,
					new Identifier("fearofthedark:networking"),
					FearOfTheDarkPacket.writeSpawnInfo(PacketByteBufs.create(), phase, ticksInDark, fearFactorA * fearFactorB)
			);
			tickNumber++;
//		} else if (tickNumber < 125) {
		} else if (tickNumber < 30) {
			tickNumber++;
		}
		
		int sky = getServerWorld().getLightLevel(LightType.SKY, ((Entity) (Object) this).getBlockPos().up());
		int block = getServerWorld().getLightLevel(LightType.BLOCK, ((Entity) (Object) this).getBlockPos().up());
		if (getServerWorld().isNight()) sky = 0;
		
		if (syncedHealth < lastHealth) {
			if (block <= 3 && sky == 0) {
				ticksInDark += 20;
			}
		}
		lastHealth = syncedHealth;
		
		if (sky < 7 && block < 7) ticksInDark += 7 - Math.max(sky, block);
		else ticksInDark -= ServerPlayerEntityMixinHandler.getRecoveryFactor(sky, block);
		
		if (ticksInDark < 0) ticksInDark = 0;

//		int scale = 60;
		float scale = PlayerMixinHandler.getScaleFactor(fearFactorA * fearFactorB);
		// multiplying by scale because the preceding number is my testing number
		if (checkPhase(4, (int) (PlayerMixinHandler.getTimeFactor(4) * scale), "stress", 2)) ;
		else if (checkPhase(3, (int) (PlayerMixinHandler.getTimeFactor(3) * scale), "fear", 5)) ;
		else if (checkPhase(2, (int) (PlayerMixinHandler.getTimeFactor(2) * scale), "paranoia", 2)) ;
		else if (checkPhase(1, (int) (PlayerMixinHandler.getTimeFactor(1) * scale), "nervousness", 2)) ;
		else phase = 0;
		
		if (((Entity) (Object) this).age % 25 == 3) {
			ServerPlayNetworking.send(
					(ServerPlayerEntity) (Object) this,
					new Identifier("fearofthedark:networking"),
					FearOfTheDarkPacket.writePhase(PacketByteBufs.create(), phase, ticksInDark)
			);
		}
		
		ServerPlayerEntityMixinHandler.doEffects(phase, ticksInDark, ((Entity) (Object) this).age, sky, block, ((ServerPlayerEntity) (Object) this));
		
		if (knownFactor != fearFactorA * fearFactorB) {
			ServerPlayNetworking.send(
					(ServerPlayerEntity) (Object) this,
					new Identifier("fearofthedark:networking"),
					FearOfTheDarkPacket.writeFearFactor(PacketByteBufs.create(), fearFactorA * fearFactorB)
			);
		}
	}
	
	@Inject(at = @At("HEAD"), method = "writeCustomDataToNbt")
	public void preWrite(NbtCompound nbt, CallbackInfo ci) {
		NbtCompound fotd = new NbtCompound();
		
		fotd.putInt("ticksInDark", ticksInDark);
		fotd.putFloat("fearFactorA", fearFactorA);
		fotd.putFloat("fearFactorB", fearFactorB);
		
		nbt.put("fearofthedark", fotd);
	}
	
	@Inject(at = @At("HEAD"), method = "readCustomDataFromNbt")
	public void preRead(NbtCompound nbt, CallbackInfo ci) {
		if (nbt.contains("fearofthedark", NbtElement.COMPOUND_TYPE)) {
			NbtCompound fotd = new NbtCompound();
			if (fotd.contains("ticksInDark", NbtElement.INT_TYPE)) ticksInDark = fotd.getInt("ticksInDark");
			if (fotd.contains("fearFactorA", NbtElement.FLOAT_TYPE)) fearFactorA = fotd.getFloat("fearFactorA");
			if (fotd.contains("fearFactorB", NbtElement.FLOAT_TYPE)) fearFactorB = fotd.getFloat("fearFactorB");
		}
	}
	
	@Unique
	public boolean checkPhase(int phaseNumber, int ticks, String name, int messageCount) {
		return ServerPlayerEntityMixinHandler.checkPhase(((ServerPlayerEntity) (Object) this), phase, ticksInDark, phaseNumber, ticks, name, messageCount);
	}
	
	@Override
	public void FearOfTheDark_setPhase(int phase) {
		this.phase = phase;
	}
	
	@Override
	public int FearOfTheDark_getPhase() {
		return phase;
	}
}
