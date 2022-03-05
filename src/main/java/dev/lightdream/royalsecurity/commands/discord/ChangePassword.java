package dev.lightdream.royalsecurity.commands.discord;

import dev.lightdream.jdaextension.commands.DiscordCommand;
import dev.lightdream.jdaextension.dto.CommandArgument;
import dev.lightdream.jdaextension.dto.context.CommandContext;
import dev.lightdream.jdaextension.dto.context.GuildCommandContext;
import dev.lightdream.jdaextension.dto.context.PrivateCommandContext;
import dev.lightdream.royalsecurity.Main;
import dev.lightdream.royalsecurity.database.User;
import fr.xephi.authme.api.v3.AuthMeApi;
import net.dv8tion.jda.api.interactions.commands.OptionType;

import java.util.Arrays;
import java.util.List;

public class ChangePassword extends DiscordCommand {
    public ChangePassword() {
        super(Main.instance, "changePassword", Main.instance.lang.changePasswordDescription, null, true, Arrays.asList(
                new CommandArgument(OptionType.STRING, "account", Main.instance.lang.accountArgDescription, false),
                new CommandArgument(OptionType.STRING, "password", Main.instance.lang.passwordArgDescription, false)
        ));
    }

    @Override
    public void executeGuild(GuildCommandContext context) {
        sendMessage(context, Main.instance.jdaConfig.dmsCommand);
    }

    @Override
    public void executePrivate(PrivateCommandContext context) {
        String password = context.getArgument("password").getAsString();
        String account = context.getArgument("account").getAsString();

        if (account.equals("")) {
            List<User> users = Main.instance.databaseManager.getUser(context.getUser().getIdLong());

            if (users.size() == 0) {
                sendMessage(context, Main.instance.jdaConfig.notLinked);
                return;
            }

            if (users.size() == 1) {
                changePassword(context, users.get(0), password);
                return;
            }

            sendMessage(context, Main.instance.jdaConfig.multipleLinked);
            AccountsCommand.staticExecute(context, context.getUser().getIdLong(), privateResponse);
            return;
        }

        User user = Main.instance.databaseManager.getUser(account);

        if (user == null) {
            sendMessage(context, Main.instance.jdaConfig.invalidUser);
            return;
        }

        if (!user.discordID.equals(context.getUser().getIdLong())) {
            sendMessage(context, Main.instance.jdaConfig.notOwner);
            return;
        }

        changePassword(context, user, password);
    }

    @Override
    public boolean isMemberSafe() {
        return true;
    }

    private void changePassword(CommandContext context, User user, String newPassword) {
        AuthMeApi.getInstance().changePassword(user.name, newPassword);
        sendMessage(context, Main.instance.jdaConfig.passwordChanged);
    }
}
