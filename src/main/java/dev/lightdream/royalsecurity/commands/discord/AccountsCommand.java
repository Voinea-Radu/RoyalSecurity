package dev.lightdream.royalsecurity.commands.discord;

import dev.lightdream.api.dto.jda.JdaEmbed;
import dev.lightdream.api.dto.jda.JdaField;
import dev.lightdream.royalsecurity.Main;
import dev.lightdream.royalsecurity.commands.DiscordCommand;
import dev.lightdream.royalsecurity.dto.User;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageChannel;

import java.util.List;

public class AccountsCommand extends DiscordCommand {
    public AccountsCommand() {
        super("accounts", Main.instance.lang.accountsDescription, null, "");
    }

    @Override
    public void execute(Member member, MessageChannel channel, List<String> args) {
        execute(member.getUser(), channel, args);
    }

    @Override
    public void execute(net.dv8tion.jda.api.entities.User user, MessageChannel channel, List<String> args) {
        List<User> users = Main.instance.databaseManager.getUser(user.getIdLong());

        JdaEmbed embed = Main.instance.jdaConfig.accounts.clone();
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
