package tfc.fearofthedark.common;

public interface IHaveFear extends IHavePhase {
	float FearOfTheDark_getFactor();
	void FearOfTheDark_setFactor(float amt);
	
	int FearOfTheDark_getTicks();
	void FearOfTheDark_setTicks(int amt);
	
	int FearOfTheDark_getPhase();
	void FearOfTheDark_setPhase(int amt);
}
