package dev.lightdream.royalsecurity.commands.discord;

import dev.lightdream.royalsecurity.Main;
import dev.lightdream.royalsecurity.commands.DiscordCommand;
import dev.lightdream.royalsecurity.database.User;
import fr.xephi.authme.api.v3.AuthMeApi;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.TextChannel;

import java.util.List;

public class ChangePassword extends DiscordCommand {
    public ChangePassword() {
        super("changePassword", Main.instance.lang.changePasswordDescription, null, "<account> [new password] - IN DMs");
    }

    @Override
    public void execute(Member member, TextChannel channel, List<String> args) {
        sendMessage(channel, Main.instance.jdaConfig.dmsCommand);
    }

    @Override
    public void execute(net.dv8tion.jda.api.entities.User u, MessageChannel channel, List<String> args) {
        if (args.size() == 0) {
            sendUsage(channel);
            return;
        }

        if (args.size() == 1) {
            List<User> users = Main.instance.databaseManager.getUser(u.getIdLong());

            if (users.size() == 0) {
                sendMessage(channel, Main.instance.jdaConfig.notLinked);
                return;
            }

            if (users.size() == 1) {
                changePassword(channel, users.get(0), args.get(0));
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

        changePassword(channel, user, args.get(1));
    }

    @Override
    public boolean isMemberSafe() {
        return true;
    }

    private void changePassword(MessageChannel channel, User user, String newPassword) {
        AuthMeApi.getInstance().changePassword(user.name, newPassword);
        sendMessage(channel, Main.instance.jdaConfig.passwordChanged);
    }
}
