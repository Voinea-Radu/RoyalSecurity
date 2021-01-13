package me.lightdream.royalsecurity;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.sql.SQLException;

public class BukkitRunnable {

    public static void sendTextLater(Player p, String str, int time)
    {
        Bukkit.getScheduler().runTaskLater(Royalsecurity.getPlugin(), () -> p.sendMessage(Royalsecurity.color(str)), time);
    }

    public static void renewDatabaseConnection()
    {
        Bukkit.getScheduler().runTaskTimer(Royalsecurity.getPlugin(), () -> {
            DatabaseConnector.sqlRenew();
            System.out.println(ChatColor.GREEN + "Connection renewd");
            try {
                DatabaseConnector.getCon().prepareStatement("SELECT * FROM RS_IP").executeQuery();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }, 0L, 12000L);
    }

}
