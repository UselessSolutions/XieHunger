package useless.xiehunger.mixin.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiIngame;
import net.minecraft.core.block.material.Material;
import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import useless.xiehunger.IHunger;
import useless.xiehunger.XieHunger;

@Mixin(value = GuiIngame.class, remap = false)
public class GuiIngameMixin extends Gui {
	@Shadow
	protected Minecraft mc;

	@Inject(method = "renderGameOverlay(FZII)V", at = @At("TAIL"))
	private void drawHunger(float partialTicks, boolean flag, int mouseX, int mouseY, CallbackInfo ci){
		GL11.glBindTexture(3553, mc.renderEngine.getTexture("/assets/xiehunger/gui/xiehunger.png"));
		GL11.glColor4f(1,1,1,1);
		IHunger hPlayer = (IHunger)mc.thePlayer;
		int width = mc.resolution.scaledWidth;
		int height = mc.resolution.scaledHeight;
		if (XieHunger.useBars) {
			int displayX = width / 2 - 91;
			int displayY = height - 41;

			if (mc.thePlayer.isUnderLiquid(Material.water)) {
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

				for(fat4 = 0; fat4 < hPlayer.getHunger(); ++fat4) {
					drawTexturedModalRect(displayX + xOffset, displayY + skullY, 8, 0, 4, 3);
					xOffset += 4;
				}
			}

			if (XieHunger.thirstEnabled) {
				xOffset = 0;
				skullY = 3;
				if (!XieHunger.fatigueEnabled) {
					skullY += 3;
				}

				for(fat4 = 0; fat4 < hPlayer.getThirst(); ++fat4) {
					drawTexturedModalRect(displayX + xOffset, displayY + skullY, 12, 0, 4, 3);
					xOffset += 4;
				}
			}

			if (XieHunger.fatigueEnabled) {
				xOffset = 0;
				int yOffset = 6;
				fat4 = hPlayer.getFatigueScaled() / 4;

				int rem;
				for(rem = 0; rem < fat4; ++rem) {
					drawTexturedModalRect(displayX + xOffset, displayY + yOffset, 16, 0, 4, 3);
					xOffset += 4;
				}

				rem = hPlayer.getFatigueScaled() % 4;
				if (rem > 0) {
					drawTexturedModalRect(displayX + xOffset, displayY + yOffset, 16, 0, rem, 3);
				}
			}

			xOffset = width / 2 - 8;
			skullY = height - 40;
			if (hPlayer.isDying() && (XieHunger.hungerEnabled || XieHunger.thirstEnabled || XieHunger.fatigueEnabled)) {
				drawTexturedModalRect(xOffset, skullY, 32, 0, 8, 8);
			}

		} else {
			int xieHungerIconX = width / 2 - 8;
			int xieHungerIconY = height - 40;

			if (XieHunger.hungerEnabled) {
				drawTexturedModalRect(xieHungerIconX, xieHungerIconY + 8, hPlayer.getHungerState() * 8, 8, 8, 8);
			}

			if (XieHunger.thirstEnabled) {
				drawTexturedModalRect(xieHungerIconX + 8, xieHungerIconY + 8, hPlayer.getThirstState() * 8, 16, 8, 8);
			}

			if (XieHunger.fatigueEnabled) {
				drawTexturedModalRect(xieHungerIconX + 8, xieHungerIconY, hPlayer.getFatigueState() * 8, 24, 8, 8);
			}

			if (hPlayer.isDying() && (XieHunger.hungerEnabled || XieHunger.thirstEnabled || XieHunger.fatigueEnabled)) {
				drawTexturedModalRect(xieHungerIconX, xieHungerIconY, 32, 0, 8, 8);
			}
		}
	}
}
