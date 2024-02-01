package useless.xiehunger.mixin.core;

import net.minecraft.core.net.handler.NetHandler;
import net.minecraft.core.net.packet.Packet;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import useless.xiehunger.PacketUpdateHunger;
import useless.xiehunger.interfaces.ICustomPackets;

@Mixin(value = NetHandler.class, remap = false)
public abstract class NetHandlerMixin implements ICustomPackets {
	@Shadow
	public abstract void handleInvalidPacket(Packet packet);

	@Override
	public void xieHunger$handleHunger(PacketUpdateHunger packetUpdateHunger) {
		handleInvalidPacket(packetUpdateHunger);
	}
}
