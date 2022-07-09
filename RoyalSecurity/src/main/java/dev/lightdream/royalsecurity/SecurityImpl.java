package dev.lightdream.royalsecurity;

import dev.lightdream.royalsecurity.database.User;
import dev.lightdream.ticketsystem.ISecurity;
import net.dv8tion.jda.api.JDA;
import org.bukkit.Bukkit;

public class SecurityImpl implements ISecurity {

    @Override
    public void sayHello() {
        System.out.println("Hello from the plugin security implementation!");
    }

    @Override
    public String getConfigPath() {
        return "/plugins/TicketSystem/";
    }

    @Override
    public JDA getBot() {
        return Main.instance.bot;
    }

    @Override
    public void banUser(Long user, Long bannedBy, String reason, Long duration) {
        for (User securityUser : Main.instance.databaseManager.getUser(user)) {
            Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), Main.instance.config.banCommand
                    .parse("username", securityUser.name)
                    .parse("duration", duration / 1000)
                    .parse("reason", reason)
                    .parse()
            );
        }
    }

    @Override
    public void unbanUser(Long user) {
        for (User securityUser : Main.instance.databaseManager.getUser(user)) {
            Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), Main.instance.config.unbanCommand
                    .parse("username", securityUser.name)
                    .parse()
            );
        }
    }
}
