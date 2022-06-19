package dev.lightdream.ticketsystem.dto;

import dev.lightdream.jdaextension.dto.JDAButton;
import dev.lightdream.jdaextension.dto.JDAEmbed;
import dev.lightdream.jdaextension.dto.JDAField;
import dev.lightdream.jdaextension.enums.JDAButtonType;

import java.util.ArrayList;
import java.util.Arrays;

@SuppressWarnings("ArraysAsListWithZeroOrOneArgument")
public class JDAConfig extends dev.lightdream.jdaextension.dto.JDAConfig {

    public long botID = 946512611896344637L;

    public JDAEmbed ticket = new JDAEmbed(
            0,
            0,
            0,
            "Create Ticket",
            "",
            "**Select the ticket type that you want to create**",
            Arrays.asList(new JDAField("Warnings!",
                    "We do not solve any problems related to any lost or stolen items!\n" +
                            "All the complains are made on https://original.gg/complain!",
                    true)),
            new ArrayList<>()
    );

    public JDAEmbed unbanTicket = new JDAEmbed(
            0,
            0,
            0,
            "Create Unban Ticket",
            "",
            "",
            Arrays.asList(new JDAField("Warnings!",
                    "If you have been banned for advertising you will not be able to be unbanned!\n" +
                            "If you have been banned as a result of your account being compromised you will need to provide proof that you have secured your account",
                    true)),
            new ArrayList<>()
    );


    public JDAEmbed ticketGreeting = new JDAEmbed(
            0,
            255,
            0,
            "%name%",
            "%avatar%",
            "A staff member will answer your ticket as soon as possible.\n" +
                    "Please state your problem bellow!\n" +
                    "If you need the help of a manager please press the button bellow!",
            new ArrayList<>(),
            Arrays.asList(new JDAButton(JDAButtonType.DANGER, "manager", "\uD83C\uDF93 Manager"),
                    new JDAButton(JDAButtonType.PRIMARY, "close-ticket", "\uD83D\uDD12 Close ticket"))
    );

    public JDAEmbed unbanTicketGreeting = new JDAEmbed(
            0,
            255,
            0,
            "%name%",
            "%avatar%",
            "Please state why you thing you need to be unbanned.\n" +
                    "The staff member that have banned you have been notified.",
            new ArrayList<>(),
            Arrays.asList(
                    new JDAButton(JDAButtonType.PRIMARY, "close-ticket", "\uD83D\uDD12 Close ticket"),
                    new JDAButton(JDAButtonType.PRIMARY, "unban", "\uD83D\uDD13 Unban")
            )
    );

    public JDAEmbed unbanDetails = new JDAEmbed(
            255,
            0,
            0,
            "%name%",
            "",
            "Username: %name%\n" +
                    "ID: %id%\n" +
                    "Banned By: %banned_by_name%\n" +
                    "Banned By ID: %banned_by_id%\n" +
                    "Reason: %reason%\n" +
                    "Ban Date: %date%\n" +
                    "Unban Date: %unban_date%",
            new ArrayList<>(),
            new ArrayList<>()
    );

    public JDAEmbed closingTicket = JDAEmbed.red(
            "Ticket",
            "This ticket will be closed in 5 seconds"
    );

    public JDAEmbed invalidID = JDAEmbed.red(
            "Ban",
            "This is not a valid discord ID"
    );

    public JDAEmbed alreadyBanned = JDAEmbed.red(
            "Ban",
            "User is already banned"
    );

    public JDAEmbed invalidUser = JDAEmbed.red(
            "Users",
            "This is not a valid user"
    );

    public JDAEmbed invalidBannedRole = JDAEmbed.red(
            "Roles",
            "The banned role is invalid please check it"
    );

    public JDAEmbed userBanned = JDAEmbed.green(
            "Roles",
            "The user %name% has been banned"
    );

    public JDAEmbed helpEmbed = JDAEmbed.black(
            "Help",
            "**General**\n" +
                    "/help\n" +
                    "\n**Moderation**\n" +
                    "/ban [user_id] [reason]\n" +
                    "/unban [user_id]\n" +
                    "/checkBan [user_id]\n" +
                    "/banDetails [id]\n" +
                    "/history [user_id]\n" +
                    "\n**Tickets**\n" +
                    "/tickets [user_id]\n" +
                    "/transcript [id]\n" +
                    "/add [user_id]\n" +
                    "/close\n" +
                    "\n**Management**\n" +
                    "/setup\n" +
                    "\n" +
                    "[] - Mandatory arguments\n" +
                    "<> - Optional / Contextual arguments"
    );

