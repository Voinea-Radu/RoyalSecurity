package dev.lightdream.royalsecurity.commands.discord;

import dev.lightdream.royalsecurity.Main;
import dev.lightdream.royalsecurity.commands.DiscordCommand;
import dev.lightdream.royalsecurity.dto.User;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageChannel;

import java.util.List;

public class LinkCommand extends DiscordCommand {
    public LinkCommand() {
        super("link", Main.instance.lang.linkCommandDescription, null, "[username]");
    }

    @Override
    public void execute(Member member, MessageChannel channel, List<String> args) {
        if (args.size() != 1) {
            sendUsage(channel);
            return;
        }

        User user = Main.instance.databaseManager.getUser(args.get(0));

        if (user == null) {
            sendMessage(channel, Main.instance.jdaConfig.invalidUser);
            return;
        }

        if (user.hasSecurity()) {
            sendMessage(channel, Main.instance.jdaConfig.alreadyLinked);
            return;
        }

        user.sendSecure(channel, Main.instance.securityManager.generateCode(user, member.getIdLong()), member.getIdLong());
        sendMessage(channel, Main.instance.jdaConfig.codeSent);
    }

    @Override
    public void execute(net.dv8tion.jda.api.entities.User user, MessageChannel channel, List<String> args) {

    }

    @Override
    public boolean isMemberSafe() {
        return false;
    }
}
