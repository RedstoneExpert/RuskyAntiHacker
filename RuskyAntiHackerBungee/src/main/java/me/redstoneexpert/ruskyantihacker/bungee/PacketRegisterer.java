package me.redstoneexpert.ruskyantihacker.bungee;

import me.redstoneexpert.ruskyantihacker.bungee.Packets.KeyRequest;
import me.redstoneexpert.ruskyantihacker.bungee.Packets.KeyResponse;
import net.md_5.bungee.protocol.DefinedPacket;
import net.md_5.bungee.protocol.Protocol;
import net.md_5.bungee.protocol.ProtocolConstants;

import java.lang.reflect.*;
import java.sql.ClientInfoStatus;
import java.util.function.Supplier;

public class PacketRegisterer {
	private static Object StatusToServer;
	private static Object StatusToClient;
	private static Method regPacket;
	private static Class<?> mapping;
	private static Constructor<?> mappingConstructor;

	private static Object map(int i1, int i2) throws InvocationTargetException, InstantiationException, IllegalAccessException {
		return mappingConstructor.newInstance(i1, i2);
	}

	private static <T extends DefinedPacket> void registerPaket(boolean response, Class<T> c, Supplier<T> supplier, Object... mappings) throws InvocationTargetException, IllegalAccessException {
		Object[] m = (Object[]) Array.newInstance(mapping, mappings.length);
		System.arraycopy(mappings, 0, m, 0, mappings.length);
		regPacket.invoke(response ? StatusToClient :StatusToServer, c, supplier, m);
	}

	public static void register()  {
		try {
			Protocol status = Protocol.STATUS;
			Field toServerF = Protocol.class.getDeclaredField("TO_SERVER");
			Field toClientF = Protocol.class.getDeclaredField("TO_CLIENT");
			toServerF.setAccessible(true);
			toClientF.setAccessible(true);
			Class<?> directionData = Class.forName("net.md_5.bungee.protocol.Protocol$DirectionData");
			mapping = Class.forName("net.md_5.bungee.protocol.Protocol$ProtocolMapping");
			regPacket = directionData.getDeclaredMethod("registerPacket", Class.class, Supplier.class, Array.newInstance(mapping, 0).getClass());
			regPacket.setAccessible(true);
			StatusToServer = toServerF.get(status);
			StatusToClient = toClientF.get(status);
			mappingConstructor = mapping.getConstructor(int.class, int.class);
			mappingConstructor.setAccessible(true);

			registerPaket(false, KeyRequest.class, KeyRequest::new, map(ProtocolConstants.MINECRAFT_1_8, 0x02));
			registerPaket(true, KeyResponse.class, KeyResponse::new, map(ProtocolConstants.MINECRAFT_1_8, 0x02));
		} catch (NoSuchFieldException | ClassNotFoundException | IllegalAccessException | NoSuchMethodException | InstantiationException | InvocationTargetException e) {
			e.printStackTrace();
		}
	}
}
