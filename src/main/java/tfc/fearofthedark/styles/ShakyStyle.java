package tfc.fearofthedark.styles;

import net.minecraft.text.Style;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Matrix4f;
import tfc.stylesplusplus.api.Color;
import tfc.stylesplusplus.api.ExtraStyle;
import tfc.stylesplusplus.api.util.GeneralUtils;

import java.util.Random;

public class ShakyStyle extends ExtraStyle {
	private int tickStart = GeneralUtils.getTime();
	
	public ShakyStyle() {
		super(new Identifier("fearofthedark:shaky"));
	}
	
	Random rng;
	
	@Override
	public Color modifyColor(Color color, int index, int count, char c, String str, Style fullStyle, float brightnessMultiplier) {
		return new Color(
				(int) (color.getRed() * brightnessMultiplier),
				(int) (color.getGreen() * brightnessMultiplier),
				(int) (color.getBlue() * brightnessMultiplier),
				color.getAlpha()
		);
	}
	
	@Override
	public void apply(int index, int count, char c, Matrix4f matrix, String str, Style fullStyle) {
		if (index == 0) {
			int tick = GeneralUtils.getTime() - tickStart;
			rng = new Random(tick);
		}
		
		matrix.multiply(Matrix4f.translate(
			rng.nextFloat() - 0.5f,
			rng.nextFloat() - 0.5f,
				0f
		));
	}
}
