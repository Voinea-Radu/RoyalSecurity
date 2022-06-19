package dev.lightdream.ticketsystem.commands;

import dev.lightdream.jdaextension.commands.DiscordCommand;
import dev.lightdream.jdaextension.dto.CommandArgument;
import dev.lightdream.jdaextension.dto.context.GuildCommandContext;
import dev.lightdream.jdaextension.dto.context.PrivateCommandContext;
import dev.lightdream.ticketsystem.Main;
import dev.lightdream.ticketsystem.database.BlacklistRecord;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.interactions.commands.OptionType;

import java.util.Arrays;

public class BlackListCommand extends DiscordCommand {
    public BlackListCommand() {
        super(Main.instance, "blacklist", Main.instance.lang.blacklistCommandDescription, Permission.ADMINISTRATOR, true, Arrays.asList(
                new CommandArgument(OptionType.STRING, "action", Main.instance.lang.blackListSubCommandsDescription, true),
                new CommandArgument(OptionType.USER, "user", Main.instance.lang.userArgumentDescription, true)
        ));
    }

    @Override
    public void executeGuild(GuildCommandContext context) {
        String subcommand = context.getArgument("action").getAsString();
        User user;

        try {
            user = context.getArgument("user").getAsUser();
        } catch (Throwable t) {
            sendMessage(context, Main.instance.jdaConfig.invalidUser);
            return;
        }

        if (subcommand.equalsIgnoreCase("add")) {
            BlacklistRecord blacklistRecord = Main.instance.databaseManager.getBlacklist(user.getIdLong());
            if (blacklistRecord != null) {
                sendMessage(context, Main.instance.jdaConfig.alreadyBlacklisted);
                return;
            }
            new BlacklistRecord(user.getIdLong()).save();
            sendMessage(context, Main.instance.jdaConfig.blacklistAdded
                    .parse("user", user.getName()));
            return;
        }
        if (subcommand.equalsIgnoreCase("remove")) {
            BlacklistRecord blacklistRecord = Main.instance.databaseManager.getBlacklist(user.getIdLong());
            if (blacklistRecord == null) {
                sendMessage(context, Main.instance.jdaConfig.notBlacklisted);
                return;
            }
            blacklistRecord.delete();
            sendMessage(context, Main.instance.jdaConfig.blacklistRemoved
                    .parse("user", user.getName()));
            return;
        }
        if (subcommand.equalsIgnoreCase("check")) {
            BlacklistRecord blacklistRecord = Main.instance.databaseManager.getBlacklist(user.getIdLong());
            sendMessage(context, Main.instance.jdaConfig.blacklistInfo
                    .parse("user", user.getName())
                    .parse("status", blacklistRecord == null ? Main.instance.jdaConfig.blacklistStatusFalse : Main.instance.jdaConfig.blacklistStatusTrue)
            );

            return;
        }

        sendMessage(context, Main.instance.jdaConfig.invalidAction);
    }

    @Override
    public void executePrivate(PrivateCommandContext commandContext) {
        //Impossible
    }

    @Override
    public boolean isMemberSafe() {
        return false;
    }
}
