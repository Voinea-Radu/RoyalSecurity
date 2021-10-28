package dev.lightdream.royalsecurity.commands.discord;

import dev.lightdream.royalsecurity.Main;
import dev.lightdream.royalsecurity.commands.DiscordCommand;
import dev.lightdream.royalsecurity.dto.User;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageChannel;

import java.util.List;

public class UnlinkCommand extends DiscordCommand {
    public UnlinkCommand() {
        super("unlink", Main.instance.lang.unlinkCommandDescription, null, "[username]");
    }

    @Override
    public void execute(Member member, MessageChannel channel, List<String> args) {

        if (args.size() == 0) {
            List<User> users = Main.instance.databaseManager.getUser(member.getIdLong());

            if (users.size() == 0) {
                sendMessage(channel, Main.instance.jdaConfig.notLinked);
                return;
            }

            if (users.size() == 1) {
                unlink(channel, users.get(0));
                return;
            }

            sendMessage(channel, Main.instance.jdaConfig.multipleLinked);
            new AccountsCommand().execute(member, channel, args);
            return;
        }

        User user = Main.instance.databaseManager.getUser(args.get(0));

        if (user == null) {
            sendMessage(channel, Main.instance.jdaConfig.invalidUser);
            return;
        }

        if (!member.hasPermission(Permission.ADMINISTRATOR)) {
            if (!user.discordID.equals(member.getIdLong())) {
                sendMessage(channel, Main.instance.jdaConfig.notOwner);
                return;
            }
        }

        unlink(channel, user);
    }

    @Override
    public void execute(net.dv8tion.jda.api.entities.User u, MessageChannel channel, List<String> args) {
        if (args.size() == 0) {
            List<User> users = Main.instance.databaseManager.getUser(u.getIdLong());

            if (users.size() == 0) {
                sendMessage(channel, Main.instance.jdaConfig.notLinked);
                return;
            }

            if (users.size() == 1) {
                unlink(channel, users.get(0));
                return;
            }

            sendMessage(channel, Main.instance.jdaConfig.multipleLinked);
            new AccountsCommand().execute(u, channel, args);
            return;
        }

        User user = Main.instance.databaseManager.getUser(args.get(0));

        if (user == null) {
            sendMessage(channel, Main.instance.jdaConfig.invalidUser);
            return;
        }

        if (!user.discordID.equals(u.getIdLong())) {
            sendMessage(channel, Main.instance.jdaConfig.notOwner);
            return;
        }

        unlink(channel, user);
    }

    @Override
    public boolean isMemberSafe() {
        return true;
    }

    private void unlink(MessageChannel channel, User user) {
        user.unlink();
        sendMessage(channel, Main.instance.jdaConfig.unlinked);
    }
}
