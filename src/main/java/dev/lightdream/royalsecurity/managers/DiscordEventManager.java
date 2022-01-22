package dev.lightdream.royalsecurity.managers;

import dev.lightdream.logger.Debugger;
import dev.lightdream.royalsecurity.Main;
import dev.lightdream.royalsecurity.database.Cooldown;
import dev.lightdream.royalsecurity.database.User;
import lombok.Getter;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.interaction.ButtonClickEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.bukkit.Bukkit;


@Getter
public class DiscordEventManager extends ListenerAdapter {

    private final Main plugin;

    public DiscordEventManager(Main plugin) {
        this.plugin = plugin;
        plugin.bot.addEventListener(this);
    }

    @Override
    public void onButtonClick(ButtonClickEvent event) {
        Debugger.info(event.getComponentId());
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
        } else if (event.getComponentId().contains("deny_authentication_")) {
            String data = event.getComponentId().replace("deny_authentication_", "");
            String ip = data.split(";")[0];
            String name = data.split(";")[1];

            event.getMessage().delete().queue();
            User user = plugin.databaseManager.getUser(name);

            if (user == null) {
                event.getChannel().sendMessageEmbeds(Main.instance.jdaConfig.invalidUser.build().build()).queue();
                return;
            }

            event.getChannel().sendMessageEmbeds(plugin.jdaConfig.accessDenied.build().build()).queue();
            new Cooldown(ip);
        }
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        if (Main.instance.config.channels.contains(event.getChannel().getIdLong())) {
            if (event.getAuthor().isBot()) {
                Bukkit.getScheduler().runTaskLater(plugin, () -> event.getMessage().delete().queue(), 10 * 20);
            } else {
                if (event.getMessage().getContentRaw().startsWith("+adminText") && event.getMember().hasPermission(Permission.ADMINISTRATOR)) {
                    return;
                }
                event.getMessage().delete().queue();
            }
        }
    }

}
