package dev.lightdream.ticketsystem.commands;

import dev.lightdream.jdaextension.commands.DiscordCommand;
import dev.lightdream.jdaextension.dto.CommandArgument;
import dev.lightdream.jdaextension.dto.JDAEmbed;
import dev.lightdream.jdaextension.dto.context.GuildCommandContext;
import dev.lightdream.jdaextension.dto.context.PrivateCommandContext;
import dev.lightdream.ticketsystem.Main;
import dev.lightdream.ticketsystem.database.Ticket;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.interactions.commands.OptionType;

import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class TicketsCommand extends DiscordCommand {
    public TicketsCommand() {
        super(Main.instance, "tickets", Main.instance.lang.ticketsCommandDescription, Permission.ADMINISTRATOR, true, Collections.singletonList(
                new CommandArgument(OptionType.USER, "user", Main.instance.lang.userIDDescription, true)
        ));
    }

    @Override
    public void executeGuild(GuildCommandContext context) {
        User user = context.getArgument("user").getAsUser();

        List<Ticket> tickets = Main.instance.databaseManager.getPastTickets(user.getIdLong());
        JDAEmbed embed = Main.instance.jdaConfig.tickets.clone();
        tickets.forEach(ticket -> {
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
            Date date = new Date(ticket.timestamp);

            embed.description += Main.instance.jdaConfig.ticketsEntry
                    .replace("%id%", String.valueOf(ticket.id))
                    .replace("%type%", ticket.type)
                    .replace("%date%", dateFormat.format(date));
            embed.description += "\n";
        });

        sendMessage(context, embed
                .parse("name", user.getName()));


    }

    @Override
    public void executePrivate(PrivateCommandContext commandContext) {

    }

    @Override
    public boolean isMemberSafe() {
        return false;
    }
}
