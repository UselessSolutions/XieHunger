package useless.xiehunger;

import net.minecraft.core.net.handler.NetHandler;
import net.minecraft.core.net.packet.Packet;
import useless.xiehunger.interfaces.ICustomPackets;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class PacketUpdateHunger extends Packet {
	public int hunger;
	public int thirst;
	public int fatigue;
	public PacketUpdateHunger(){

	}
	public PacketUpdateHunger(int hunger, int thirst, int fatigue){
		this.hunger = hunger;
		this.thirst = thirst;
		this.fatigue = fatigue;
	}
	@Override
	public void readPacketData(DataInputStream dataInputStream) throws IOException {
		hunger = dataInputStream.readInt();
		thirst = dataInputStream.readInt();
		fatigue = dataInputStream.readInt();
	}

	@Override
	public void writePacketData(DataOutputStream dataOutputStream) throws IOException {
		dataOutputStream.writeInt(hunger);
		dataOutputStream.writeInt(thirst);
		dataOutputStream.writeInt(fatigue);
	}

	@Override
	public void processPacket(NetHandler netHandler) {
		((ICustomPackets)netHandler).xieHunger$handleHunger(this);
	}

	@Override
	public int getPacketSize() {
		return 12;
	}
}
