package tfc.fearofthedark.styles;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import net.minecraft.text.Style;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Matrix4f;
import tfc.stylesplusplus.api.Color;
import tfc.stylesplusplus.api.ExtraStyle;
import tfc.stylesplusplus.api.util.GeneralUtils;

import java.util.Objects;

public class TorchStyle extends ExtraStyle {
	public double speed;
	public boolean globalTime = false;
	private int tickStart;
	
	public Color lightColor = new Color(255, 255, 100);
	
	public TorchStyle() {
		super(new Identifier("fearofthedark:torch_style"));
		this.speed = 1;
	}
	
	public TorchStyle(double speed) {
		super(new Identifier("fearofthedark:torch_style"));
		this.speed = speed;
	}
	
	@Override
	public void apply(int index, int count, char c, Matrix4f matrix, String str, Style fullStyle) {
	}
	
	@Override
	public Color modifyColor(Color color, int index, int count, char c, String str, Style fullStyle, float brightnessMultiplier) {
		double tick = GeneralUtils.getTime() - tickStart;
//		new java.awt.Color(-29409473);
		tick = (tick / 20d) * speed;
		
		double c1 = Math.cos(tick);
		
		double v1 = ((index + 14) - (c1 * 15));
		double v2 = (((count - index) + 28) - ((1 - c1) * 15));
		v1 = Math.min(v1, v2);
		if (v1 > 8) {
			return new Color(
					(int)(color.getRed() * brightnessMultiplier),
					(int)(color.getGreen() * brightnessMultiplier),
					(int)(color.getBlue() * brightnessMultiplier),
					color.getAlpha()
			);
		}
		v1 = Math.min(8, v1);
		v1 = Math.max(-8, v1);
		v1 = Math.abs(v1);
		v1 += 8;
		v1 /= 24;
		v1 = 1- v1;
		return new Color(
				(int) (MathHelper.lerp(v1, color.getRed(), lightColor.getRed()) * brightnessMultiplier),
				(int) (MathHelper.lerp(v1, color.getGreen(), lightColor.getGreen()) * brightnessMultiplier),
				(int) (MathHelper.lerp(v1, color.getBlue(), lightColor.getBlue()) * brightnessMultiplier),
				255
		);
	}
	
	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		TorchStyle that = (TorchStyle) o;
		return Double.compare(that.speed, speed) == 0 && globalTime == that.globalTime && Objects.equals(lightColor, that.lightColor);
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(speed, globalTime, lightColor);
	}
	
	@Override
	public int modifyWidth(int currentWidth, int index, int count, char c, String str, Style fullStyle) {
		return currentWidth;
	}
	
	@Override
	public JsonObject serialize() {
		JsonObject object = super.serialize();
		
		object.addProperty("speed", speed);
		object.addProperty("globalTime", globalTime);
		object.addProperty("red", lightColor.getRed());
		object.addProperty("green", lightColor.getGreen());
		object.addProperty("blue", lightColor.getBlue());
		
		return object;
	}
	
	@Override
	public void deserialize(JsonObject object) {
		speed = get(object, "speed", 1);
		globalTime = get(object, "globalTime", false);
		
		int red = getInt(object, "red", 255);
		int green = getInt(object, "green", 255);
		int blue = getInt(object, "blue", 100);
		
		lightColor = new Color(red, green, blue);
	}
	
	public int getInt(JsonObject object, String name, int defaultValue) {
		if (object.has(name)) {
			JsonElement element = object.get(name);
			if (element instanceof JsonPrimitive) return element.getAsInt();
		}
		return defaultValue;
	}
	
	public double get(JsonObject object, String name, double defaultValue) {
		if (object.has(name)) {
			JsonElement element = object.get(name);
			if (element instanceof JsonPrimitive) return element.getAsDouble();
		}
		return defaultValue;
	}
	
	public boolean get(JsonObject object, String name, boolean defaultValue) {
		if (object.has(name)) {
			JsonElement element = object.get(name);
			if (element instanceof JsonPrimitive) return element.getAsBoolean();
		}
		return defaultValue;
	}
}
