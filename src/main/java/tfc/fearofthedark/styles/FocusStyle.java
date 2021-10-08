package tfc.fearofthedark.styles;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.texture.AbstractTexture;
import net.minecraft.client.texture.NativeImage;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.text.Style;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Matrix4f;
import org.lwjgl.opengl.GL11;
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
			AbstractTexture texture = MinecraftClient.getInstance().getTextureManager().getTexture(MinecraftClient.getInstance().player.getSkinTexture());
			
			int w = 64;
			int[] pixel = new int[w * w];
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture.getGlId());
			GL11.glGetTexImage(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA, GL11.GL_BYTE, pixel);
//			BufferedImage image = new BufferedImage(w, w, BufferedImage.TYPE_INT_ARGB);
//			for (int x = 0; x < w; x++) {
//				for (int y = 0; y < w; y++) {
//					Color col = new Color(pixel[x + (y * w)]);
//					image.setRGB(x, y, new Color(col.getBlue(), col.getGreen(), col.getRed(), col.getAlpha()).getRGB());
//				}
//			}
			
			pixel[3] = ((pixel[(24 + (26 * w))] >> 24)) & 0xff * 2;
			pixel[2] = ((pixel[(24 + (26 * w))] >> 16)) & 0xff;
			pixel[1] = ((pixel[(24 + (26 * w))] >> 8)) & 0xff;
			pixel[0] = ((pixel[(24 + (26 * w))] >> 0)) & 0xff;
			
			return new Color(
//					(int) (0 * dist * brightnessMultiplier),
//					(int) (0 * dist * brightnessMultiplier),
					(int) ((pixel[0]) * dist * brightnessMultiplier),
					(int) ((pixel[1]) * dist * brightnessMultiplier),
					(int) ((pixel[2]) * dist * brightnessMultiplier),
					(int) (color.getAlpha() * dist)
			);
		}
		
		return new Color(
				(int) (color.getRed() * dist * brightnessMultiplier),
				(int) (color.getGreen() * dist * brightnessMultiplier),
				(int) (color.getBlue() * dist * brightnessMultiplier),
				(int) (color.getAlpha() * dist)
		);
	}
}
