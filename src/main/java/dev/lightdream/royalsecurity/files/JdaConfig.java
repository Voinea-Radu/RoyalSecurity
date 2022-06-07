package dev.lightdream.royalsecurity.files;

import dev.lightdream.jdaextension.dto.JDAButton;
import dev.lightdream.jdaextension.dto.JDAConfig;
import dev.lightdream.jdaextension.dto.JDAEmbed;
import dev.lightdream.jdaextension.dto.JDAField;
import dev.lightdream.jdaextension.enums.JDAButtonType;

import java.util.ArrayList;
import java.util.Arrays;

public class JdaConfig extends JDAConfig {

    public JDAEmbed auth = new JDAEmbed(
            255,
            255,
            0,
            "Auth Message",
            "",
            "",
            Arrays.asList(new JDAField("Username", "%player_name%", true),
                    new JDAField("IP", "%ip%", true),
                    new JDAField("Date", "%date%", true)),
            Arrays.asList(new JDAButton(JDAButtonType.PRIMARY, "authorize_authentication_%ip%;%player_name%", "Yes it was me"),
                    new JDAButton(JDAButtonType.DANGER, "deny_authentication_%ip%;%player_name%", "No it was not me"))
    );

    public JDAEmbed secure = JDAEmbed.green(
            "Secure Message",
            "Please rejoin the server and use the command `/link %code%` in-game to confirm the link"
    );

    public JDAEmbed invalidUser = JDAEmbed.red(
            "Invalid User",
            "This is not a valid minecraft username. Please make sure to login first on the server"
    );

    public JDAEmbed alreadyLinked = JDAEmbed.red(
            "Already linked",
            "This minecraft account is already linked to a discord account. Please make sure that you typed the account name correctly" +
                    " or contact an administrator"
    );

    public JDAEmbed codeSent = JDAEmbed.green(
            "Code send",
            "I have sent you a code in private messages. Please use that command on the server."
    );

    public JDAEmbed unlinked = JDAEmbed.green(
            "Unlinked",
            "Successfully unlinked account"
    );

    public JDAEmbed usage = JDAEmbed.black(
            "%command%",
            "+%command% %usage%"
    );

    public JDAEmbed helpEmbed = JDAEmbed.black(
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

    public JDAEmbed accessGranted = JDAEmbed.green(
            "Access granted",
            "Please wait a few seconds and join again "
    );

    public JDAEmbed accessDenied = JDAEmbed.red(
            "Access denied",
            ""
    );

    public JDAEmbed notLinked = JDAEmbed.red(
            "Not Linked",
            "Your account does not seem to be linked to any minecraft account. " +
                    "You can link your account using the `+link` command"
    );

    @SuppressWarnings("ArraysAsListWithZeroOrOneArgument")
    public JDAEmbed accounts = new JDAEmbed(
            0,
            0,
            0,
            "Linked Accounts",
            "",
            "",
            Arrays.asList(new JDAField("%user%'s Accounts", "`%player_name%`\n", true)),
            new ArrayList<>()
    );

    public JDAEmbed multipleLinked = JDAEmbed.red(
            "Multiple Linked",
            "You have multiple accounts please add the argument to your command `[username]` in order to execute this command"
    );

    public JDAEmbed notOwner = JDAEmbed.red(
            "Not Owner",
            "You seem to not be the owner of this minecraft account. If you think that this is an error please contact an administrator"
    );

    public JDAEmbed dmsCommand = JDAEmbed.red(
            "DMs Required",
            "This command is required to be rin in DMs rather then on a server"
    );

    public JDAEmbed passwordChanged = JDAEmbed.green(
            "Password Changed",
            "Your password has been successfully changed"
    );

    public JDAEmbed unregistered = JDAEmbed.green(
            "Unregistered",
            "The target account has been successfully unregistered"
    );

    public JDAEmbed invalidNumber = JDAEmbed.red(
            "Invalid Number",
            "This is not a valid number. Please check it and try again"
    );

    public JDAEmbed cannotSendMessage = JDAEmbed.red(
            "Cannot Send Message",
            "It seems like I can not send you a message in dms. Consider opening your dms to server members"
    );


    public JDAEmbed payments = JDAEmbed.green(
            "Payments",
            "%name%'s payments:"
    );

    public String payment = "%package_name% (x%package_quantity%)";

    public JDAEmbed passwordCanNotBeEmpty = JDAEmbed.red(
            "Password can not be empty",
            "Please make sure you press tab after you typed your password and that is not an empty password"
    );

}
