package me.redstoneexpert.ruskyantihacker.spigot;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class TCPUtils {

	public static byte[] getKey() {
		byte[] key = null;
		try {
			Socket socket = new Socket(Main.plugin.getConfig().getString("bungee-ip"), Main.plugin.getConfig().getInt("bungee-port"));
			OutputStream outputStream = socket.getOutputStream();
			InputStream inputStream = socket.getInputStream();
			outputStream.write(new byte[]{0x06, 0x00, 0x00, 0x00, 0x00, 0x00, 0x01});
			outputStream.write(new byte[]{0x01, 0x02});
			int len = readVarInt(inputStream);
			if (len > 1) {
				int id = readVarInt(inputStream);
				if (id == 2) {
					int keyLen = readVarInt(inputStream);
					if (keyLen > 0) {
						key = inputStream.readNBytes(keyLen);
					}
				}
			}
			outputStream.close();
			inputStream.close();
			socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return key;
	}

	private static int readVarInt(InputStream in) throws IOException {
		int i = 0;
		int j = 0;
		while (true) {
			int k = in.readNBytes(1)[0];
			i |= (k & 0x7F) << j++ * 7;
			if (j > 5) throw new RuntimeException("VarInt too big");
			if ((k & 0x80) != 128) break;
		}
		return i;
	}
}
