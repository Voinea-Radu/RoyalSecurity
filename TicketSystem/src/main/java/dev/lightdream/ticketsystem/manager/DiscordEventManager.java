package dev.lightdream.ticketsystem.manager;

import dev.lightdream.jdaextension.dto.JDAEmbed;
import dev.lightdream.logger.Debugger;
import dev.lightdream.logger.Logger;
import dev.lightdream.ticketsystem.Main;
import dev.lightdream.ticketsystem.database.BanRecord;
import dev.lightdream.ticketsystem.database.Ticket;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.events.interaction.ButtonClickEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.exceptions.HierarchyException;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class DiscordEventManager extends ListenerAdapter {

    public DiscordEventManager(Main plugin) {
        plugin.bot.addEventListener(this);
    }

    @Override
    public void onButtonClick(ButtonClickEvent event) {
        String id = event.getComponentId();
        Debugger.info(id);

        Conditions:
        if (id.equalsIgnoreCase("close-ticket")) {
            Debugger.info("IF-ELSE 1");
            JDAEmbed embed = TicketManager.closeTicket(event.getTextChannel(), event.getUser());
            event.getTextChannel().sendMessageEmbeds(embed.build().build()).queue();
            event.deferEdit().queue();
        } else if (id.equalsIgnoreCase("manager")) {
            Debugger.info("IF-ELSE 2");
            MessageChannel channel = event.getChannel();

            Ticket ticket = Main.instance.databaseManager.getTicket(channel.getIdLong());
            if (ticket == null) {
                break Conditions;
            }

            if (ticket.pingedManager) {
                channel.sendMessageEmbeds(Main.instance.jdaConfig.alreadyPingedManager.build().build()).queue();
                break Conditions;
            }

            channel.sendMessage("<@&" + Main.instance.config.managerPingRank + ">").queue(message ->
                    message.delete().queue());

            ticket.pingedManager = true;
            ticket.save();
            event.deferEdit().queue();
        } else if (id.equals(Main.instance.config.unbanTicket.id)) {
            Debugger.info("IF-ELSE 3");

            User user = event.getUser();

            BanRecord ban = Main.instance.databaseManager.getBan(user.getIdLong());

            if (ban == null) {
                event.replyEmbeds(Main.instance.jdaConfig.notBanned.build().build()).setEphemeral(true).queue();
                return;
            }

            TicketManager.createTicket(event.getGuild(), event.getMember(), Main.instance.config.unbanTicket, (textChannel) -> {
                String avatar = user.getAvatarUrl() == null ?
                        "https://external-preview.redd.it/4PE-nlL_PdMD5PrFNLnjurHQ1QKPnCvg368LTDnfM-M.png?auto=webp&s=ff4c3fbc1cce1a1856cff36b5d2a40a6d02cc1c3" :
                        user.getAvatarUrl();

                Main.instance.jdaConfig.unbanTicketGreeting
                        .parse("name", user.getName())
                        .parse("avatar", avatar)
                        .buildMessageAction(textChannel).queue();

                ban.sendBanDetails(textChannel);

                textChannel.sendMessage("<@" + ban.bannedBy + ">").queue(message ->
                        message.delete().queue());

                if(!ban.isApplicable()){
                     ban.unban(textChannel);
                }

            }, embed -> event.replyEmbeds(embed.build().build()).setEphemeral(true).queue());
        } else if (id.equalsIgnoreCase("unban")) {
            Debugger.info("IF-ELSE 4");

            Member member = event.getMember();
            TextChannel channel = event.getTextChannel();

            if (member == null || !member.hasPermission(Permission.BAN_MEMBERS)) {
                channel.sendMessageEmbeds(Main.instance.jdaConfig.notAllowed.build().build()).queue();
                break Conditions;
            }

            Ticket ticket = Main.instance.databaseManager.getTicket(channel.getIdLong());

            if (ticket == null) {
                channel.sendMessageEmbeds(Main.instance.jdaConfig.error.build().build()).queue();
                return;
            }

            BanManager.unban(ticket.creatorID, channel);
            event.deferEdit().queue();
        } else {
            Debugger.info("IF-ELSE 5");

            Main.instance.config.ticketTypes.forEach(ticketType -> {
                if (!ticketType.id.equals(id)) {
                    return;
                }

                Debugger.info("For loop instance");

                TicketManager.createTicket(event.getGuild(), event.getMember(), ticketType, (textChannel) -> {
                    User user = event.getUser();

                    String avatar = user.getAvatarUrl() == null ?
                            "https://external-preview.redd.it/4PE-nlL_PdMD5PrFNLnjurHQ1QKPnCvg368LTDnfM-M.png?auto=webp&s=ff4c3fbc1cce1a1856cff36b5d2a40a6d02cc1c3" :
                            user.getAvatarUrl();

                    Main.instance.jdaConfig.ticketGreeting
                            .parse("name", user.getName())
                            .parse("avatar", avatar)
                            .buildMessageAction(textChannel).queue();
                }, embed -> event.replyEmbeds(embed.build().build()).setEphemeral(true).queue());
            });
        }
    }

    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        if (event.getAuthor().isBot() || !event.isFromGuild()) {
            return;
        }

        try {
            AtomicBoolean inCategory = new AtomicBoolean(false);

            if (Main.instance.config.unbanTicket.categoryID.equals(event.getTextChannel().getParentCategoryIdLong())) {
                inCategory.set(true);
            }

            Main.instance.config.ticketTypes.forEach(ticketType -> {
                if (ticketType.categoryID.equals(event.getTextChannel().getParentCategoryIdLong())) {
                    inCategory.set(true);
                }
            });

            if (!inCategory.get()) {
                return;
            }

            Ticket ticket = Main.instance.databaseManager.getTicket(event.getChannel().getIdLong());

            if (ticket == null) {
                return;
            }

            ticket.getTranscript().record(event.getAuthor(), event.getMessage().getContentRaw());
        } catch (Throwable t) {
            //Empty
        }
    }

    @Override
    public void onGuildMemberJoin(@NotNull GuildMemberJoinEvent event) {

        Debugger.info(event.getMember() + " has joined");

        BanRecord banRecord = Main.instance.databaseManager.getBan(event.getMember().getIdLong());
        if (banRecord == null) {
            Debugger.info(event.getMember() + " is not banned");
            return;
        }

        Debugger.info(event.getMember() + " is banned");

        Member member = event.getMember();
        Guild guild = event.getGuild();

        List<Long> ranks = new ArrayList<>();

        member.getRoles().forEach(role -> ranks.add(role.getIdLong()));

        for (Long rank : ranks) {
            Role role = guild.getRoleById(rank);
            if (role == null) {
                continue;
            }
            try {
                guild.removeRoleFromMember(member, role).queue();
            } catch (HierarchyException e) {
                Logger.error(Main.instance.jdaConfig.cannotBan.description);
                return;
            }
        }

        Role role = guild.getRoleById(Main.instance.config.bannedRank);

        if (role == null) {
            Logger.error(Main.instance.jdaConfig.invalidBannedRole.description);
            return;
        }

        guild.addRoleToMember(member, role).queue();
    }
}


