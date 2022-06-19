package dev.lightdream.royalsecurity.commands.minecraft.link.subcommands;

import dev.lightdream.commandmanager.commands.Command;
import dev.lightdream.messagebuilder.MessageBuilder;
import dev.lightdream.royalsecurity.Main;
import dev.lightdream.royalsecurity.commands.minecraft.link.LinkCommand;
import dev.lightdream.royalsecurity.database.User;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.List;


@SuppressWarnings("unused")
@dev.lightdream.commandmanager.annotation.Command(
        aliases = {"autoConnect", "auto-connect"},
        onlyForPlayers = true,
        parent = LinkCommand.class
)
public class AutoConnectCommand extends Command {
    public AutoConnectCommand() {
        super(Main.instance);
    }


    @Override
    public void exec(@NotNull Player player, @NotNull List<String> args) {
        User user = Main.instance.databaseManager.getUser(player);

        user.changeAutoConnect();

        player.sendMessage(new MessageBuilder(Main.instance.lang.autoConnect)
                .parse("status", String.valueOf(user.autoConnect))
                .parse());
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, List<String> list) {
        return null;
    }
}
