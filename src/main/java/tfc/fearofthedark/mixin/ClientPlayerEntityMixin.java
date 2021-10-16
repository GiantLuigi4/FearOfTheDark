package tfc.fearofthedark.mixin;

import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import tfc.fearofthedark.common.ClientPlayerInfo;
import tfc.fearofthedark.common.IHaveFear;
import tfc.fearofthedark.common.PlayerMixinHandler;

@Mixin(ClientPlayerEntity.class)
public class ClientPlayerEntityMixin implements IHaveFear, ClientPlayerInfo {
	@Unique
	float factor;
	@Unique
	int ticks;
	@Unique
	int phase;
	
	@Unique
	int ticksOutOfSun = 0;
	
	@Override
	public float FearOfTheDark_getFactor() {
		return factor;
	}
	
	@Override
	public void FearOfTheDark_setFactor(float amt) {
		factor = amt;
	}
	
	@Override
	public int FearOfTheDark_getTicks() {
		return ticks;
	}
	
	@Override
	public void FearOfTheDark_setTicks(int amt) {
		ticks = amt;
	}
	
	@Override
	public int FearOfTheDark_getPhase() {
		return phase;
	}
	
	@Override
	public void FearOfTheDark_setPhase(int amt) {
		phase = amt;
	}
	
	@Inject(at = @At("HEAD"), method = "tick")
	public void preTick(CallbackInfo ci) {
		PlayerMixinHandler.onTick((PlayerEntity) (Object) this);
	}
	
	@Unique boolean isPocketAware = true;
	
	public boolean FearOfTheDark_isPocketAware() {
		return isPocketAware;
	}
	
	public void FearOfTheDark_setPocketAware(boolean value) {
		isPocketAware = value;
	}
	
	@Override
	public int getTicksOutOfSun() {
		return ticksOutOfSun;
	}
	
	@Unique float lastBlur = 0;
	
	@Override
	public float getLastBlur() {
		return lastBlur;
	}
	
	@Override
	public void setLastBlur(float lb) {
		lastBlur = lb;
	}
	
	@Override
	public void setTicksOutOfSun(int amt) {
		ticksOutOfSun = amt;
	}
}
