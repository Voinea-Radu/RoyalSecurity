package dev.lightdream.royalsecurity.managers;

import dev.lightdream.royalsecurity.Main;
import dev.lightdream.royalsecurity.database.User;
import dev.lightdream.royalsecurity.database.UserPair;
import org.apache.commons.lang.RandomStringUtils;

public class SecurityManager {

    private final Main plugin;

    public SecurityManager(Main plugin) {
        this.plugin = plugin;
    }

    public String generateCode(User user, Long id) {
        String code = RandomStringUtils.random(plugin.config.codeDigits, true, true);

        if (Main.instance.databaseManager.getUserPair(code) != null) {
            return generateCode(user, id);
        }

        new UserPair(code, user, id);
        return code;
    }

}
