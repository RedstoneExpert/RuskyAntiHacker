package me.redstoneexpert.ruskyantihacker.spigot.Events;

import me.redstoneexpert.ruskyantihacker.nmsabstract.Property;
import me.redstoneexpert.ruskyantihacker.spigot.EncryptionUtils;
import me.redstoneexpert.ruskyantihacker.spigot.Main;
import me.redstoneexpert.ruskyantihacker.spigot.TCPUtils;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.security.GeneralSecurityException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Base64;

public class onLogin implements Listener {

	private boolean checkSignature(Property property) throws NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException {
		return new String(EncryptionUtils.decrypt(Base64.getDecoder().decode(property.sig()))).equals("Signature: " + Bukkit.getServer().getPort() + ";" + property.val());
	}

	private void kick(PlayerLoginEvent e, int i, Property p) throws GeneralSecurityException {
		byte[] newKey = TCPUtils.getKey();
		if (!Arrays.equals(Main.key.getEncoded(), newKey)) {
			Main.key = EncryptionUtils.getPubKey(newKey);
			try {
				if (checkSignature(p)) {
					Main.lastId = i;
					e.setResult(PlayerLoginEvent.Result.ALLOWED);
					return;
				}
			} catch (BadPaddingException ignored) {}
		}
		e.setKickMessage("Bad id or signature");
	}

	@EventHandler
	public void event(PlayerLoginEvent e) {
		if (e.getResult() == PlayerLoginEvent.Result.ALLOWED) {
			e.setResult(PlayerLoginEvent.Result.KICK_OTHER);
			Property id = Main.nmsUtils.getSignature(e.getPlayer());
			if (id != null) {
				try {
					int val = Integer.parseInt(id.val());
					try {
						if (val > Main.lastId) {
							if (checkSignature(id)) {
								Main.lastId = val;
								e.setResult(PlayerLoginEvent.Result.ALLOWED);
							} else kick(e, val, id);
						} else {
							kick(e, val, id);
						}
					} catch (BadPaddingException ignored) {
						kick(e, val, id);
					} catch (GeneralSecurityException ex) {
						e.setKickMessage("RSA error");
						ex.printStackTrace();
					}
				} catch (NumberFormatException | GeneralSecurityException ignored) {
					e.setKickMessage("Bad id format");
				}
			} else {
				e.setKickMessage("Missing signature");
			}
		}
	}
}
