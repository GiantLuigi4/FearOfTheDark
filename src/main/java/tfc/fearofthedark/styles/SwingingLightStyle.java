package tfc.fearofthedark.styles;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import net.minecraft.text.Style;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Matrix4f;
import tfc.stylesplusplus.api.Color;
import tfc.stylesplusplus.api.ExtraStyle;
import tfc.stylesplusplus.api.util.GeneralUtils;

import java.util.Objects;

public class SwingingLightStyle extends ExtraStyle {
	public double speed;
	public boolean globalTime = false;
	private int tickStart = GeneralUtils.getTime();
	
	public Color lightColor = new Color(255, 255, 100);
	
	public SwingingLightStyle() {
		super(new Identifier("fearofthedark:swinging_light"));
		this.speed = 1;
	}
	
	public SwingingLightStyle(double speed) {
		super(new Identifier("fearofthedark:swinging_light"));
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
		double v = Math.sin(tick) + 1;
		v /= 2;
		int end = 2;
		int start = 2;
		v *= ((count - 1) - (end + start));
		v += start;
		double dist = 1 - (Math.abs(index - v) / count);
		dist *= Math.pow(dist, count / 5f);
		color = new Color(
				(int) (Math.max(color.getRed(), lightColor.getRed() * dist) * brightnessMultiplier),
				(int) (Math.max(color.getGreen(), lightColor.getGreen() * dist) * brightnessMultiplier),
				(int) (Math.max(color.getBlue(), lightColor.getBlue() * dist) * brightnessMultiplier),
				color.getAlpha()
		);
		return color;
	}
	
	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		SwingingLightStyle that = (SwingingLightStyle) o;
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
