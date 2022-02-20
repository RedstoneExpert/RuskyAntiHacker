package me.redstoneexpert.ruskyantihacker.bungee.Packets;

import io.netty.buffer.ByteBuf;
import net.md_5.bungee.EncryptionUtil;
import net.md_5.bungee.protocol.AbstractPacketHandler;
import net.md_5.bungee.protocol.DefinedPacket;

public class KeyResponse extends DefinedPacket {
	@Override
	public void write(ByteBuf buf) {
		writeArray(EncryptionUtil.keys.getPublic().getEncoded(), buf);
	}

	@Override
	public void handle(AbstractPacketHandler handler) {
	}

	@Override
	public boolean equals(Object obj) {
		return false;
	}

	@Override
	public int hashCode() {
		return 0;
	}

	@Override
	public String toString() {
		return null;
	}
}
