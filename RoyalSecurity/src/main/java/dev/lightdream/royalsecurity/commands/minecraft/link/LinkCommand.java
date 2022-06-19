package dev.lightdream.royalsecurity.commands.minecraft.link;

import dev.lightdream.commandmanager.commands.Command;
import dev.lightdream.royalsecurity.Main;
import dev.lightdream.royalsecurity.database.User;
import dev.lightdream.royalsecurity.database.UserPair;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;

@dev.lightdream.commandmanager.annotation.Command(
        aliases = {"link"},
        onlyForPlayers = true
)
public class LinkCommand extends Command {
    public LinkCommand() {
        super(Main.instance);
    }

    @Override
    public void exec(@NotNull Player player, List<String> args) {
        if (args.size() != 1) {
            sendUsage(player);
            return;
        }

        User user = Main.instance.databaseManager.getUser(player);
        UserPair pair = Main.instance.databaseManager.getUserPair(args.get(0));

        if (pair == null) {
            player.sendMessage(Main.instance.lang.invalidCode);
            return;
        }

        if (!pair.getUser().equals(user)) {
            player.sendMessage(Main.instance.lang.invalidCode);
            return;
        }

        pair.pair(player.getAddress().getHostName());
        player.sendMessage(Main.instance.lang.linked);
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, List<String> list) {
        return null;
    }
}
