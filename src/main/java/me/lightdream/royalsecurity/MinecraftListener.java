package me.lightdream.royalsecurity;

import com.google.gson.internal.$Gson$Preconditions;
import net.dv8tion.jda.api.entities.User;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.*;

import java.util.Objects;

public class MinecraftListener implements Listener {

    public MinecraftListener() { }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e)
    {
        Player p = e.getPlayer();
        if(Royalsecurity.secured(p.getName()))
        {
            if(Royalsecurity.getIp(p.getName()).equals(p.getAddress().getHostName()))
            {
                Royalsecurity.logedInPlayers.add(p.getName());
                BukkitRunnable.sendTextLater(p, Royalsecurity.getPlugin().getConfig().getString("ip-logged"), 2);
                //Bukkit.dispatchCommand(Bukkit.getServer().getConsoleSender(), "lp user " + p.getName() + " permission set dm.selector true");
                //if(Royalsecurity.perms.contains(p.getName()))
                //    Bukkit.dispatchCommand(Bukkit.getServer().getConsoleSender(), "lp user " + p.getName() + " permission set * true");
            }
            else
            {
                //Bukkit.dispatchCommand(Bukkit.getServer().getConsoleSender(), "lp user " + p.getName() + " permission set dm.selector false");
                //if(Royalsecurity.perms.contains(p.getName()))
                //    Bukkit.dispatchCommand(Bukkit.getServer().getConsoleSender(), "lp user " + p.getName() + " permission set * false");
                BukkitRunnable.sendTextLater(p, Royalsecurity.getPlugin().getConfig().getString("message-send"), 2);
                Royalsecurity.sendAuthMessage(DiscordListener.instance, Royalsecurity.getID(p.getName()), p.getAddress().getHostName());
            }
        }
        else
            Royalsecurity.logedInPlayers.add(e.getPlayer().getName());
    }

    @EventHandler
    public void onCommandSend(PlayerCommandPreprocessEvent e)
    {
        Player p = e.getPlayer();
        if(Royalsecurity.secured(p.getName()))
        {
            if (!Royalsecurity.logedInPlayers.contains(e.getPlayer().getName()))
            {
                boolean ok = true;
                for(String str : Royalsecurity.commandWhitelist)
                {
                    if(str.contains(e.getMessage()))
                    {
                        ok = false;
                        break;
                    }
                }
                if(ok)
                    e.setCancelled(true);
            }
        }
    }
    @EventHandler
    public void onPlayerMove(PlayerMoveEvent e)
    {
        Player p = e.getPlayer();

        if (!Royalsecurity.logedInPlayers.contains(e.getPlayer().getName()))
            if(Royalsecurity.secured(p.getName()))
                e.setCancelled(true);
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent e)
    {
        Player p = e.getPlayer();

        if (!Royalsecurity.logedInPlayers.contains(e.getPlayer().getName()))
            if(Royalsecurity.secured(p.getName()))
                e.setCancelled(true);
    }

    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent e)
    {
        Player p = e.getPlayer();
        Royalsecurity.logedInPlayers.remove(p.getName());
        Bukkit.dispatchCommand(Bukkit.getServer().getConsoleSender(), "lp user " + p.getName() + " permission set dm.compass true");
        if(Royalsecurity.perms.contains(p.getName()))
            Bukkit.dispatchCommand(Bukkit.getServer().getConsoleSender(), "lp user " + p.getName() + " permission unset * ");

    }

}
