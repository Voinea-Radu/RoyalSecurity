package me.lightdream.royalsecurity;

import net.dv8tion.jda.api.EmbedBuilder;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

import java.awt.*;
import java.util.UUID;

public class Utils {
    public static EmbedBuilder createEmbed(String str, int r, int g, int b) {
        EmbedBuilder embed = new EmbedBuilder();

        embed.setTitle("RoyalSecurity", null);
        embed.setColor(new Color(r, g, b));
        embed.setDescription(str);
        embed.setFooter("Author: LightDream#4379");

        return embed;
    }

    public static UUID getUUID(String name) {
        OfflinePlayer player = Bukkit.getOfflinePlayer(name);
        return player != null ? player.getUniqueId() : null;
    }
}
