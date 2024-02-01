package useless.xiehunger;

import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import turniplabs.halplibe.util.TomlConfigHandler;
import turniplabs.halplibe.util.toml.Toml;

import static useless.xiehunger.XieHunger.MOD_ID;

public class HungerConfig {
    public static TomlConfigHandler cfg;
	public static int hungerRate = 60;
	public static boolean passiveRegen = false;
	public static int hungerMax = 20;
	public static int hungerStateFactor = 5;
	public static int thirstRate = 20;
	public static boolean waterDrinkable = true;
	public static int thirstMax = 20;
	public static int thirstStateFactor = 5;
	public static int fatigueMax = 1000;
	public static int fatigueScaledMax = 80;
	public static int fatigueStateFactor = fatigueMax / 4;
	public static int fatigueHeartScaleFactor = fatigueMax / 20;
	public static int[] fatigueRate = new int[]{0, 2, 5, 5, 5, 5, -1, -3};
	public static int hungerClockTickRate = 20;
	public static boolean hungerEnabled = true;
	public static boolean thirstEnabled = true;
	public static boolean fatigueEnabled = true;
	public static boolean useBars = true;
	public static boolean logBars = false;
	public static final int CONSTANT = 0;
	public static final int WALKING = 1;
	public static final int JUMPING = 2;
	public static final int SWIMMING = 3;
	public static final int SWINGING = 4;
	public static final int SNEAKING = 5;
	public static final int RESTING = 6;
	public static final int SLEEPING = 7;

	static {
		ModContainer thisMod = FabricLoader.getInstance().getModContainer("xiehunger").get();
		Toml defaults = new Toml(thisMod.getMetadata().getName() + " " + thisMod.getMetadata().getVersion() + " settings");
		defaults.addCategory("Toggles")
			.addEntry("enableHunger", hungerEnabled)
			.addEntry("enableThirst", thirstEnabled)
			.addEntry("enableFatigue", fatigueEnabled)
			.addEntry("waterDrinkable","Water drinkable by crouching", waterDrinkable);
		defaults.addCategory("Timings")
			.addEntry("clockRate", "Game ticks per mod tick",  hungerClockTickRate)
			.addEntry("hungerRate", "Ticks between increasing hunger or thirst", hungerRate)
			.addEntry("thirstRate", thirstRate);
		defaults.addCategory("Saturated Benefits")
			.addEntry("passiveRegen", passiveRegen);
		defaults.addCategory("GUI")
			.addEntry("useBars", useBars);
		defaults.addCategory("Fatigue Rates")
			.addEntry("constantRate", fatigueRate[CONSTANT])
			.addEntry("walkingRate", fatigueRate[WALKING])
			.addEntry("jumpingRate", fatigueRate[JUMPING])
			.addEntry("swimmingRate", fatigueRate[SWIMMING])
			.addEntry("swingingRate", fatigueRate[SWINGING])
			.addEntry("sneakingRate", fatigueRate[SNEAKING])
			.addEntry("restingRate", fatigueRate[RESTING])
			.addEntry("sleepingRate", fatigueRate[SLEEPING]);
		defaults.addCategory("Debug")
			.addEntry("logBars", logBars);

		cfg = new TomlConfigHandler(MOD_ID, defaults);

		hungerEnabled = cfg.getBoolean("Toggles.enableHunger");
		thirstEnabled = cfg.getBoolean("Toggles.enableThirst");
		fatigueEnabled = cfg.getBoolean("Toggles.enableFatigue");
		waterDrinkable = cfg.getBoolean("Toggles.waterDrinkable");
		hungerClockTickRate = cfg.getInt("Timings.clockRate");
		hungerRate = cfg.getInt("Timings.hungerRate");
		thirstRate = cfg.getInt("Timings.thirstRate");
		passiveRegen = cfg.getBoolean("Saturated Benefits.passiveRegen");
		useBars = cfg.getBoolean("GUI.useBars");
		logBars = cfg.getBoolean("Debug.logBars");

		fatigueRate[CONSTANT] = cfg.getInt("Fatigue Rates.constantRate");
		fatigueRate[WALKING] = cfg.getInt("Fatigue Rates.walkingRate");
		fatigueRate[JUMPING] = cfg.getInt("Fatigue Rates.jumpingRate");
		fatigueRate[SWIMMING] = cfg.getInt("Fatigue Rates.swimmingRate");
		fatigueRate[SWINGING] = cfg.getInt("Fatigue Rates.swingingRate");
		fatigueRate[SNEAKING] = cfg.getInt("Fatigue Rates.sneakingRate");
		fatigueRate[RESTING] = cfg.getInt("Fatigue Rates.restingRate");
		fatigueRate[SLEEPING] = cfg.getInt("Fatigue Rates.sleepingRate");
	}

	public static void writeConfig() {
		cfg.writeConfig();
	}
}
