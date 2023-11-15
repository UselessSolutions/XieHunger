package useless.xiehunger;

import net.minecraft.core.item.ItemFood;

public interface IHunger {
	boolean inTheGreen();
	void feed(int amount, ItemFood food);
	void feed(int amount);
	int getHungerState();
	int getThirstState();
	int getFatigueState();
	int getFatigueScaled();
	int getHunger();
	int getThirst();
	int getFatigue();
	boolean isDying();
}
