package useless.xiehunger;

import net.fabricmc.api.ModInitialItemStacker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class XieHunger implements ModInitialItemStacker {
    public static final String MOD_ID = "xiehunger";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitialItemStacke() {
		FoodLists.init();
        LOGGER.info("XieHunger initialItemStacked.");
    }
}
