package dev.lightdream.ticketsystem.commands;

import dev.lightdream.jdaextension.commands.DiscordCommand;
import dev.lightdream.jdaextension.dto.CommandArgument;
import dev.lightdream.jdaextension.dto.JDAEmbed;
import dev.lightdream.jdaextension.dto.context.GuildCommandContext;
import dev.lightdream.jdaextension.dto.context.PrivateCommandContext;
import dev.lightdream.ticketsystem.Main;
import dev.lightdream.ticketsystem.database.BanRecord;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.interactions.commands.OptionType;

import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class HistoryCommand extends DiscordCommand {
    public HistoryCommand() {
        super(Main.instance, "history", Main.instance.lang.historyCommandDescription, Permission.BAN_MEMBERS, true, Collections.singletonList(
                new CommandArgument(OptionType.USER, "user", Main.instance.lang.userIDDescription, true)
        ));
    }

    @Override
    public void executeGuild(GuildCommandContext context) {
        User user = context.getArgument("user").getAsUser();

        List<BanRecord> bans = Main.instance.databaseManager.getPastBans(user.getIdLong());
        JDAEmbed embed = Main.instance.jdaConfig.bans.clone();
        bans.forEach(ban -> {
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
            Date date = new Date(ban.timestamp);

            embed.description += Main.instance.jdaConfig.bansEntry
                    .replace("%id%", String.valueOf(ban.id))
                    .replace("%reason%", ban.reason)
                    .replace("%date%", dateFormat.format(date));
            embed.description += "\n";
        });

        sendMessage(context, embed
                .parse("name", user.getName()));

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
