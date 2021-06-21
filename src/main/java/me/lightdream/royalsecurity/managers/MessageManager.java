package me.lightdream.royalsecurity.managers;

import me.lightdream.royalsecurity.RoyalSecurity;
import me.lightdream.royalsecurity.SecurityUser;
import me.lightdream.royalsecurity.Utils;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.interactions.components.Button;
import net.dv8tion.jda.api.requests.RestAction;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

import java.util.UUID;

public class MessageManager {

    private final RoyalSecurity plugin;

    public MessageManager(RoyalSecurity plugin) {
        this.plugin = plugin;
    }

    public void sendAuthMessage(JDA jda, UUID uuid, String ip) {
        SecurityUser securityUser = plugin.getApi().getSecurityUser(uuid);

        if (securityUser == null) {
            return;
        }
        RestAction<User> action = jda.retrieveUserById(securityUser.discordID);
        String messageText = plugin.getMessages().authMessage;
        OfflinePlayer minecraftPlayer = Bukkit.getOfflinePlayer(securityUser.getUUID());

        messageText = messageText.replace("%account_name%", minecraftPlayer == null ? "Unknown" : minecraftPlayer.getName());
        messageText = messageText.replace("%account_uuid%", securityUser.getUUID());
        messageText = messageText.replace("%server_name%", plugin.getMessages().serverName);
        messageText = messageText.replace("%ip%", ip);
        String finalMessageText = messageText;

        action.queue(user -> user.openPrivateChannel().queue(channel -> channel.sendMessageEmbeds(Utils.createEmbed(finalMessageText, 255, 255, 0).build())
                .setActionRow(
                        Button.primary("authorize_authentication", "Yes it was me!"),
                        Button.danger("deny_authentication", "No it was not me!"))
                .queue()), Throwable::printStackTrace);
    }

    public void sendLinkMessage(JDA jda, Long userID, String code) {

        RestAction<User> action = jda.retrieveUserById(userID);
        String messageText = plugin.getMessages().linkMessage;

        messageText = messageText.replace("%code%", code);
        String finalMessageText = messageText;

        action.queue(user -> user.openPrivateChannel().queue(channel -> channel.sendMessageEmbeds(Utils.createEmbed(finalMessageText, 255, 255, 0).build()).queue()), Throwable::printStackTrace);
    }


}
