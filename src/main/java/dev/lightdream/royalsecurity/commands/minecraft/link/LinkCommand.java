package dev.lightdream.royalsecurity.commands.minecraft.link;

import dev.lightdream.commandmanager.annotations.Command;
import dev.lightdream.commandmanager.commands.BaseCommand;
import dev.lightdream.royalsecurity.Main;
import dev.lightdream.royalsecurity.database.User;
import dev.lightdream.royalsecurity.database.UserPair;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

@Command(
        command = "link"
)
public class LinkCommand extends BaseCommand {
    public LinkCommand() {
        super(Main.instance);
    }

    @Override
    public void execute(CommandSender commandSender, List<String> args) {
        if (args.size() != 1) {
            sendUsage(commandSender);
            return;
        }

        User user = Main.instance.databaseManager.getUser(commandSender);
        UserPair pair = Main.instance.databaseManager.getUserPair(args.get(0));
        Player player = (Player) commandSender;

        if (pair == null) {
            commandSender.sendMessage(Main.instance.lang.invalidCode);
            return;
        }

        if (!pair.getUser().equals(user)) {
            commandSender.sendMessage(Main.instance.lang.invalidCode);
            return;
        }

        pair.pair(player.getAddress().getHostName());
        commandSender.sendMessage(Main.instance.lang.linked);
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, List<String> list) {
        return null;
    }
}
