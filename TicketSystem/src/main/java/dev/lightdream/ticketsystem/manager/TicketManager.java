package dev.lightdream.ticketsystem.manager;

import dev.lightdream.jdaextension.dto.JDAEmbed;
import dev.lightdream.lambda.LambdaExecutor;
import dev.lightdream.logger.Debugger;
import dev.lightdream.ticketsystem.Main;
import dev.lightdream.ticketsystem.database.BlacklistRecord;
import dev.lightdream.ticketsystem.database.Ticket;
import dev.lightdream.ticketsystem.dto.TicketType;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.exceptions.ErrorHandler;
import net.dv8tion.jda.api.requests.ErrorResponse;
import org.jetbrains.annotations.NotNull;

public class TicketManager {

    public static JDAEmbed closeTicket(TextChannel textChannel, @NotNull User user) {
        Ticket ticket = Main.instance.databaseManager.getTicket(textChannel.getIdLong());

        if (ticket == null) {
            return Main.instance.jdaConfig.notTicket;
        }

        return _closeTicket(textChannel, ticket, user);
    }

    private static JDAEmbed _closeTicket(TextChannel textChannel, @NotNull Ticket ticket, @NotNull User user) {
        new java.util.Timer().schedule(new java.util.TimerTask() {
            @Override
            public void run() {
                ticket.getTranscript().record(user, "Closed Ticket");
                ticket.close();
                textChannel.delete().queue(null, new ErrorHandler().handle(
                        ErrorResponse.UNKNOWN_CHANNEL,
                        e -> {
                            //empty
                        }
                ));
            }
        }, 5000);
        return Main.instance.jdaConfig.closingTicket;
    }


    public static void createTicket(Guild guild, Member member, TicketType ticketType,
                                    LambdaExecutor.NoReturnLambdaExecutor<TextChannel> executeOnChannel,
                                    LambdaExecutor.NoReturnLambdaExecutor<JDAEmbed> replyExecutor) {
        Debugger.info("Creating ticket");
        if (guild == null) {
            return;
        }

        Category category = guild.getCategoryById(ticketType.categoryID);

        if (category == null || member == null) {
            return;
        }

        BlacklistRecord blacklistRecord = Main.instance.databaseManager.getBlacklist(member.getIdLong());

        if (blacklistRecord != null) {
            replyExecutor.execute(Main.instance.jdaConfig.blacklisted);
            return;
        }

        for (TextChannel channel : category.getTextChannels()) {
            Ticket ticket = Main.instance.databaseManager.getTicket(channel.getIdLong());

            if (ticket == null) {
                continue;
            }

            if (!ticket.creatorID.equals(member.getIdLong())) {
                continue;
            }

            channel.putPermissionOverride(member).setAllow(
                    Permission.MESSAGE_SEND, Permission.MESSAGE_HISTORY,
                    Permission.MESSAGE_ATTACH_FILES, Permission.VIEW_CHANNEL
            ).queue();

            channel.sendMessage("<@" + member.getId() + ">")
                    .queue(message -> message.delete().queue());

            replyExecutor.execute(Main.instance.jdaConfig.alreadyHaveTicket);
            return;
        }

        guild.createTextChannel(member.getEffectiveName(), category).queue(textChannel -> {
            textChannel.putPermissionOverride(member).setAllow(
                    Permission.MESSAGE_SEND, Permission.MESSAGE_HISTORY,
                    Permission.MESSAGE_ATTACH_FILES, Permission.VIEW_CHANNEL
            ).queue();

            ticketType.associatedRanks.forEach(rank -> {
                if (Main.instance.bot.getRoleById(rank) == null) {
                    return;
                }
                //noinspection ConstantConditions
                textChannel.putPermissionOverride(Main.instance.bot.getRoleById(rank)).setAllow(
                        Permission.MESSAGE_SEND, Permission.MESSAGE_HISTORY,
                        Permission.MESSAGE_ATTACH_FILES, Permission.VIEW_CHANNEL
                ).queue();
            });

            textChannel.sendMessage("<@" + member.getId() + ">").queue(message ->
                    message.delete().queue());

            new Ticket(ticketType.id, textChannel.getIdLong(), member.getIdLong()).save();

            replyExecutor.execute(Main.instance.jdaConfig.ticketCreated);
            executeOnChannel.execute(textChannel);
        });
    }

}
