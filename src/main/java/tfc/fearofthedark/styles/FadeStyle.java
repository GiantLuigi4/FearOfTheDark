package tfc.fearofthedark.styles;

import net.minecraft.text.Style;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Matrix4f;
import tfc.stylesplusplus.api.Color;
import tfc.stylesplusplus.api.ExtraStyle;
import tfc.stylesplusplus.api.util.GeneralUtils;

public class FadeStyle extends ExtraStyle {
	public FadeStyle() {
		super(new Identifier("fearofthedark:fade"));
	}
	
	@Override
	public void apply(int index, int count, char c, Matrix4f matrix, String str, Style fullStyle) {
	}
	
	@Override
	public Color modifyColor(Color color, int index, int count, char c, String str, Style fullStyle, float brightnessMultiplier) {
		double tick = GeneralUtils.getTime() / 5d;
		
		return new Color(
				color.getRed(), color.getGreen(), color.getBlue(),
				(int) (color.getAlpha() * Math.min(Math.max(Math.cos(tick), 0), 1))
		);
	}
}
