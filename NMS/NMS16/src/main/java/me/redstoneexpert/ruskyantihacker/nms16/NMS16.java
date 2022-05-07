package me.redstoneexpert.ruskyantihacker.nms16;

import me.redstoneexpert.ruskyantihacker.nmsabstract.NMSUtils;
import me.redstoneexpert.ruskyantihacker.nmsabstract.Property;
import me.redstoneexpert.ruskyantihacker.nmsabstract.ReflectionUtils;
import net.minecraft.server.v1_16_R3.NetworkManager;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_16_R3.CraftServer;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Objects;

public class NMS16 implements NMSUtils {

	@SuppressWarnings("unchecked")
	public NetworkManager getNetworkManager(Player p) {
		List<NetworkManager> managers = (List<NetworkManager>) ReflectionUtils.getValue(Objects.requireNonNull(((CraftServer) Bukkit.getServer()).getServer().getServerConnection()), "connectedChannels");
		assert managers != null;
		for (NetworkManager manager: managers) {
			if (manager.spoofedUUID == p.getUniqueId()) {
				return manager;
			}
		}
		return null;
	}

	@Override
	public Property getSignature(Player player) {
		com.mojang.authlib.properties.Property[] props = getNetworkManager(player).spoofedProfile;
		for (com.mojang.authlib.properties.Property prop : props) {
			if (prop.getName().equals("sessionId")) {
				return new Property(prop.getValue(), prop.getSignature());
			}
		}
		return null;
	}
}
