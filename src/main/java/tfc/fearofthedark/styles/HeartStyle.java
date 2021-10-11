package tfc.fearofthedark.styles;

import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Style;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Matrix4f;
import tfc.stylesplusplus.api.Color;
import tfc.stylesplusplus.api.ExtraStyle;
import tfc.stylesplusplus.api.util.GeneralUtils;

public class HeartStyle extends ExtraStyle {
	public HeartStyle() {
		super(new Identifier("fearofthedark:heart"));
	}
	
	@Override
	public void apply(int index, int count, char c, Matrix4f matrix, String str, Style fullStyle) {
	}
	
	@Override
	public Color modifyColor(Color color, int index, int count, char c, String str, Style fullStyle, float brightnessMultiplier) {
		int half = (count / 2);
		
		double tick = (GeneralUtils.getTime() * 1 + (MinecraftClient.getInstance().getTickDelta() * 1));
		
		if (Math.abs(index - half) < 5) {
			tick = (tick % 10);
			if (tick > 5) tick = ((-5) - (tick - 5)) + 10;
			double dist = index - half;
			dist = Math.abs(dist);
			dist /= tick;
			dist = 1 - dist;
			dist = Math.min(Math.max(dist, 0), 1);
			if (index == half) dist = 1;
			Color out = new Color(
					(int) (MathHelper.lerp(dist, color.getRed(), 255)),
					(int) (MathHelper.lerp(dist, color.getGreen(), 32)),
					(int) (MathHelper.lerp(dist, color.getBlue(), 32)),
					color.getAlpha()
			);
			return out;
		}
		
		return super.modifyColor(color, index, count, c, str, fullStyle, brightnessMultiplier);
	}
}
