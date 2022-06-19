package dev.lightdream.ticketsystem.commands;

import dev.lightdream.jdaextension.commands.DiscordCommand;
import dev.lightdream.jdaextension.dto.CommandArgument;
import dev.lightdream.jdaextension.dto.context.GuildCommandContext;
import dev.lightdream.jdaextension.dto.context.PrivateCommandContext;
import dev.lightdream.ticketsystem.Main;
import dev.lightdream.ticketsystem.database.BanRecord;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.interactions.commands.OptionType;

import java.util.Collections;

public class CheckBanCommand extends DiscordCommand {
    public CheckBanCommand() {
        super(Main.instance, "checkBan", Main.instance.lang.checkBanCommandDescription, Permission.BAN_MEMBERS, true, Collections.singletonList(
                new CommandArgument(OptionType.USER, "user", Main.instance.lang.banIDDescription, true)
        ));
    }

    @Override
    public void executeGuild(GuildCommandContext context) {
        User user = context.getArgument("user").getAsUser();

        BanRecord ban = Main.instance.databaseManager.getBan(user.getIdLong());
        if (ban == null) {
            sendMessage(context, Main.instance.jdaConfig.notBanned);
            return;
        }

        ban.sendBanDetails(context, privateResponse);
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
