package dev.lightdream.royalsecurity.managers;

import dev.lightdream.royalsecurity.dto.User;
import dev.lightdream.royalsecurity.dto.UserPair;
import dev.lightdream.royalsecurity.Main;
import net.dv8tion.jda.api.entities.Member;
import org.apache.commons.lang.RandomStringUtils;

import java.util.HashMap;

public class SecurityManager {

    private final Main plugin;
    public HashMap<String, UserPair> linkCodes = new HashMap<>();

    public SecurityManager(Main plugin) {
        this.plugin = plugin;
    }

    public String generateCode(User user, Member member) {
        String code = RandomStringUtils.random(plugin.config.codeDigits, true, true);

        linkCodes.put(code, new UserPair(user, member));
        return code;
    }

}
