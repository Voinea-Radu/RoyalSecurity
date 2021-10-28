package dev.lightdream.royalsecurity.managers;

import dev.lightdream.royalsecurity.Main;
import dev.lightdream.royalsecurity.commands.DiscordCommand;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DiscordCommandManager extends ListenerAdapter {

    @SuppressWarnings({"FieldCanBeLocal", "unused"})
    private final Main plugin;
    public List<DiscordCommand> commands;

    public DiscordCommandManager(Main plugin, List<DiscordCommand> commands) {
        this.plugin = plugin;
        this.commands = commands;
        plugin.bot.addEventListener(this);
    }

    public void sendHelp(MessageChannel channel) {
        channel.sendMessageEmbeds(Main.instance.jdaConfig.helpEmbed.build().build()).queue();
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        if (event.getAuthor().isBot()) {
            return;
        }

        String[] message = event.getMessage().getContentRaw().split(" ");
        Member member = event.getMember();

        if (message[0].startsWith("+")) {
            commands.forEach(subCommands -> {
                if (subCommands.aliases.contains(message[0].replace("+", "").toLowerCase())) {
                    if (member != null) {
                        if (subCommands.hasPermission(event.getMember())) {
                            subCommands.execute(event.getMember(), event.getAuthor(), event.getChannel(), new ArrayList<>(Arrays.asList(message).subList(1, message.length)));
                            return;
                        }
                        return;
                    }
                    subCommands.execute(null, event.getAuthor(), event.getChannel(), new ArrayList<>(Arrays.asList(message).subList(1, message.length)));
                }
            });
        }
    }


}
