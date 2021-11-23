package dev.lightdream.royalsecurity.managers;

import dev.lightdream.api.utils.Debugger;
import dev.lightdream.royalsecurity.Main;
import dev.lightdream.royalsecurity.database.Cooldown;
import dev.lightdream.royalsecurity.database.User;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;

public class MinecraftEventManager implements Listener {

    @SuppressWarnings({"FieldCanBeLocal", "unused"})
    private final Main plugin;

    public MinecraftEventManager(Main plugin) {
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerJoin(PlayerLoginEvent event) {
        User user = Main.instance.databaseManager.getUser(event.getPlayer().getName());

        if (user == null) {
            user = Main.instance.databaseManager.getUser(event.getPlayer().getUniqueId());
        }

        if (!user.hasSecurity()) {
            return;
        }

        if (event.getAddress().getHostName().equals(user.ip)) {
            return;
        }

        String ip = event.getAddress().getHostName();

        for (Cooldown cooldown : Main.instance.databaseManager.getCooldown(ip)) {
            if (cooldown != null) {
                if (cooldown.isValid()) {
                    Debugger.info("Kicking for cooldown");
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
