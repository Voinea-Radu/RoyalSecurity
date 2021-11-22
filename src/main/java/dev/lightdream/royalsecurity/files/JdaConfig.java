package dev.lightdream.royalsecurity.files;

import dev.lightdream.api.dto.jda.Button;
import dev.lightdream.api.dto.jda.JdaEmbed;
import dev.lightdream.api.dto.jda.JdaField;
import dev.lightdream.api.enums.JDAButtonType;

import java.util.ArrayList;
import java.util.Arrays;

public class JdaConfig extends dev.lightdream.api.configs.JdaConfig {

    public JdaEmbed auth = new JdaEmbed(255, 255, 0, "Auth Message", "", "", Arrays.asList(
            new JdaField("Username", "%player_name%", true),
            new JdaField("IP", "%ip%", true),
            new JdaField("Date", "%date%", true),
            new JdaField("Server", "mc.lightdream.dev", true)
    ), Arrays.asList(
            new Button(JDAButtonType.PRIMARY, "authorize_authentication_%ip%;%player_name%", "Yes it was me"),
            new Button(JDAButtonType.DANGER, "deny_authentication_%ip%;%player_name%", "No it was not me")
    ));

    public JdaEmbed secure = new JdaEmbed(0, 255, 0, "Secure Message",
            "", "Please rejoin the server and use the command `/link %code%` in-game to confirm the link", new ArrayList<>(), new ArrayList<>());

    public JdaEmbed invalidUser = new JdaEmbed(255, 0, 0, "Invalid User", "",
            "This is not a valid minecraft username. Please make sure to login first on the server", new ArrayList<>(), new ArrayList<>());

    public JdaEmbed alreadyLinked = new JdaEmbed(255, 0, 0, "Already linked", "",
            "This minecraft account is already linked to a discord account. Please make sure that you typed the account name correctly" +
                    " or contact an administrator", new ArrayList<>(), new ArrayList<>());

    public JdaEmbed codeSent = new JdaEmbed(0, 255, 0, "Code send", "",
            "I have sent you a code in private messages. Please use that command on the server.", new ArrayList<>(), new ArrayList<>());

    public JdaEmbed unlinked = new JdaEmbed(0, 255, 0, "Unlinked", "",
            "Successfully unlinked account", new ArrayList<>(), new ArrayList<>());

    public JdaEmbed usage = new JdaEmbed(0, 0, 0, "%command%", "",
            "+%command% %usage%", new ArrayList<>(), new ArrayList<>());

    public JdaEmbed helpEmbed = new JdaEmbed(0, 0, 0, "Help", "",
            "+help\n" +
                    "+link [username]\n" +
                    "+unlink <username>\n" +
                    "+changePassword <username> [newPassword] - In DMs\n" +
                    "+accounts <discordID>\n" +
                    "+stats\n"+
                    "+unregister <discordID>\n"+
                    "\n" +
                    "[] - Mandatory arguments\n" +
                    "<> - Optional / Contextual arguments",
            new ArrayList<>(), new ArrayList<>());

    public JdaEmbed accessGranted = new JdaEmbed(0, 255, 0, "Access granted", "",
            "Please wait a few seconds and join again ", new ArrayList<>(), new ArrayList<>());

    public JdaEmbed accessDenied = new JdaEmbed(255, 0, 0, "Access denied", "", "",
            new ArrayList<>(), new ArrayList<>());

    public JdaEmbed notLinked = new JdaEmbed(255, 0, 0, "Not Linked", "",
            "Your account does not seem to be linked to any minecraft account. " +
                    "You can link your account using the `+link` command", new ArrayList<>(), new ArrayList<>());

    @SuppressWarnings("ArraysAsListWithZeroOrOneArgument")
    public JdaEmbed accounts = new JdaEmbed(0, 0, 0, "Linked Accounts", "", "", Arrays.asList(
            new JdaField("%user%'s Accounts", "%player_name%\n", true)
    ), new ArrayList<>());

    public JdaEmbed multipleLinked = new JdaEmbed(255, 0, 0, "Multiple Linked", "",
            "You have multiple accounts please add the argument to your command `[username]` in order to execute this command",
            new ArrayList<>(), new ArrayList<>());

    public JdaEmbed notOwner = new JdaEmbed(255, 0, 0, "Not Owner", "",
            "You seem to not be the owner of this minecraft account. If you think that this is an error please contact an administrator",
            new ArrayList<>(), new ArrayList<>());

    public JdaEmbed serverCommand = new JdaEmbed(255, 0, 0, "Server Required", "",
            "This command is required to be run in a server rather then on private messages", new ArrayList<>(), new ArrayList<>());

    public JdaEmbed dmsCommand = new JdaEmbed(255, 0, 0, "DMs Required", "",
            "This command is required to be rin in DMs rather then on a server", new ArrayList<>(), new ArrayList<>());

    public JdaEmbed passwordChanged = new JdaEmbed(255, 0, 0, "Password Changed", "",
            "Your password has been successfully changed", new ArrayList<>(), new ArrayList<>());

    public JdaEmbed unregistered = new JdaEmbed(0, 255, 0, "Unregistered", "",
            "The target account has been successfully unregistered", new ArrayList<>(), new ArrayList<>());

    public JdaEmbed invalidNumber = new JdaEmbed(255, 0, 0, "Invalid Number", "",
            "This is not a valid number. Please check it and try again", new ArrayList<>(), new ArrayList<>());

    public JdaEmbed notAllowed = new JdaEmbed(255, 0, 0, "Not Allowed", "",
            "You do not have the necessary permission to do this.", new ArrayList<>(), new ArrayList<>());

    public JdaEmbed cannotSendMessage = new JdaEmbed(255, 0, 0, "Cannot Send Message", "",
            "It seems like I can not send you a message in dms. Consider opening your dms to server members", new ArrayList<>(), new ArrayList<>());

    public JdaEmbed stats = new JdaEmbed(0, 0, 0, "Stats", "", "", Arrays.asList(
            new JdaField("RAM", "%ram%MB", true),
            new JdaField("CPU", "%cpu%%", true),
            new JdaField("Java Version", "%java%", true),
            new JdaField("Active codes", "%codes%", true),
            new JdaField("Active accounts", "%users%", true)
    ), new ArrayList<>());

    public JdaEmbed reGiven = new JdaEmbed(0, 255, 0, "Results", "",
            "Given the rank and changed the nickname to %count% users", new ArrayList<>(), new ArrayList<>());


}
