package useless.xiehunger;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiIngame;
import net.minecraft.core.block.material.Material;
import org.lwjgl.opengl.GL11;

public class XieHungerDisplay {
	public static XieHungerDisplay instance = new XieHungerDisplay();
	public XieHungerDisplay() {
	}

	public void display(Minecraft minecraft) {
		if (minecraft.theWorld == null) return;
		if (XieHunger.useBars) {
			this.displayBars(minecraft);
		} else {
			this.displayIcons(minecraft);
		}

	}

	public void displayBars(Minecraft minecraft) {
		GuiIngame ingame = minecraft.ingameGUI;
		if (ingame == null) return;
		int width = minecraft.resolution.scaledWidth;
		int height = minecraft.resolution.scaledHeight;
		int displayX = width / 2 - 91;
		int displayY = height - 41;

		GL11.glBindTexture(3553, minecraft.renderEngine.getTexture("/assets/xiehunger/gui/xiehunger.png"));
		GL11.glColor4f(1,1,1,1);

		if (minecraft.thePlayer.isUnderLiquid(Material.water)) {
			displayY -= 9;
		}

		int xOffset;
		int skullY;
		int fat4;
		if (XieHunger.hungerEnabled) {
			xOffset = 0;
			skullY = 0;
			if (!XieHunger.thirstEnabled) {
				skullY += 3;
			}

			if (!XieHunger.fatigueEnabled) {
				skullY += 3;
			}

			for(fat4 = 0; fat4 < XieHunger.hunger; ++fat4) {
				ingame.drawTexturedModalRect(displayX + xOffset, displayY + skullY, 8, 0, 4, 3);
				xOffset += 4;
			}
		}

		if (XieHunger.thirstEnabled) {
			xOffset = 0;
			skullY = 3;
			if (!XieHunger.fatigueEnabled) {
				skullY += 3;
			}

			for(fat4 = 0; fat4 < XieHunger.thirst; ++fat4) {
				ingame.drawTexturedModalRect(displayX + xOffset, displayY + skullY, 12, 0, 4, 3);
				xOffset += 4;
			}
		}

		if (XieHunger.fatigueEnabled) {
			xOffset = 0;
			int yOffset = 6;
			fat4 = XieHunger.fatigueScaled / 4;

			int rem;
			for(rem = 0; rem < fat4; ++rem) {
				ingame.drawTexturedModalRect(displayX + xOffset, displayY + yOffset, 16, 0, 4, 3);
				xOffset += 4;
			}

			rem = XieHunger.fatigueScaled % 4;
			if (rem > 0) {
				ingame.drawTexturedModalRect(displayX + xOffset, displayY + yOffset, 16, 0, rem, 3);
			}
		}

		xOffset = width / 2 - 8;
		skullY = height - 40;
		if (XieHunger.dying && (XieHunger.hungerEnabled || XieHunger.thirstEnabled || XieHunger.fatigueEnabled)) {
			ingame.drawTexturedModalRect(xOffset, skullY, 32, 0, 8, 8);
		}

	}

	public void displayIcons(Minecraft minecraft) {
		GuiIngame ingame = minecraft.ingameGUI;
		if (ingame == null) return;
		int width = minecraft.resolution.scaledWidth;
		int height = minecraft.resolution.scaledHeight;
		int xieHungerIconX = width / 2 - 8;
		int xieHungerIconY = height - 40;

		GL11.glBindTexture(3553, minecraft.renderEngine.getTexture("/assets/xiehunger/gui/xiehunger.png"));
		GL11.glColor4f(1,1,1,1);

		if (XieHunger.hungerEnabled) {
			ingame.drawTexturedModalRect(xieHungerIconX, xieHungerIconY + 8, XieHunger.hungerState * 8, 8, 8, 8);
		}

		if (XieHunger.thirstEnabled) {
			ingame.drawTexturedModalRect(xieHungerIconX + 8, xieHungerIconY + 8, XieHunger.thirstState * 8, 16, 8, 8);
		}

		if (XieHunger.fatigueEnabled) {
			ingame.drawTexturedModalRect(xieHungerIconX + 8, xieHungerIconY, XieHunger.fatigueState * 8, 24, 8, 8);
		}

		if (XieHunger.dying && (XieHunger.hungerEnabled || XieHunger.thirstEnabled || XieHunger.fatigueEnabled)) {
			ingame.drawTexturedModalRect(xieHungerIconX, xieHungerIconY, 32, 0, 8, 8);
		}

	}
}
