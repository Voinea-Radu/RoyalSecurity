package me.lightdream.royalsecurity.listener;

import me.lightdream.royalsecurity.RoyalSecurity;
import me.lightdream.royalsecurity.SecurityUser;
import me.lucko.helper.Events;
import me.lucko.helper.Schedulers;
import me.lucko.helper.text3.Text;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.player.*;

public class MinecraftListener implements Listener {

    private final RoyalSecurity plugin;

    public MinecraftListener(RoyalSecurity plugin) {
        this.plugin = plugin;
        registerListeners();
    }

    public void registerListeners() {
        Events.subscribe(PlayerJoinEvent.class)
                .handler(event -> {
                    Player player = event.getPlayer();
                    SecurityUser securityUser = plugin.getApi().getSecurityUser(player.getUniqueId());

                    if (securityUser == null) {
                        return;
                    }

                    if (securityUser.ip.equals(player.getAddress().getHostName())) {
                        plugin.loggedInPlayers.add(securityUser.getUUID());
                        Schedulers.sync().runLater(() -> {
                            player.sendMessage(Text.colorize(plugin.getMessages().ipLogged));
                        }, 2);
                        return;
                    }

                    Schedulers.sync().runLater(() -> {
                        player.sendMessage(Text.colorize(plugin.getMessages().messageSent));
                        plugin.getMessageManager().sendAuthMessage(plugin.getDiscordListener().instance, player.getUniqueId(), player.getAddress().getHostName());
                    }, 2);
                }).bindWith(plugin);

        Events.subscribe(PlayerCommandPreprocessEvent.class)
                .handler(event -> {
                    Player player = event.getPlayer();
                    SecurityUser securityUser = plugin.getApi().getSecurityUser(player.getUniqueId());

                    if (securityUser == null) {
                        return;
                    }

                    if (plugin.loggedInPlayers.contains(player.getUniqueId().toString())) {
                        return;
                    }

                    boolean isAllowed = true;
                    for (String str : RoyalSecurity.commandWhitelist) {
                        if (str.contains(event.getMessage())) {
                            isAllowed = false;
                            break;
                        }
                    }
                    if (isAllowed) {
                        event.setCancelled(true);
                    }

                }).bindWith(plugin);

        Events.subscribe(PlayerMoveEvent.class)
                .handler(event -> {
                    Player player = event.getPlayer();
                    SecurityUser securityUser = plugin.getApi().getSecurityUser(player.getUniqueId());

                    if (securityUser == null) {
                        return;
                    }

                    if (!plugin.loggedInPlayers.contains(player.getUniqueId().toString())) {
                        event.setCancelled(true);
                    }
                }).bindWith(plugin);


        Events.subscribe(PlayerInteractEvent.class)
                .handler(event -> {
                    Player player = event.getPlayer();
                    SecurityUser securityUser = plugin.getApi().getSecurityUser(player.getUniqueId());

                    if (securityUser == null) {
                        return;
                    }

                    if (!plugin.loggedInPlayers.contains(player.getUniqueId().toString())) {
                        event.setCancelled(true);
                    }
                }).bindWith(plugin);
        Events.subscribe(PlayerQuitEvent.class)
                .handler(event -> {
                    Player player = event.getPlayer();
                    SecurityUser securityUser = plugin.getApi().getSecurityUser(player.getUniqueId());

                    if (securityUser == null) {
                        return;
                    }

                    plugin.loggedInPlayers.remove(player.getUniqueId().toString());
                }).bindWith(plugin);
    }


}
