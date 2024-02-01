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
import useless.xiehunger.HungerConfig;
import useless.xiehunger.interfaces.IHunger;

@Mixin(value = GuiIngame.class, remap = false)
public abstract class GuiIngameMixin extends Gui {
	@Shadow
	protected Minecraft mc;

	@Inject(method = "renderGameOverlay(FZII)V", at = @At("TAIL"))
	private void xie_drawHunger(float partialTicks, boolean flag, int mouseX, int mouseY, CallbackInfo ci){
		GL11.glBindTexture(3553, mc.renderEngine.getTexture("/assets/xiehunger/gui/xiehunger.png"));
		GL11.glColor4f(1,1,1,1);
		IHunger hPlayer = (IHunger)mc.thePlayer;
		int width = mc.resolution.scaledWidth;
		int height = mc.resolution.scaledHeight;
		int sp = (int)(this.mc.gameSettings.screenPadding.value * (float)height / 8.0f);
		if (HungerConfig.useBars) {
			int displayX = width / 2 - 91;
			int displayY = height - 41 - sp;

			if (mc.thePlayer.isUnderLiquid(Material.water)) {
				displayY -= 9;
			}

			int xOffset;
			int skullY;
			int fat4;
			if (HungerConfig.hungerEnabled) {
				xOffset = 0;
				skullY = 0;
				if (!HungerConfig.thirstEnabled) {
					skullY += 3;
				}

				if (!HungerConfig.fatigueEnabled) {
					skullY += 3;
				}

				for(fat4 = 0; fat4 < hPlayer.xieHunger$getHunger(); ++fat4) {
					drawTexturedModalRect(displayX + xOffset, displayY + skullY, 8, 0, 4, 3);
					xOffset += 4;
				}
			}

			if (HungerConfig.thirstEnabled) {
				xOffset = 0;
				skullY = 3;
				if (!HungerConfig.fatigueEnabled) {
					skullY += 3;
				}

				for(fat4 = 0; fat4 < hPlayer.xieHunger$getThirst(); ++fat4) {
					drawTexturedModalRect(displayX + xOffset, displayY + skullY, 12, 0, 4, 3);
					xOffset += 4;
				}
			}

			if (HungerConfig.fatigueEnabled) {
				xOffset = 0;
				int yOffset = 6;
				fat4 = hPlayer.xieHunger$getFatigueScaled() / 4;

				int rem;
				for(rem = 0; rem < fat4; ++rem) {
					drawTexturedModalRect(displayX + xOffset, displayY + yOffset, 16, 0, 4, 3);
					xOffset += 4;
				}

				rem = hPlayer.xieHunger$getFatigueScaled() % 4;
				if (rem > 0) {
					drawTexturedModalRect(displayX + xOffset, displayY + yOffset, 16, 0, rem, 3);
				}
			}

			xOffset = width / 2 - 8;
			skullY = height - 40;
			if (hPlayer.xieHunger$isDying() && (HungerConfig.hungerEnabled || HungerConfig.thirstEnabled || HungerConfig.fatigueEnabled)) {
				drawTexturedModalRect(xOffset, skullY, 32, 0, 8, 8);
			}

		} else {
			int xieHungerIconX = width / 2 - 8;
			int xieHungerIconY = height - 40 - sp;

			if (HungerConfig.hungerEnabled) {
				drawTexturedModalRect(xieHungerIconX, xieHungerIconY + 8, hPlayer.xieHunger$getHungerState() * 8, 8, 8, 8);
			}

			if (HungerConfig.thirstEnabled) {
				drawTexturedModalRect(xieHungerIconX + 8, xieHungerIconY + 8, hPlayer.xieHunger$getThirstState() * 8, 16, 8, 8);
			}

			if (HungerConfig.fatigueEnabled) {
				drawTexturedModalRect(xieHungerIconX + 8, xieHungerIconY, hPlayer.xieHunger$getFatigueState() * 8, 24, 8, 8);
			}

			if (hPlayer.xieHunger$isDying() && (HungerConfig.hungerEnabled || HungerConfig.thirstEnabled || HungerConfig.fatigueEnabled)) {
				drawTexturedModalRect(xieHungerIconX, xieHungerIconY, 32, 0, 8, 8);
			}
		}
	}
}
