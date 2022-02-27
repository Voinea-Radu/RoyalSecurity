package dev.lightdream.royalsecurity.files;

import dev.lightdream.jdaextension.dto.Button;
import dev.lightdream.jdaextension.dto.JDAConfig;
import dev.lightdream.jdaextension.dto.JdaEmbed;
import dev.lightdream.jdaextension.dto.JdaField;
import dev.lightdream.jdaextension.enums.JDAButtonType;

import java.util.ArrayList;
import java.util.Arrays;

public class JdaConfig extends JDAConfig {

    public JdaEmbed auth = new JdaEmbed(
            255,
            255,
            0,
            "Auth Message",
            "",
            "",
            Arrays.asList(new JdaField("Username", "%player_name%", true),
                    new JdaField("IP", "%ip%", true),
                    new JdaField("Date", "%date%", true)),
            Arrays.asList(new Button(JDAButtonType.PRIMARY, "authorize_authentication_%ip%;%player_name%", "Yes it was me"),
                    new Button(JDAButtonType.DANGER, "deny_authentication_%ip%;%player_name%", "No it was not me"))
    );

    public JdaEmbed secure = JdaEmbed.green(
            "Secure Message",
            "Please rejoin the server and use the command `/link %code%` in-game to confirm the link"
    );

    public JdaEmbed invalidUser = JdaEmbed.red(
            "Invalid User",
            "This is not a valid minecraft username. Please make sure to login first on the server"
    );

    public JdaEmbed alreadyLinked = JdaEmbed.red(
            "Already linked",
            "This minecraft account is already linked to a discord account. Please make sure that you typed the account name correctly" +
                    " or contact an administrator"
    );

    public JdaEmbed codeSent = JdaEmbed.green(
            "Code send",
            "I have sent you a code in private messages. Please use that command on the server."
    );

    public JdaEmbed unlinked = JdaEmbed.green(
            "Unlinked",
            "Successfully unlinked account"
    );

    public JdaEmbed usage = JdaEmbed.black(
            "%command%",
            "+%command% %usage%"
    );

    public JdaEmbed helpEmbed = JdaEmbed.black(
            "Help",
            "+help\n" +
                    "+link [username]\n" +
                    "+unlink <username>\n" +
                    "+changePassword <username> [newPassword] - In DMs\n" +
                    "+accounts <discordID>\n" +
                    "+stats\n" +
                    "+unregister <discordID>\n" +
                    "\n" +
                    "[] - Mandatory arguments\n" +
                    "<> - Optional / Contextual arguments"
    );

    public JdaEmbed accessGranted = JdaEmbed.green(
            "Access granted",
            "Please wait a few seconds and join again "
    );

    public JdaEmbed accessDenied = JdaEmbed.red(
            "Access denied",
            ""
    );

    public JdaEmbed notLinked = JdaEmbed.red(
            "Not Linked",
            "Your account does not seem to be linked to any minecraft account. " +
                    "You can link your account using the `+link` command"
    );

    @SuppressWarnings("ArraysAsListWithZeroOrOneArgument")
    public JdaEmbed accounts = new JdaEmbed(
            0,
            0,
            0,
            "Linked Accounts",
            "",
            "",
            Arrays.asList(new JdaField("%user%'s Accounts", "`%player_name%`\n", true)),
            new ArrayList<>()
    );

    public JdaEmbed multipleLinked = JdaEmbed.red(
            "Multiple Linked",
            "You have multiple accounts please add the argument to your command `[username]` in order to execute this command"
    );

    public JdaEmbed notOwner = JdaEmbed.red(
            "Not Owner",
            "You seem to not be the owner of this minecraft account. If you think that this is an error please contact an administrator"
    );

    public JdaEmbed dmsCommand = JdaEmbed.red(
            "DMs Required",
            "This command is required to be rin in DMs rather then on a server"
    );

    public JdaEmbed passwordChanged = JdaEmbed.red(
            "Password Changed",
            "Your password has been successfully changed"
    );

    public JdaEmbed unregistered = JdaEmbed.green(
            "Unregistered",
            "The target account has been successfully unregistered"
    );

    public JdaEmbed invalidNumber = JdaEmbed.red(
            "Invalid Number",
            "This is not a valid number. Please check it and try again"
    );

    public JdaEmbed notAllowed = JdaEmbed.red(
            "Not Allowed",
            "You do not have the necessary permission to do this."
    );

    public JdaEmbed cannotSendMessage = JdaEmbed.red(
            "Cannot Send Message",
            "It seems like I can not send you a message in dms. Consider opening your dms to server members"
    );


    public JdaEmbed lockdown = JdaEmbed.green(
            "Lockdown",
            "Your account lockdown status is %status%"
    );

}
