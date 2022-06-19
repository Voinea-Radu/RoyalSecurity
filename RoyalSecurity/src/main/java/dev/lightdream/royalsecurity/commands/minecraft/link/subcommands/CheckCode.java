package dev.lightdream.royalsecurity.commands.minecraft.link.subcommands;

import dev.lightdream.commandmanager.commands.Command;
import dev.lightdream.messagebuilder.MessageBuilder;
import dev.lightdream.royalsecurity.Main;
import dev.lightdream.royalsecurity.commands.minecraft.link.LinkCommand;
import dev.lightdream.royalsecurity.database.UserPair;
import org.bukkit.command.CommandSender;

import java.util.List;

@SuppressWarnings("unused")
@dev.lightdream.commandmanager.annotation.Command(
        usage = "[code]",
        parent = LinkCommand.class,
        aliases = {"checkCode"}
)
public class CheckCode extends Command {
    public CheckCode() {
        super(Main.instance);
    }

    @Override
    public void exec(CommandSender sender, List<String> args) {
        if (args.size() == 0) {
            sendUsage(sender);
            return;
        }

        UserPair pair = Main.instance.databaseManager.getUserPair(args.get(0));

        if (pair == null) {
            sender.sendMessage(Main.instance.lang.invalidCode);
            return;
        }

        sender.sendMessage(new MessageBuilder(Main.instance.lang.codeDetails)
                .parse("name", pair.getUser().name)
                .parse("discord_id", String.valueOf(pair.memberID))
                .parse()
        );
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, List<String> list) {
        return null;
    }
}
