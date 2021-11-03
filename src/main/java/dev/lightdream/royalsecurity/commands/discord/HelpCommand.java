package dev.lightdream.royalsecurity.commands.discord;

import dev.lightdream.royalsecurity.Main;
import dev.lightdream.royalsecurity.commands.DiscordCommand;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.User;

import java.util.List;

public class HelpCommand extends DiscordCommand {
    public HelpCommand() {
        super("help", Main.instance.lang.helpCommandDescription, null, "");
    }

    @Override
    public void execute(Member user, MessageChannel channel, List<String> args) {
        execute(user.getUser(), channel, args);
    }

    @Override
    public void execute(User user, MessageChannel channel, List<String> args) {
        Main.instance.discordCommandManager.sendHelp(channel);
    }

    @Override
    public boolean isMemberSafe() {
        return true;
    }
}
