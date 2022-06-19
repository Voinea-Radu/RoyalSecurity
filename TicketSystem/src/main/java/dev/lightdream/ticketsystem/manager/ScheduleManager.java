package dev.lightdream.ticketsystem.manager;

import dev.lightdream.lambda.LambdaExecutor;
import dev.lightdream.logger.Logger;
import dev.lightdream.ticketsystem.Main;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import org.jetbrains.annotations.NotNull;

import java.time.OffsetDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Timer;
import java.util.TimerTask;

public class ScheduleManager {

    public ScheduleManager() {
        registerAutomaticTicketClose();
    }

    public static void lastMessageSent(@NotNull TextChannel textChannel, LambdaExecutor.NoReturnNoArgLambdaExecutor warnLambda,
                                       LambdaExecutor.NoReturnLambdaExecutor<User> closeLambda,
                                       LambdaExecutor.NoReturnLambdaExecutor<Long> defaultLambda) {
        long lastMessageID = textChannel.getLatestMessageIdLong();
        textChannel.retrieveMessageById(lastMessageID).queue(message -> {
            long timeLeft = OffsetDateTime.now().minusHours(24).until(message.getTimeCreated(), ChronoUnit.SECONDS);

            if (message.getEmbeds().size() == 0) {
                if (message.getTimeCreated().isBefore(OffsetDateTime.now().minusHours(23))) {
                    warnLambda.execute();
                }
            } else {
                timeLeft -= 23 * 60 * 60;
                User botUser = Main.instance.bot.getUserById(Main.instance.jdaConfig.botID);
                if (botUser == null) {
                    Logger.warn("Bot id is not valid please make sure it is set correctly to use automatic ticket closing");
                    return;
                }

                if (message.getAuthor().getIdLong() == Main.instance.jdaConfig.botID) {
                    if (message.getTimeCreated().isBefore(OffsetDateTime.now().minusHours(1))) {
                        closeLambda.execute(botUser);
                    }
                }
            }

            defaultLambda.execute(timeLeft);

        }, error -> {
        });
    }

    public void registerAutomaticTicketClose() {
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                Main.instance.databaseManager.getOpenTickets().forEach(ticket -> {
                    TextChannel textChannel = Main.instance.bot.getTextChannelById(ticket.channelID);

                    if (textChannel == null) {
                        return;
                    }

                    lastMessageSent(textChannel, () -> textChannel.sendMessageEmbeds(Main.instance.jdaConfig.inactiveTicket.build().build()).queue(),
                            botUser -> TicketManager.closeTicket(textChannel, botUser), timeLeft -> {
                            });
                });


            }
        }, 0, 10 * 60 * 1000L); //10min
    }


}
