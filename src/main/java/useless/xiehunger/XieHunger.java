package useless.xiehunger;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import net.minecraft.client.Minecraft;
import net.minecraft.core.item.ItemFood;
import net.minecraft.core.util.helper.DamageType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import turniplabs.halplibe.util.TomlConfigHandler;
import turniplabs.halplibe.util.toml.Toml;


public class XieHunger implements ModInitializer {
    public static final String MOD_ID = "xiehunger";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitialize() {
		FoodLists.init();
        LOGGER.info("XieHunger initialized.");
    }
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

	private static boolean gryllsEnabled = false;
	private static int gryllsTick = 0;
	public static final int CONSTANT = 0;
	public static final int WALKING = 1;
	public static final int JUMPING = 2;
	public static final int SWIMMING = 3;
	public static final int SWINGING = 4;
	public static final int SNEAKING = 5;
	public static final int RESTING = 6;
	public static final int SLEEPING = 7;
	public static TomlConfigHandler cfg;
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

		fatigueRate[CONSTANT] = cfg.getInt("Fatigue Rates.constantRate");
		fatigueRate[WALKING] = cfg.getInt("Fatigue Rates.walkingRate");
		fatigueRate[JUMPING] = cfg.getInt("Fatigue Rates.jumpingRate");
		fatigueRate[SWIMMING] = cfg.getInt("Fatigue Rates.swimmingRate");
		fatigueRate[SWINGING] = cfg.getInt("Fatigue Rates.swingingRate");
		fatigueRate[SNEAKING] = cfg.getInt("Fatigue Rates.sneakingRate");
		fatigueRate[RESTING] = cfg.getInt("Fatigue Rates.restingRate");
		fatigueRate[SLEEPING] = cfg.getInt("Fatigue Rates.sleepingRate");
	}

	public void ModsLoaded() { // For integrations with Xie's other mods
//		if (ModLoader.isModLoaded("mod_XieFarming")) {
//			FoodLists.juiceList.add(XieMod.lemonade);
//			FoodLists.juiceList.add(XieMod.orangeJuice);
//			FoodLists.addItemsToList(new gm[]{XieMod.orange, XieMod.lemon, XieMod.lettuce, XieMod.tomato, XieMod.watermelonPiece, XieMod.fruitSalad}, FoodLists.juicyList);
//			gryllsEnabled = true;
//		}
//
//		if (ModLoader.isModLoaded("mod_XieCooking")) {
//			FoodLists.juicyList.add(XieMod.soup);
//			FoodLists.juicyList.add(XieMod.stew);
//		}

	}



//	private static void doGrylls(Minecraft game) {
//		if (thirst >= thirstMax && game.thePlayer.t() && !game.thePlayer.isInWater() && game.thePlayer.input.moveStrafe  == 0.0F && game.thePlayer.input.moveForward == 0.0F) {
//			if (game.thePlayer.c.b() != null) {
//				if (game.thePlayer.c.b().c == uu.N.bn && game.thePlayer.c.b().a == 1 && thirst >= thirstMax) {
//					if (gryllsTick++ >= 3) {
//						game.thePlayer.c.a[game.thePlayer.c.c] = new iz(XieMod.lemonade);
//					}
//				} else {
//					gryllsTick = 0;
//				}
//			} else {
//				gryllsTick = 0;
//			}
//		} else {
//			gryllsTick = 0;
//		}
//	}




// TODO Properly save data to level instead of seperate file
//	public static void saveStateToFile(Minecraft game) {
//		File dir = new File(Minecraft.b() + "/mods/Xie/Hunger");
//		if (!dir.exists()) {
//			dir.mkdirs();
//		}
//
//		String filename = game.k.b + ".hunger";
//		File f = new File(dir + "/" + filename);
//		Properties props = new Properties();
//		String world;
//		if (game.l()) {
//			world = game.z.C;
//		} else {
//			world = game.theWorld.getLevelData().j();
//		}
//
//		String keyHunger = world + "@hunger";
//		String keyThirst = world + "@thirst";
//		String keyFatigue = world + "@fatigue";
//		String keyClockTick = world + "@clockTick";
//		String lastTickTime = world + "@lastTickTime";
//
//		try {
//			if (!f.exists()) {
//				f.createNewFile();
//			} else {
//				FileReader fr = new FileReader(f);
//				props.load(fr);
//			}
//
//			props.put(keyHunger, "" + hunger);
//			props.put(keyThirst, "" + thirst);
//			props.put(keyFatigue, "" + fatigue);
//			props.put(keyClockTick, "" + tickCounter);
//			props.put(lastTickTime, "" + timeOfLastTick);
//			FileWriter fw = new FileWriter(f);
//			props.store(fw, (String)null);
//			fw.close();
//		} catch (IOException var12) {
//			var12.printStackTrace();
//		}
//
//	}
//
//	public static void loadStateFromFile(Minecraft mc) {
//		resetState();
//		File dir = new File(Minecraft.b() + "/mods/Xie/Hunger");
//		String filename = mc.k.b + ".hunger";
//		File f = new File(dir + "/" + filename);
//		Properties props = new Properties();
//		String world;
//		if (mc.l()) {
//			world = mc.z.C;
//		} else {
//			world = mc.f.x.j();
//		}
//
//		String keyHunger = world + "@hunger";
//		String keyThirst = world + "@thirst";
//		String keyFatigue = world + "@fatigue";
//		String keyClockTick = world + "@clockTick";
//		String lastTickTime = world + "@lastTickTime";
//
//		try {
//			if (!f.exists()) {
//				System.out.println("Couldn't find hunger file " + f.getCanonicalPath());
//			} else {
//				FileReader fr = new FileReader(f);
//				props.load(fr);
//				if (props.containsKey(keyHunger)) {
//					hunger = Integer.parseInt(props.getProperty(keyHunger));
//				} else {
//					System.out.println("Hunger information for world/server " + world + " not found.");
//				}
//
//				if (props.containsKey(keyThirst)) {
//					thirst = Integer.parseInt(props.getProperty(keyThirst));
//				} else {
//					System.out.println("Thirst information for world/server " + world + " not found.");
//				}
//
//				if (props.containsKey(keyFatigue)) {
//					fatigue = Integer.parseInt(props.getProperty(keyFatigue));
//				} else {
//					System.out.println("Fatigue information for world/server " + world + " not found.");
//				}
//
//				if (props.containsKey(keyClockTick)) {
//					tickCounter = Integer.parseInt(props.getProperty(keyClockTick));
//				}
//
//				if (props.containsKey(lastTickTime)) {
//					timeOfLastTick = Long.parseLong(props.getProperty(lastTickTime));
//				}
//			}
//		} catch (IOException var12) {
//			var12.printStackTrace();
//		}
//
//	}
}
