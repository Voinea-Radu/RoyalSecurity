package me.lightdream.royalsecurity;

import org.apache.commons.lang.RandomStringUtils;

import java.util.HashMap;
import java.util.Optional;
import java.util.UUID;

public class API {
    private final RoyalSecurity plugin;

    public API(RoyalSecurity plugin) {
        this.plugin = plugin;
    }

    public SecurityUser getSecurityUser(Long userID) {
        Optional<SecurityUser> optionalUser = RoyalSecurity.instance.getDatabaseManager().getUserList().stream().filter(user -> user.getDiscordID().equals(userID)).findFirst();
        return optionalUser.orElse(null);
    }

    public SecurityUser getSecurityUser(UUID uuid) {
        Optional<SecurityUser> optionalUser = RoyalSecurity.instance.getDatabaseManager().getUserList().stream().filter(user -> user.getUUID().equals(uuid.toString())).findFirst();
        return optionalUser.orElse(null);
    }

    public String generateCode(UUID uuid, Long userID) {
        String code = RandomStringUtils.random(plugin.getMainConfig().codeDigits, true, true);
        if (getSecurityUser(uuid) != null) {
            return plugin.getMessages().minecraftAccountAlreadyRegistered;
        }
        if (getSecurityUser(userID) != null) {
            return plugin.getMessages().discordAccountAlreadyRegistered;
        }

        HashMap<UUID, Long> hashMap = new HashMap<>();
        hashMap.put(uuid, userID);
        plugin.getPendingCodes().put(code, hashMap);
        plugin.getMessageManager().sendLinkMessage(plugin.getDiscordListener().instance, userID, code);

        return "";
    }

    public void link(UUID uuid, Long userID) {
        link(uuid, userID, "0.0.0.0");
    }

    public String link(UUID uuid, Long userID, String ip) {
        if (getSecurityUser(uuid) != null) {
            return plugin.getMessages().minecraftAccountAlreadyRegistered;
        }
        if (getSecurityUser(userID) != null) {
            return plugin.getMessages().discordAccountAlreadyRegistered;
        }
        SecurityUser securityUser = new SecurityUser(uuid.toString(), userID, ip);

        plugin.getDatabaseManager().getUserList().add(securityUser);

        return "";
    }

    public String unlink(String name) {
        UUID uuid = Utils.getUUID(name);
        if (uuid == null) {
            return plugin.getMessages().nullUUID;
        }
        SecurityUser user = getSecurityUser(uuid);
        if (user == null) {
            return plugin.getMessages().minecraftAccountNotRegistered;
        }
        plugin.getDatabaseManager().getUserList().remove(user);
        return "";
    }

    public String unlink(UUID uuid) {
        SecurityUser user = getSecurityUser(uuid);
        if (user == null) {
            return plugin.getMessages().minecraftAccountNotRegistered;
        }
        plugin.getDatabaseManager().getUserList().remove(user);
        return "";
    }

    public String unlink(Long userID) {
        SecurityUser user = getSecurityUser(userID);
        if (user == null) {
            return plugin.getMessages().discordAccountNotRegistered;
        }
        plugin.getDatabaseManager().getUserList().remove(user);
        return "";
    }


}
