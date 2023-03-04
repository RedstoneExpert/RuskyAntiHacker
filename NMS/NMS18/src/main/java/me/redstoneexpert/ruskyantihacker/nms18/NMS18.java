package me.redstoneexpert.ruskyantihacker.nms18;

import me.redstoneexpert.ruskyantihacker.nmsabstract.NMSUtils;
import me.redstoneexpert.ruskyantihacker.nmsabstract.Property;
import me.redstoneexpert.ruskyantihacker.nmsabstract.ReflectionUtils;
import net.minecraft.network.NetworkManager;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_18_R2.CraftServer;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Objects;

public class NMS18 implements NMSUtils {

	@SuppressWarnings("unchecked")
	public NetworkManager getNetworkManager(Player p) {
		List<NetworkManager> managers = (List<NetworkManager>) ReflectionUtils.getValue(Objects.requireNonNull(((CraftServer) Bukkit.getServer()).getServer().ad()), "g");
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
		if (props == null) return  null;
		for (com.mojang.authlib.properties.Property prop : props) {
			if (prop.getName().equals("sessionId")) {
				return new Property(prop.getValue(), prop.getSignature());
			}
		}
		return null;
	}
}
