package tfc.fearofthedark.client;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.texture.AbstractTexture;
import org.lwjgl.opengl.GL11;
import tfc.stylesplusplus.api.Color;

public class Utils {
	public static int[] getSkinColor(double coordModifier) {
		AbstractTexture texture = MinecraftClient.getInstance().getTextureManager().getTexture(MinecraftClient.getInstance().player.getSkinTexture());
		
		int w = 64;
		int[] pixel = new int[w * w];
		// https://stackoverflow.com/questions/31417914/opengl-reading-pixels-from-texture
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture.getGlId());
		GL11.glGetTexImage(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA, GL11.GL_BYTE, pixel);
//			BufferedImage image = new BufferedImage(w, w, BufferedImage.TYPE_INT_ARGB);
//			for (int x = 0; x < w; x++) {
//				for (int y = 0; y < w; y++) {
//					Color col = new Color(pixel[x + (y * w)]);
//					image.setRGB(x, y, new Color(col.getBlue(), col.getGreen(), col.getRed(), col.getAlpha()).getRGB());
//				}
//			}
		
		int[] color = new int[4];
		
		int x = (int) (coordModifier < 0 ? Math.ceil(23.5 + (coordModifier * 4)) : Math.floor(23.5 + (coordModifier * 4)));
		int y = (26 * w);
		color[3] = ((pixel[(x + y)] >> 24)) & 0xff;
		color[2] = ((pixel[(x + y)] >> 16)) & 0xff;
//		color[2] = ((pixel[(24 + (26 * w))] >> 16)) & 0xff;
		color[1] = ((pixel[(x + y)] >> 8)) & 0xff;
//		color[1] = ((pixel[(24 + (26 * w))] >> 8)) & 0xff;
		color[0] = ((pixel[(x + y)])) & 0xff;
//		color[0] = ((pixel[(24 + (26 * w))] >> 0)) & 0xff;
		
		color[3] *= 2;
		color[2] *= 2;
		color[1] *= 2;
		color[0] *= 2;
		
		return color;
	}
	
	public static double invRound(double val) {
		double v = val - (int) val;
		if (v < 0.5) return val;
		else return val + 0.5;
	}
}
