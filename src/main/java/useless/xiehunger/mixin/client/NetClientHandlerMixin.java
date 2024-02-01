package useless.xiehunger.mixin.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.net.handler.NetClientHandler;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import useless.xiehunger.PacketUpdateHunger;
import useless.xiehunger.interfaces.ICustomPackets;
import useless.xiehunger.interfaces.IHunger;

@Mixin(value = NetClientHandler.class, remap = false)
public abstract class NetClientHandlerMixin implements ICustomPackets {
	@Final
	@Shadow
	private Minecraft mc;

	@Override
	public void xieHunger$handleHunger(PacketUpdateHunger packetUpdateHunger) {
		((IHunger)mc.thePlayer).xieHunger$updateHunger(packetUpdateHunger.hunger, packetUpdateHunger.thirst, packetUpdateHunger.fatigue);
	}
}
