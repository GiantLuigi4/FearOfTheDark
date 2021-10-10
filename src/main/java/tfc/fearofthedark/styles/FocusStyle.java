package tfc.fearofthedark.styles;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.texture.AbstractTexture;
import net.minecraft.client.texture.NativeImage;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.text.Style;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Matrix4f;
import org.lwjgl.opengl.GL11;
import tfc.fearofthedark.client.Utils;
import tfc.stylesplusplus.api.Color;
import tfc.stylesplusplus.api.ExtraStyle;
import tfc.stylesplusplus.api.util.GeneralUtils;

import java.awt.image.BufferedImage;

public class FocusStyle extends ExtraStyle {
	public FocusStyle() {
		super(new Identifier("fearofthedark:focus"));
	}
	
	private int tickStart = GeneralUtils.getTime();
	
	@Override
	public void apply(int index, int count, char c, Matrix4f matrix, String str, Style fullStyle) {
	}
	
	@Override
	public Color modifyColor(Color color, int index, int count, char c, String str, Style fullStyle, float brightnessMultiplier) {
		int middle = count / 2;
		index += 1;
		
		int offset = index - middle;
		offset = Math.abs(offset);
		
		double dist = offset / (double) (middle);
		dist *= 4;
		
		int tick = GeneralUtils.getTime() - tickStart;
		double c1 = Math.cos(tick / 10f);
		if (dist < c1) dist = c1;
		dist = c1 - dist;
		dist = Math.abs(dist);
		dist = Math.max(Math.min(dist, 1), 0);
		
		if (Math.abs(index - middle) <= 1) {
			int point = index - middle;
			int[] pixel = Utils.getSkinColor(point);
			
			return new Color(
//					(int) (0 * dist),
//					(int) (0 * dist),
					(int) ((pixel[0]) * dist),
					(int) ((pixel[1]) * dist),
					(int) ((pixel[2]) * dist),
					(int) (color.getAlpha() * dist)
			);
		}
		
		return new Color(
				(int) (color.getRed() * dist),
				(int) (color.getGreen() * dist),
				(int) (color.getBlue() * dist),
				(int) (color.getAlpha() * dist)
		);
	}
}
