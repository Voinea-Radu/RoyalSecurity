package dev.lightdream.royalsecurity.commands.minecraft.link.subcommands;

import dev.lightdream.commandmanager.commands.SubCommand;
import dev.lightdream.messagebuilder.MessageBuilder;
import dev.lightdream.royalsecurity.Main;
import dev.lightdream.royalsecurity.commands.minecraft.link.LinkCommand;
import dev.lightdream.royalsecurity.database.UserPair;
import org.bukkit.command.CommandSender;

import java.util.HashMap;
import java.util.List;

@SuppressWarnings("unused")
@dev.lightdream.commandmanager.annotations.SubCommand(usage = "[code]",
        parent = LinkCommand.class,
        command = "checkCode")
public class CheckCode extends SubCommand {
    public CheckCode() {
        super(Main.instance);
    }

    @Override
    public void execute(CommandSender commandSender, List<String> args) {
        if (args.size() == 0) {
            sendUsage(commandSender);
            return;
        }

        UserPair pair = Main.instance.databaseManager.getUserPair(args.get(0));

        if (pair == null) {
            commandSender.sendMessage(Main.instance.lang.invalidCode);
            return;
        }

        commandSender.sendMessage(new MessageBuilder(Main.instance.lang.codeDetails).addPlaceholders(new HashMap<String, String>() {{
            put("name", pair.getUser().name);
            put("discord_id", String.valueOf(pair.memberID));
        }}).parseString());
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, List<String> list) {
        return null;
    }
}
