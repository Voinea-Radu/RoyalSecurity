package dev.lightdream.royalsecurity.commands.minecraft.link.subcommands;

import dev.lightdream.api.IAPI;
import dev.lightdream.api.commands.SubCommand;
import dev.lightdream.api.databases.User;
import dev.lightdream.api.utils.MessageBuilder;
import dev.lightdream.royalsecurity.Main;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


@SuppressWarnings("unused")
@dev.lightdream.api.annotations.commands.SubCommand(aliases = {"autoConnect"},
        onlyForPlayers = true,
        parent = AutoConnectCommand.class,
        command = "auto-connect")
public class AutoConnectCommand extends SubCommand {
    public AutoConnectCommand(IAPI api) {
        super(api);
    }

    @Override
    public void execute(User u, List<String> list) {
        dev.lightdream.royalsecurity.database.User user = (dev.lightdream.royalsecurity.database.User) u;

        user.changeAutoConnect();

        new MessageBuilder(Main.instance.lang.autoConnect).addPlaceholders(new HashMap<String, String>() {{
            put("status", String.valueOf(user.autoConnect));
        }}).send(user);
    }

    @Override
    public List<String> onTabComplete(User user, List<String> list) {
        return new ArrayList<>();
    }
}
