package dev.lightdream.royalsecurity.files;

import dev.lightdream.commandmanager.dto.CommandLang;
import dev.lightdream.jdaextension.dto.JDALang;

public class Lang extends CommandLang implements JDALang {

    public String linkCommandDescription = "Link your minecraft account to the discord one";
    public String unlinkCommandDescription = "Unlink your minecraft account from the discord one";
    public String changePasswordDescription = "Receive a private message from the bot in order to change your password";
    public String accountsDescription = "List all minecraft linked accounts to your discord account";
    public String unregisterDescription = "Unregister the account of the target";
    public String lockdownDescription = "Lock you account. All the ips that will try to access your account will be blacklisted.";
    public String required = "You are required to use the security. Please refer to our discord in order to link your account";
    public String helpCommandDescription = "Send the help command";
    public String statsCommandDescription = "Shows technical details about the bot and its environment";

    public String userIDArgDescription = "User ID";
    public String accountArgDescription = "Account";
    public String passwordArgDescription = "The new password";
    public String usernameArgDescription = "Minecraft username";

    public String invalidCode = "Invalid code";
    public String linked = "Linked";

    public String authoriseConnection = "Please authorise this connection";
    public String cooldown = "Your ip has a cooldown. Please try again in 30 minutes";

    public String codeDetails = "User: %name%\n" + "Discord ID: %discord_id%";

    public String autoConnect = "Your auto connect status is now %status%";
    public String autoConnected = "You have been auto-connected based on your ip. You can stop this by doing /link auto-connect";

    @Override
    public String getHelpCommandDescription() {
        return helpCommandDescription;
    }

    @Override
    public String getStatsCommandDescription() {
        return statsCommandDescription;
    }
}
