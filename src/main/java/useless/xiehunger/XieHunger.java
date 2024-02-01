package useless.xiehunger;

import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import turniplabs.halplibe.helper.NetworkHelper;
import turniplabs.halplibe.util.GameStartEntrypoint;


public class XieHunger implements ModInitializer, GameStartEntrypoint {
    public static final String MOD_ID = "xiehunger";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	// Initialization can be weird so this is still required for packets.
	@Override
	public void onInitialize() {
		NetworkHelper.register(PacketUpdateHunger.class, false, true);
	}

	@Override
	public void beforeGameStart() {
		FoodLists.init();

		if (HungerConfig.cfg == null) HungerConfig.writeConfig();
		else HungerConfig.cfg.loadConfig();

		LOGGER.info("XieHunger initialized.");
	}

	@Override
	public void afterGameStart() {

	}
}
