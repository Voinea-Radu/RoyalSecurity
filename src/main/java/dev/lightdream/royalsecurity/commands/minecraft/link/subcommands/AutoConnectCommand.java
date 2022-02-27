package dev.lightdream.royalsecurity.commands.minecraft.link.subcommands;

import dev.lightdream.commandmanager.commands.SubCommand;
import dev.lightdream.messagebuilder.MessageBuilder;
import dev.lightdream.royalsecurity.Main;
import dev.lightdream.royalsecurity.database.User;
import org.bukkit.command.CommandSender;

import java.util.HashMap;
import java.util.List;


@SuppressWarnings("unused")
@dev.lightdream.commandmanager.annotations.SubCommand(aliases = {"autoConnect"},
        onlyForPlayers = true,
        parent = AutoConnectCommand.class,
        command = "auto-connect")
public class AutoConnectCommand extends SubCommand {
    public AutoConnectCommand() {
        super(Main.instance);
    }

    @Override
    public void execute(CommandSender commandSender, List<String> list) {
        User user = Main.instance.databaseManager.getUser(commandSender);

        if (user == null) {
            return;
        }

        user.changeAutoConnect();

        commandSender.sendMessage(new MessageBuilder(Main.instance.lang.autoConnect).addPlaceholders(new HashMap<String, String>() {{
            put("status", String.valueOf(user.autoConnect));
        }}).parseString());
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, List<String> list) {
        return null;
    }
}
