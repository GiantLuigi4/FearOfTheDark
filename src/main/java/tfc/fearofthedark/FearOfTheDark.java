package tfc.fearofthedark;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents;
import net.minecraft.util.Identifier;
import tfc.fearofthedark.common.DualFactored;
import tfc.fearofthedark.common.IHaveFear;
import tfc.fearofthedark.styles.*;
import tfc.stylesplusplus.api.StyleRegistry;

import java.util.Random;

public class FearOfTheDark implements ModInitializer {
	@Override
	public void onInitialize() {
		StyleRegistry.addStyleLoader(()->{
			StyleRegistry.register(new Identifier("fearofthedark:swinging_light"), SwingingLightStyle::new);
			StyleRegistry.register(new Identifier("fearofthedark:torch_style"), TorchStyle::new);
			StyleRegistry.register(new Identifier("fearofthedark:shaky"), ShakyStyle::new);
			StyleRegistry.register(new Identifier("fearofthedark:focus"), FocusStyle::new);
			StyleRegistry.register(new Identifier("fearofthedark:follow"), FollowStyle::new);
			StyleRegistry.register(new Identifier("fearofthedark:fear"), FearStyle::new);
			StyleRegistry.register(new Identifier("fearofthedark:heart"), HeartStyle::new);
			StyleRegistry.register(new Identifier("fearofthedark:fade"), FadeStyle::new);
			StyleRegistry.register(new Identifier("fearofthedark:alone"), AloneStyle::new);
		});
		
		ServerPlayerEvents.AFTER_RESPAWN.register(
				(oldPlayer, newPlayer, alive) -> {
					if (newPlayer instanceof DualFactored) {
						if (!alive) ((DualFactored)newPlayer).setFactorA(new Random().nextFloat() + 0.5f);
						((DualFactored)newPlayer).setFactorB(((DualFactored)oldPlayer).getFactorB() - new Random().nextInt(100) / 10000f + 0.001f);
					}
					
					if (alive) {
						if (newPlayer instanceof DualFactored) {
							((DualFactored)newPlayer).FearOfTheDark_setTicks(((DualFactored)oldPlayer).FearOfTheDark_getTicks());
							((DualFactored)newPlayer).FearOfTheDark_setPhase(((DualFactored)oldPlayer).FearOfTheDark_getPhase());
							((DualFactored)newPlayer).setFactorA(((DualFactored)oldPlayer).getFactorA());
						} else {
							((IHaveFear) newPlayer).FearOfTheDark_setTicks(((IHaveFear) oldPlayer).FearOfTheDark_getTicks());
							((IHaveFear) newPlayer).FearOfTheDark_setFactor(((IHaveFear) oldPlayer).FearOfTheDark_getFactor());
							((IHaveFear) newPlayer).FearOfTheDark_setPhase(((IHaveFear) oldPlayer).FearOfTheDark_getPhase());
						}
					}
				}
		);
	}
}
