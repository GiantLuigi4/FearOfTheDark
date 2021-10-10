package tfc.fearofthedark;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents;
import net.minecraft.util.Identifier;
import tfc.fearofthedark.common.DualFactored;
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
		});
		
		ServerPlayerEvents.AFTER_RESPAWN.register(
				(oldPlayer, newPlayer, alive) -> {
					((DualFactored)newPlayer).setFactorA(new Random().nextFloat() + 0.5f);
					((DualFactored)newPlayer).setFactorB(((DualFactored)oldPlayer).getFactorB() - new Random().nextInt(100) / 10000f + 0.001f);
				}
		);
	}
}
