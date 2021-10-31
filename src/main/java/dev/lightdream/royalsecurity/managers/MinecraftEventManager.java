package dev.lightdream.royalsecurity.managers;

import dev.lightdream.royalsecurity.Main;
import dev.lightdream.royalsecurity.dto.User;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class MinecraftEventManager implements Listener {

    @SuppressWarnings({"FieldCanBeLocal", "unused"})
    private final Main plugin;

    public MinecraftEventManager(Main plugin){
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event){
        User user = Main.instance.databaseManager.getUser(event.getPlayer());

        if(!user.hasSecurity()){
            return;
        }

        if(event.getPlayer().getAddress().getHostName().equals(user.ip)){
            return;
        }

        String ip = event.getPlayer().getAddress().getHostName();

        event.getPlayer().kickPlayer(Main.instance.lang.kickMessage);
        user.sendAuth(ip);
    }

}
