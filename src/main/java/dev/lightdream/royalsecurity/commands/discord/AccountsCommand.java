package dev.lightdream.royalsecurity.commands.discord;

import dev.lightdream.jdaextension.commands.DiscordCommand;
import dev.lightdream.jdaextension.dto.CommandArgument;
import dev.lightdream.jdaextension.dto.JdaEmbed;
import dev.lightdream.jdaextension.dto.JdaField;
import dev.lightdream.jdaextension.dto.context.CommandContext;
import dev.lightdream.jdaextension.dto.context.GuildCommandContext;
import dev.lightdream.jdaextension.dto.context.PrivateCommandContext;
import dev.lightdream.royalsecurity.Main;
import dev.lightdream.royalsecurity.database.User;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.interactions.commands.OptionType;

import java.util.Arrays;
import java.util.List;

public class AccountsCommand extends DiscordCommand {
    @SuppressWarnings("ArraysAsListWithZeroOrOneArgument")
    public AccountsCommand() {
        super(Main.instance, "accounts", Main.instance.lang.accountsDescription, null, true, Arrays.asList(
                new CommandArgument(OptionType.STRING, "user_id", Main.instance.lang.userIDArgDescription, false)
        ));
    }

    public static void sendAccounts(CommandContext context, List<User> users, net.dv8tion.jda.api.entities.User user, boolean privateResponse) {
        JdaEmbed embed = Main.instance.jdaConfig.accounts.clone().parse("user", user.getName());

        JdaField field = embed.fields.get(0);
        String s = field.content;
        field.content = "";

        users.forEach(u -> field.content += s.replace("%player_name%", u.name));
        embed.fields.set(0, field);

        sendMessage(context, embed, privateResponse);
    }

    public static void staticExecute(CommandContext context, Long id, boolean privateResponse) {
        List<User> users = Main.instance.databaseManager.getUser(id);
        Main.instance.bot.retrieveUserById(id).queue(user -> {
            if (user == null) {
                sendMessage(context, Main.instance.jdaConfig.invalidUser, privateResponse);
                return;
            }
            sendAccounts(context, users, user, privateResponse);
        });
    }

    @Override
    public void executeGuild(GuildCommandContext context) {
        if (context.getArgument("user_id") == null) {
            executePrivate(context.toPrivate());
            return;
        }

        if (!context.getMember().hasPermission(Permission.ADMINISTRATOR)) {
            sendMessage(context, Main.instance.jdaConfig.notAllowed);
            return;
        }

        long id;
        try {
            id = Long.parseLong(context.getArgument("user_id").getAsString());
        } catch (NumberFormatException e) {
            sendMessage(context, Main.instance.jdaConfig.invalidNumber);
            return;
        }
        staticExecute(context, id, privateResponse);
    }

    @Override
    public void executePrivate(PrivateCommandContext context) {
        List<User> users = Main.instance.databaseManager.getUser(context.getUser().getIdLong());
        sendAccounts(context, users, context.getUser(), privateResponse);
    }

    @Override
    public boolean isMemberSafe() {
        return true;
    }
}
