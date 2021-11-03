package dev.lightdream.royalsecurity.commands.discord;

import dev.lightdream.api.dto.jda.JdaEmbed;
import dev.lightdream.api.dto.jda.JdaField;
import dev.lightdream.royalsecurity.Main;
import dev.lightdream.royalsecurity.commands.DiscordCommand;
import dev.lightdream.royalsecurity.dto.User;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageChannel;

import java.util.List;

public class AccountsCommand extends DiscordCommand {
    public AccountsCommand() {
        super("accounts", Main.instance.lang.accountsDescription, null, "");
    }

    @Override
    public void execute(Member member, MessageChannel channel, List<String> args) {
        if (args.size() != 0) {
            if (member.hasPermission(Permission.ADMINISTRATOR)) {
                long id;
                try {
                    id = Long.parseLong(args.get(0));
                } catch (NumberFormatException e) {
                    sendMessage(channel, Main.instance.jdaConfig.invalidNumber);
                    return;
                }
                List<User> users = Main.instance.databaseManager.getUser(id);
                Main.instance.bot.retrieveUserById(id).queue(user -> {
                    if (user == null) {
                        sendAccounts(users, channel, String.valueOf(id));
                        return;
                    }
                    sendAccounts(users, channel, user.getAsTag());
                });
                return;
            }
            sendMessage(channel, Main.instance.jdaConfig.notAllowed);
            return;
        }
        execute(member.getUser(), channel, args);
    }

    @Override
    public void execute(net.dv8tion.jda.api.entities.User user, MessageChannel channel, List<String> args) {
        List<User> users = Main.instance.databaseManager.getUser(user.getIdLong());
        sendAccounts(users, channel, user.getName());
    }

    public void sendAccounts(List<User> users, MessageChannel channel, String userName) {
        JdaEmbed embed = Main.instance.jdaConfig.accounts.clone()
                .parse("user", userName);

        JdaField field = embed.fields.get(0);
        String s = field.content;
        field.content = "";

        users.forEach(u -> field.content += s.replace("%player_name%", u.name));
        embed.fields.set(0, field);

        sendMessage(channel, embed);
    }

    @Override
    public boolean isMemberSafe() {
        return true;
    }
}
