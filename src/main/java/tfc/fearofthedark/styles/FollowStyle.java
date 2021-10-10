package tfc.fearofthedark.styles;

import net.minecraft.text.Style;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Matrix4f;
import tfc.fearofthedark.client.Utils;
import tfc.stylesplusplus.api.Color;
import tfc.stylesplusplus.api.ExtraStyle;
import tfc.stylesplusplus.api.util.GeneralUtils;

import java.util.Random;

public class FollowStyle extends ExtraStyle {
	private int tickStart = GeneralUtils.getTime();
	
	public FollowStyle() {
		super(new Identifier("fearofthedark:follow"));
	}
	
	@Override
	public void apply(int index, int count, char c, Matrix4f matrix, String str, Style fullStyle) {
	}
	
	float stopPoint = new Random().nextFloat();
	float lastStopPoint = new Random().nextFloat();
	int lastPoint = 0;
	
	@Override
	public Color modifyColor(Color color, int index, int count, char c, String str, Style fullStyle, float brightnessMultiplier) {
		int tick = GeneralUtils.getTime() - tickStart;
		int oneThird = (int) (count * stopPoint);
		
		int tickFollow = ((tick - 40) % (count + 60));
		{
			int pause = Math.max(Math.min(tickFollow, oneThird + 20), oneThird);
			tickFollow -= pause;
			tickFollow += oneThird;
		}
		
		tick %= count + 60;
		{
			int pause = Math.max(Math.min(tick, oneThird + 20), oneThird);
			tick -= pause;
			tick += oneThird;
		}
		if (lastPoint > tick) {
			lastStopPoint = stopPoint;
			stopPoint = new Random().nextFloat() / 2 + 0.25f;
		}
		lastPoint = tick;
		
		if (Math.ceil(Math.abs(index - tick)) <= 1) {
			int point = Math.round(index - tick);
			int[] pixel = Utils.getSkinColor(point);
			
			return new Color(
					(int) ((pixel[0])),
					(int) ((pixel[1])),
					(int) ((pixel[2])),
					color.getAlpha()
			);
		}
		
		if (Math.ceil(Math.abs(index - tickFollow)) <= 1) {
			return new Color(
					0, 0, 0,
					color.getAlpha()
			);
		}
		
		return new Color(
				(int) ((color.getRed())),
				(int) ((color.getGreen())),
				(int) ((color.getBlue())),
				(color.getAlpha())
		);
	}
}
