package useless.xiehunger.interfaces;

import net.minecraft.core.item.ItemFood;

public interface IHunger {
	boolean xieHunger$inTheGreen();
	void xieHunger$feed(int amount, ItemFood food);
	void xieHunger$feed(int amount);
	int xieHunger$getHungerState();
	int xieHunger$getThirstState();
	int xieHunger$getFatigueState();
	int xieHunger$getFatigueScaled();
	int xieHunger$getHunger();
	int xieHunger$getThirst();
	int xieHunger$getFatigue();
	boolean xieHunger$isDying();
	void xieHunger$updateHunger(int hunger, int thirst, int fatigue);
}
