package me.redstoneexpert.ruskyantihacker.bungee;

import me.redstoneexpert.ruskyantihacker.bungee.Events.onServerConnect;
import net.md_5.bungee.api.plugin.Plugin;

public class Main extends Plugin {
	public static int sessionId = 0;

	@Override
	public void onEnable() {
		PacketRegisterer.register();

		getProxy().getPluginManager().registerListener(this, new onServerConnect());
	}

	@Override
	public void onDisable() {
		// Plugin shutdown logic
	}
}
