package me.redstoneexpert.ruskyantihacker.spigot;

import org.bukkit.Bukkit;

public class VersionUtils {
	public static String getVersion() {
		final String packageName = Bukkit.getServer().getClass().getPackage().getName();
		return packageName.substring(packageName.lastIndexOf('.') + 1);
	}
}
