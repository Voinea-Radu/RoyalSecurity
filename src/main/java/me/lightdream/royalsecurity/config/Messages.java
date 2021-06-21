package me.lightdream.royalsecurity.config;

public class Messages {

    public String tokenNotSet = "You have not set any tokens in the plugin's config.";
    public String serverName = "lightdream.tk";
    public String authMessage = "You have logged in into your minecraft (%account_name% ~ %account_uuid%) on our server (%server_name%) from the ip %ip%. We detected this login as an unusual connection.";
    public String linkMessage = "Use the command `/rs-link %code%` in order to link your discord account with the minecraft one" ;

    public String minecraftAccountAlreadyRegistered = "This minecraft account is already linked to a discord account";
    public String minecraftAccountNotRegistered = "This minecraft account is not linked to a discord account";
    public String discordAccountAlreadyRegistered = "This discord account is already linked to a minecraft account";
    public String discordAccountNotRegistered = "This discord account is not linked to a minecraft account";
    public String linkCodeSent = "We have sent you the code needed to link your account. Please execute that command in-game. If you did not received any message from us please check that you do not have the bot blocked and if you accept messages from non-friends and members of the same guild.";
    public String invalidDiscordID = "The id you have entered is not valid. Please check it.";
    public String notAdmin = "You are not allowed to use this command or subcommand because you are not an admin";
    public String linkSuccessful = "You have successfully linked your discord and minecraft account";
    public String unlinkSuccessful = "You have successfully unlinked your discord and minecraft account";
    public String adminAdded = "You have successfully added this person as an admin";
    public String adminRemoved = "You have successfully removed this person as an admin";

    public String ipLogged = "You have been logged in based on you ip address";
    public String messageSent = "We have detected some irregular activity on your account. We've sent you a confirmation message over on discord.";

    public String invalidCode = "This code is invalid";

    public String helpCommandDefault = "Commands:\n" +
            "+help - Show this help embed\n" +
            "+link [Minecraft Name] - Links your minecraft account with your discord account\n" +
            "+unlink - Unlinks your minecraft account from your discord account";

    public String helpCommandAdmin = "Commands:\n" +
            "+help - Show this help embed\n" +
            "+link [Minecraft Name] [Discord ID] - Links the specified minecraft account with the specified discord account\n" +
            "+unlink [Minecraft Name / Mention / Discord ID] - Unlinks the specified minecraft account from the specified discord account\n"+
            "+admin [add / remove] [Mention / Discord ID] - Adds / Removes a person as an admin";

    public String nullUUID = "We could not found the UUID for the account your have specified. Please make sure that you have logged it at least once onto the server";

}
