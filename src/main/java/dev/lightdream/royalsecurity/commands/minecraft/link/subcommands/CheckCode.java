package dev.lightdream.royalsecurity.commands.minecraft.link.subcommands;

import dev.lightdream.api.IAPI;
import dev.lightdream.api.commands.SubCommand;
import dev.lightdream.api.databases.User;
import dev.lightdream.api.utils.MessageBuilder;
import dev.lightdream.royalsecurity.Main;
import dev.lightdream.royalsecurity.commands.minecraft.link.LinkCommand;
import dev.lightdream.royalsecurity.database.UserPair;

import java.util.HashMap;
import java.util.List;

@SuppressWarnings("unused")
@dev.lightdream.api.annotations.commands.SubCommand(usage = "[code]",
        parent = LinkCommand.class,
        command = "checkCode")
public class CheckCode extends SubCommand {
    public CheckCode(IAPI api) {
        super(api);
    }

    @Override
    public void execute(User user, List<String> args) {
        if (args.size() == 0) {
            sendUsage(user);
            return;
        }

        UserPair pair = Main.instance.databaseManager.getUserPair(args.get(0));

        if (pair == null) {
            user.sendMessage(Main.instance.lang.invalidCode);
            return;
        }

        new MessageBuilder(Main.instance.lang.codeDetails).addPlaceholders(new HashMap<String, String>() {{
            put("name", pair.getUser().name);
            put("discord_id", String.valueOf(pair.memberID));
        }}).send(user);
    }

    @Override
    public List<String> onTabComplete(User user, List<String> list) {
        return null;
    }
}
