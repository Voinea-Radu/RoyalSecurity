package dev.lightdream.royalsecurity.managers;

import dev.lightdream.royalsecurity.Main;
import dev.lightdream.royalsecurity.dto.User;
import lombok.Getter;
import net.dv8tion.jda.api.events.interaction.ButtonClickEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;


@Getter
public class DiscordEventManager extends ListenerAdapter {

    private final Main plugin;

    public DiscordEventManager(Main plugin) {
        this.plugin = plugin;
        plugin.bot.addEventListener(this);
    }

    @Override
    public void onButtonClick(ButtonClickEvent event) {
        if (event.getComponentId().contains("authorize_authentication_")) {
            String data = event.getComponentId().replace("authorize_authentication_", "");
            String ip = data.split(";")[0];
            String name = data.split(";")[1];

            event.getChannel().sendMessageEmbeds(plugin.jdaConfig.accessGranted.build().build()).queue();
            event.getMessage().delete().queue();
            User user = plugin.databaseManager.getUser(name);

            if (user == null) {
                event.getChannel().sendMessageEmbeds(Main.instance.jdaConfig.invalidUser.build().build()).queue();
                return;
            }

            user.setIP(ip);
        } else if (event.getComponentId().contains("deny_authentication")) {
            event.getChannel().sendMessageEmbeds(plugin.jdaConfig.accessDenied.build().build()).queue();
            event.getMessage().delete().queue();
        }
    }
}
