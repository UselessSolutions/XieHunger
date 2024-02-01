package useless.xiehunger;

import net.minecraft.core.item.Item;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FoodLists {
	public static boolean initialised = false;
	public static List<Item> sammichList = new ArrayList<>();
	public static List<Item> sauceList = new ArrayList<>();
	public static List<Item> fruitList = new ArrayList<>();
	public static List<Item> flavorList = new ArrayList<>();
	public static List<Item> saladList = new ArrayList<>();
	public static List<Item> juicyList = new ArrayList<>();
	public static List<Item> juiceList = new ArrayList<>();
	public static List<Item> waterList = new ArrayList<>();
	public static List<Item> customFoodList = new ArrayList<>();

	public static void init() {
		if (!initialised) {
			setDefaults();
			initialised = true;
		}
	}

	private static void setDefaults() {
		addItemsToList(new Item[]{Item.foodPorkchopCooked, Item.foodFishCooked}, sammichList);
		addItemsToList(new Item[]{Item.foodAppleGold, Item.foodApple}, fruitList);
		addItemsToList(new Item[]{Item.dustSugar, Item.dye}, flavorList);
		addItemsToList(new Item[]{Item.foodApple, Item.foodAppleGold}, saladList);
		addItemsToList(new Item[]{Item.foodApple, Item.foodAppleGold, Item.foodFishRaw}, juicyList);
	}

	public static void addItemToLists(Item item, List<Item>[] lists) {
		for (List<Item> list : lists) {
			list.add(item);
		}
	}

	public static void addItemsToList(Item[] items, List<Item> list) {
		list.addAll(Arrays.asList(items));
	}
}
