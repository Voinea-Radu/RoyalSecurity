package dev.lightdream.royalsecurity;

import dev.lightdream.ticketsystem.ISecurity;
import net.dv8tion.jda.api.JDA;

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
}
