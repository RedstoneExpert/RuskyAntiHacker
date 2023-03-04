package me.redstoneexpert.ruskyantihacker.bungee.Events;

import me.redstoneexpert.ruskyantihacker.bungee.Main;
import net.md_5.bungee.EncryptionUtil;
import net.md_5.bungee.api.event.ServerConnectEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.connection.InitialHandler;
import net.md_5.bungee.connection.LoginResult;
import net.md_5.bungee.event.EventHandler;
import net.md_5.bungee.protocol.Property;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

public class onServerConnect implements Listener {

	@EventHandler
	public void event(ServerConnectEvent e) {
		try {
			Cipher cipher = Cipher.getInstance("RSA");
			cipher.init(Cipher.ENCRYPT_MODE, EncryptionUtil.keys.getPrivate());
			LoginResult loginProfile = ((InitialHandler) e.getPlayer().getPendingConnection()).getLoginProfile();
			if (loginProfile == null) {
				loginProfile = new LoginResult(e.getPlayer().getUniqueId().toString(), e.getPlayer().getName(), new Property[0]);
				Field lp = InitialHandler.class.getDeclaredField("loginProfile");
				lp.setAccessible(true);
				lp.set(e.getPlayer().getPendingConnection(), loginProfile);
			}
			List<Property> properties = Arrays.stream(loginProfile.getProperties()).collect(Collectors.toList());
			properties.removeIf(property -> property.getName().equals("sessionId"));
			byte[] sign = cipher.doFinal(("Signature: " + e.getTarget().getAddress().getPort() + ";" + Main.sessionId).getBytes(StandardCharsets.UTF_8));
			properties.add(new Property("sessionId", Integer.toString(Main.sessionId), Base64.getEncoder().encodeToString(sign)));
			Main.sessionId++;
			loginProfile.setProperties(properties.toArray(new Property[0]));
		} catch (BadPaddingException | NoSuchPaddingException | NoSuchAlgorithmException | InvalidKeyException | IllegalBlockSizeException | NoSuchFieldException | IllegalAccessException badPaddingException) {
			badPaddingException.printStackTrace();
		}
	}
}
