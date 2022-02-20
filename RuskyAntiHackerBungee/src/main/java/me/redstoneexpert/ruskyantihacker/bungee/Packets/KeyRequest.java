package me.redstoneexpert.ruskyantihacker.bungee.Packets;

import io.netty.buffer.ByteBuf;
import net.md_5.bungee.connection.InitialHandler;
import net.md_5.bungee.protocol.AbstractPacketHandler;
import net.md_5.bungee.protocol.DefinedPacket;
import net.md_5.bungee.protocol.packet.EncryptionRequest;
import net.md_5.bungee.protocol.packet.StatusRequest;

public class KeyRequest extends DefinedPacket {
	public void read(ByteBuf buf) {
	}

	public void write(ByteBuf buf) {
	}

	public void handle(AbstractPacketHandler handler) {
		((InitialHandler) handler).unsafe().sendPacket(new KeyResponse());
	}

	public String toString() {
		return "StatusRequest()";
	}

	public KeyRequest() {}

	public boolean equals(Object o) {
		if (o == this) {
			return true;
		} else if (!(o instanceof KeyRequest)) {
			return false;
		} else {
			KeyRequest other = (KeyRequest)o;
			return other.canEqual(this);
		}
	}

	protected boolean canEqual(Object other) {
		return other instanceof StatusRequest;
	}

	public int hashCode() {
		return 1;
	}
}
