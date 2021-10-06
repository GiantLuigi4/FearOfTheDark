package tfc.fearofthedark.mixin;

import com.google.common.collect.Multimap;
import com.google.common.collect.MultimapBuilder;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.attribute.*;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import tfc.fearofthedark.common.IHaveFear;
import tfc.fearofthedark.common.PlayerMixinHandler;

import java.util.UUID;

@Mixin(ClientPlayerEntity.class)
public class ClientPlayerEntityMixin implements IHaveFear {
	@Unique
	float factor;
	@Unique
	int ticks;
	@Unique
	int phase;
	
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
		PlayerMixinHandler.addAttributes((PlayerEntity) (Object) this);
	}
}
