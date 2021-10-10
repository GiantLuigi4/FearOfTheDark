package tfc.fearofthedark.styles;

import net.minecraft.text.Style;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Matrix4f;
import tfc.stylesplusplus.api.Color;
import tfc.stylesplusplus.api.ExtraStyle;
import tfc.stylesplusplus.api.util.GeneralUtils;

import java.util.Random;

public class FearStyle extends ExtraStyle {
	private int tickStart = GeneralUtils.getTime();
	
	public FearStyle() {
		super(new Identifier("fearofthedark:fear"));
	}
	
	Random rng;
	
	public float getFactor(int indx, int cnt) {
		return indx / (float) cnt;
	}
	
	@Override
	public void apply(int index, int count, char c, Matrix4f matrix, String str, Style fullStyle) {
		if (index == 0) {
			int tick = GeneralUtils.getTime() - tickStart;
			rng = new Random(tick);
		}
		
		matrix.multiply(Matrix4f.translate(
				(rng.nextFloat() - 0.5f) * getFactor(index, count),
				0, 0
		));
	}
	
	@Override
	public Color modifyColor(Color color, int index, int count, char c, String str, Style fullStyle, float brightnessMultiplier) {
		return new Color(
				(int) (color.getRed() * (1 - (getFactor(index, count)) / 2)),
				(int) (color.getGreen() * (1 - (getFactor(index, count)) / 2)),
				(int) (color.getBlue() * (1 - (getFactor(index, count)) / 2)),
				(int) (color.getAlpha() * (1 - (getFactor(index, count)) / 2))
		);
	}
}
