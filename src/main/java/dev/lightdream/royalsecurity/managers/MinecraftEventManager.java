package dev.lightdream.royalsecurity.managers;

import dev.lightdream.logger.Debugger;
import dev.lightdream.royalsecurity.Main;
import dev.lightdream.royalsecurity.database.Cooldown;
import dev.lightdream.royalsecurity.database.User;
import fr.xephi.authme.api.v3.AuthMeApi;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;

import java.util.Objects;

public class MinecraftEventManager implements Listener {

    @SuppressWarnings({"FieldCanBeLocal", "unused"})
    private final Main plugin;

    public MinecraftEventManager(Main plugin) {
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @SuppressWarnings({"unused"})
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerJoin(PlayerLoginEvent event) {
        User user = Main.instance.databaseManager.createUser(event.getPlayer());

        if (!user.hasSecurity()) {
            if (!Objects.equals(Main.instance.config.requiredPermission, "")) {
                if (Main.instance.config.requiredPermission.equals("none")) {
                    event.setResult(PlayerLoginEvent.Result.KICK_OTHER);
                    event.setKickMessage(Main.instance.lang.required);
                } else {
                    if (event.getPlayer().hasPermission(Main.instance.config.requiredPermission)) {
                        event.setResult(PlayerLoginEvent.Result.KICK_OTHER);
                        event.setKickMessage(Main.instance.lang.required);
                    }
                }
            }

            return;
        }

        String ip = event.getAddress().getHostName();

        Debugger.info(Main.instance.databaseManager.getLockdown(user.discordID).status);

        if (Main.instance.databaseManager.getLockdown(user.discordID).status) {
            new Cooldown(ip);
            event.setResult(PlayerLoginEvent.Result.KICK_OTHER);
            event.setKickMessage(Main.instance.lang.cooldown);
            return;
        }

        if (event.getAddress().getHostName().equals(user.ip)) {
            if (user.autoConnect()) {
                Bukkit.getScheduler().runTaskLater(Main.instance, () -> {
                    AuthMeApi.getInstance().forceLogin(event.getPlayer());
                    Bukkit.getScheduler().runTaskLater(Main.instance, () -> event.getPlayer().sendMessage(Main.instance.lang.autoConnected), 20L);
                }, 40L);
            }
            return;
        }

        for (Cooldown cooldown : Main.instance.databaseManager.getCooldown(ip)) {
            if (cooldown != null) {
                if (cooldown.isValid()) {
                    event.setResult(PlayerLoginEvent.Result.KICK_OTHER);
                    event.setKickMessage(Main.instance.lang.cooldown);
                    return;
                }
            }
        }

        event.setResult(PlayerLoginEvent.Result.KICK_OTHER);
        event.setKickMessage(Main.instance.lang.authoriseConnection);
        user.sendAuth(ip);
    }

}
