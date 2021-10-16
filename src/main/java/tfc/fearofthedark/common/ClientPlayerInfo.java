package tfc.fearofthedark.common;

public interface ClientPlayerInfo {
	boolean FearOfTheDark_isPocketAware();
	void FearOfTheDark_setPocketAware(boolean value);
	int getTicksOutOfSun();
	void setTicksOutOfSun(int amt);
	void setLastBlur(float amt);
	float getLastBlur();
}
