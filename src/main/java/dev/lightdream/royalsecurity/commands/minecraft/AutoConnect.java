package dev.lightdream.royalsecurity.commands.minecraft;

import dev.lightdream.api.commands.SubCommand;
import dev.lightdream.api.databases.User;
import dev.lightdream.api.utils.MessageBuilder;
import dev.lightdream.royalsecurity.Main;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


@dev.lightdream.api.annotations.commands.SubCommand(
        aliases = {"autoConnect", "auto-connect"},
        onlyForPlayers = true,
        parentCommand = "link")
public class AutoConnect extends SubCommand {
    public AutoConnect() {
        super(Main.instance);
    }

    @Override
    public void execute(User u, List<String> list) {
        dev.lightdream.royalsecurity.database.User user = (dev.lightdream.royalsecurity.database.User) u;

        user.autoConnect();

        user.sendMessage(Main.instance, new MessageBuilder(Main.instance.lang.autoConnect).addPlaceholders(new HashMap<String, String>() {{
            put("status", String.valueOf(user.autoConnect));
        }}));
    }

    @Override
    public List<String> onTabComplete(User user, List<String> list) {
        return new ArrayList<>();
    }
}
