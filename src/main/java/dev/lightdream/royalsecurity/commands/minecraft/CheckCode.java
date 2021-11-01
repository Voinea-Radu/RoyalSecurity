package dev.lightdream.royalsecurity.commands.minecraft;

import dev.lightdream.api.commands.SubCommand;
import dev.lightdream.api.databases.User;
import dev.lightdream.api.utils.MessageBuilder;
import dev.lightdream.royalsecurity.Main;
import dev.lightdream.royalsecurity.dto.UserPair;

import java.util.HashMap;
import java.util.List;

public class CheckCode extends SubCommand {
    public CheckCode() {
        super(Main.instance, "checkCode", false, false, "[code]");
    }

    @Override
    public void execute(User user, List<String> args) {
        if (args.size() == 0) {
            sendUsage(user);
            return;
        }

        UserPair pair = Main.instance.databaseManager.getUserPair(args.get(0));

        if (pair == null) {
            user.sendMessage(api, Main.instance.lang.invalidCode);
            return;
        }

        System.out.println(api);
        System.out.println(Main.instance);
        System.out.println(Main.instance.lang);
        System.out.println(Main.instance.lang.codeDetails);
        System.out.println(pair);
        System.out.println(pair.user);
        System.out.println(pair.user.name);
        System.out.println(pair.memberID);

        user.sendMessage(api, new MessageBuilder(Main.instance.lang.codeDetails).addPlaceholders(new HashMap<String, String>() {{
            put("name", pair.user.name);
            put("discord_id", String.valueOf(pair.memberID));
        }}));
    }

    @Override
    public List<String> onTabComplete(User user, List<String> list) {
        return null;
    }
}
