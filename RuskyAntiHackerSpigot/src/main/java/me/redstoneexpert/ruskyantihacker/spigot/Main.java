package me.redstoneexpert.ruskyantihacker.spigot;

import me.redstoneexpert.ruskyantihacker.nms17.NMS17;
import me.redstoneexpert.ruskyantihacker.nmsabstract.NMSUtils;
import me.redstoneexpert.ruskyantihacker.spigot.Events.onLogin;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.*;
import java.security.GeneralSecurityException;
import java.security.PublicKey;

public class Main extends JavaPlugin {
	public static NMSUtils nmsUtils;

	public static int lastId = -1;

	public static PublicKey key;

	public static Main plugin;

	@Override
	public void onEnable() {
		plugin = this;

		switch (VersionUtils.getVersion()) {
			case "v1_17_R1" -> nmsUtils = new NMS17();
			default -> throw new UnsupportedClassVersionError();
		}

		saveDefaultConfig();

		try {
			DataInputStream in = new DataInputStream(new FileInputStream(new File(getDataFolder(), "data.dat")));
			lastId = in.readInt();
			int keyLen = in.readInt();
			if (keyLen > 0) {
				key = EncryptionUtils.getPubKey(in.readNBytes(keyLen));
			}
		} catch (FileNotFoundException ignored) {
		} catch (IOException | GeneralSecurityException e) {
			e.printStackTrace();
		}

		Bukkit.getPluginManager().registerEvents(new onLogin(), this);

		if (key == null) {
			new Thread(() -> {
				try {
					key = EncryptionUtils.getPubKey(TCPUtils.getKey());
				} catch (GeneralSecurityException e) {
					e.printStackTrace();
				}
			}).start();
		}
	}

	@Override
	public void onDisable() {
		try {
			File file = new File(getDataFolder(), "data.dat");
			DataOutputStream out = new DataOutputStream(new FileOutputStream(file));
			out.writeInt(lastId);
			if (key != null) {
				out.writeInt(key.getEncoded().length);
				out.write(key.getEncoded());
			} else {
				out.writeInt(0);
			}
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