    public JDAEmbed setupFinished = JDAEmbed.green(
            "Setup",
            "Setup finished"
    );

    public JDAEmbed notBanned = JDAEmbed.red(
            "Ban",
            "User is not banned"
    );

    public JDAEmbed unBanned = JDAEmbed.green(
            "UnBan",
            "User %name% is now unbanned. %roles_1%/%roles_2% roles have been restored"
    );


    public JDAEmbed notTicket = JDAEmbed.red(
            "Ticket",
            "This chanel is not a valid ticket channel"
    );

    public JDAEmbed alreadyPingedManager = JDAEmbed.red(
            "Ticket",
            "You have already pinged the manager on this ticket. Please be patient we will answer your problem as soon as possible"
    );

    public JDAEmbed error = JDAEmbed.red(
            "Error",
            "An error occurred while performing this action. Please contact the author in regards to this."
    );

    public JDAEmbed addedToTicket = JDAEmbed.green(
            "Tickets",
            "Added %name% to this ticket."
    );

    public JDAEmbed cannotBan = JDAEmbed.red(
            "Ban",
            "I can not ban this use because he has a higher rank then me."
    );

    public JDAEmbed cannotUnban = JDAEmbed.red(
            "Ban",
            "I can not unban this use because he has a higher rank then me."
    );

    public JDAEmbed invalidTicketID = JDAEmbed.red(
            "Ticket",
            "This is not a valid ticket id."
    );

    public JDAEmbed invalidBanID = JDAEmbed.red(
            "Bans",
            "This is not a valid ban id."
    );

    public JDAEmbed closedTicket = JDAEmbed.green(
            "Ticket",
            "Your ticket has been closed. You can access your transcript by using the command `/transcript %id%`."
    );

    public JDAEmbed notAllowedToAccessTranscript = JDAEmbed.red(
            "Transcript",
            "You are not allowed to access this transcript."
    );

    public JDAEmbed transcript = JDAEmbed.black(
            "Transcript",
            "You can access the transcript with id %id% at %url%."
    );

    public JDAEmbed tickets = JDAEmbed.green(
            "%name%'s Tickets (last 10)",
            "You can use /transcript [id] to see the transcript on one specific ticket\n"
    );

    public String ticketsEntry = "[%id%] Ticket %type% on date %date%";

    public JDAEmbed bans = JDAEmbed.green(
            "%name%'s Bans (last 10)",
            "You can use /banDetails [id] to see more about one specific ban\n"
    );

    public String bansEntry = "[%id%] Ban on date %date% for `%reason%`";

    public JDAEmbed blacklisted = JDAEmbed.red(
            "Tickets",
            "You have been blacklisted from create any ticket"
    );

    public JDAEmbed alreadyHaveTicket = JDAEmbed.red(
            "Tickets",
            "You already have a ticket of this type. I have mentioned you on the channel."
    );

    public JDAEmbed ticketCreated = JDAEmbed.green(
            "Tickets",
            "We have created your ticket."
    );

    public JDAEmbed invalidAction = JDAEmbed.red(
            "Commands",
            "This is not a valid action."
    );

    public JDAEmbed blacklistAdded = JDAEmbed.green(
            "Blacklist",
            "User %user% has been added to the blacklist."
    );

    public JDAEmbed blacklistRemoved = JDAEmbed.green(
            "Blacklist",
            "User %user% has been added to the blacklist."
    );

    public JDAEmbed blacklistInfo = JDAEmbed.black(
            "Blacklist",
            "%user%'s blacklist status is %status%."
    );

    public JDAEmbed alreadyBlacklisted = JDAEmbed.red(
            "Blacklist",
            "This user is already blacklisted"
    );

    public JDAEmbed notBlacklisted = JDAEmbed.red(
            "Blacklist",
            "This user is not blacklisted"
    );

    public String blacklistStatusTrue = "blacklisted";
    public String blacklistStatusFalse = "not blacklisted";

    public JDAEmbed inactiveTicket = JDAEmbed.red(
            "Inactive Ticket",
            "This ticket will be closed in 1 hour if there are not messages by then."
    );

    public JDAEmbed timeLeft = JDAEmbed.red(
            "Inactive Ticket",
            "This tickets has a left %h%h %m%m %s%s."
    );

    public JDAEmbed banDurationInvalid = JDAEmbed.red(
            "Invalid duration",
            "The duration is invalid. Please use the format a duration of type [amount]m or [amount]h or [amount]d."
    );

}
