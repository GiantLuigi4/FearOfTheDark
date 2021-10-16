package tfc.fearofthedark.styles;

import net.minecraft.text.Style;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Matrix4f;
import tfc.fearofthedark.client.Utils;
import tfc.stylesplusplus.api.Color;
import tfc.stylesplusplus.api.ExtraStyle;
import tfc.stylesplusplus.api.util.GeneralUtils;

import java.util.Random;

public class WantStyle extends ExtraStyle {
	private int tickStart = GeneralUtils.getTime();
	
	public WantStyle() {
		super(new Identifier("fearofthedark:want"));
	}
	
	float stopPoint = new Random().nextFloat();
	int lastPoint = 0;
	
	@Override
	public void apply(int index, int count, char c, Matrix4f matrix, String str, Style fullStyle) {
	}
	
	public Color lightColor = new Color(255, 255, 100);
	
	@Override
	public Color modifyColor(Color color, int index, int count, char c, String str, Style fullStyle, float brightnessMultiplier) {
		int tick = GeneralUtils.getTime() - tickStart;
		int oneThird = (int) (count * stopPoint);
		
		tick %= count + 60;
		{
			int pause = Math.max(Math.min(tick, oneThird + 20), oneThird);
			tick -= pause;
			tick += oneThird;
		}
		
		if (lastPoint > tick) stopPoint = new Random().nextFloat() / 2 + 0.25f;
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
		
		double v1 = index - (tick + 2);
		if (v1 == 0) {
			return new Color(
					(int) (99),
					(int) (48),
					(int) (0),
					color.getAlpha()
			);
		}
		v1 = Math.min(Math.max(v1, -8), 8);
		v1 /= 8;
		if (v1 < 0) v1 = 1;
		v1 = 1 - v1;
		
		return new Color(
				(int) (MathHelper.lerp(v1, color.getRed(), lightColor.getRed())),
				(int) (MathHelper.lerp(v1, color.getGreen(), lightColor.getGreen())),
				(int) (MathHelper.lerp(v1, color.getBlue(), lightColor.getBlue())),
				color.getAlpha()
		);
	}
}
