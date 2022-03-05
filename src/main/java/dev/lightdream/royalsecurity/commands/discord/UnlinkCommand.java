package dev.lightdream.royalsecurity.commands.discord;

import dev.lightdream.jdaextension.commands.DiscordCommand;
import dev.lightdream.jdaextension.dto.CommandArgument;
import dev.lightdream.jdaextension.dto.context.CommandContext;
import dev.lightdream.jdaextension.dto.context.GuildCommandContext;
import dev.lightdream.jdaextension.dto.context.PrivateCommandContext;
import dev.lightdream.royalsecurity.Main;
import dev.lightdream.royalsecurity.database.User;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.interactions.commands.OptionType;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class UnlinkCommand extends DiscordCommand {
    @SuppressWarnings("ArraysAsListWithZeroOrOneArgument")
    public UnlinkCommand() {
        super(Main.instance, "unlink", Main.instance.lang.unlinkCommandDescription, null, true, Arrays.asList(
                new CommandArgument(OptionType.STRING, "username", Main.instance.lang.usernameArgDescription, false)
        ));
    }


    @Override
    public void executeGuild(GuildCommandContext context) {
        String account = context.getArgument("account").getAsString();

        if (Objects.equals(account, "")) {
            List<User> users = Main.instance.databaseManager.getUser(context.getMember().getIdLong());

            if (users.size() == 0) {
                sendMessage(context, Main.instance.jdaConfig.notLinked);
                return;
            }

            if (users.size() == 1) {
                unlink(context, users.get(0));
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

        if (!context.getMember().hasPermission(Permission.ADMINISTRATOR)) {
            if (!user.discordID.equals(context.getMember().getIdLong())) {
                sendMessage(context, Main.instance.jdaConfig.notOwner);
                return;
            }
        }

        unlink(context, user);
    }

    @Override
    public void executePrivate(PrivateCommandContext context) {
        String account = context.getArgument("account").getAsString();

        if (Objects.equals(account, "")) {
            List<User> users = Main.instance.databaseManager.getUser(context.getUser().getIdLong());

            if (users.size() == 0) {
                sendMessage(context, Main.instance.jdaConfig.notLinked);
                return;
            }

            if (users.size() == 1) {
                unlink(context, users.get(0));
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

        unlink(context, user);
    }

    @Override
    public boolean isMemberSafe() {
        return true;
    }

    private void unlink(CommandContext context, User user) {
        user.unlink();
        sendMessage(context, Main.instance.jdaConfig.unlinked);
    }
}
