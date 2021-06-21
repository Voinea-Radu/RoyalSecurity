package me.lightdream.royalsecurity.listener;

import lombok.Getter;
import me.lightdream.royalsecurity.RoyalSecurity;
import me.lightdream.royalsecurity.SecurityUser;
import me.lightdream.royalsecurity.Utils;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.events.interaction.ButtonClickEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.bukkit.Bukkit;

import java.util.UUID;


@Getter
public class DiscordListener extends ListenerAdapter {

    private final RoyalSecurity plugin;
    public JDA instance;

    public DiscordListener(RoyalSecurity plugin) {
        this.plugin = plugin;
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        instance = event.getJDA();

        String[] message = event.getMessage().getContentRaw().split(" ");
        Long authorID = event.getAuthor().getIdLong();
        UUID uuid;
        String response;

        if (!event.getAuthor().isBot()) {
            switch (message[0]) {
                case "+help":
                    if (plugin.getAdmins().adminsList.contains(authorID)) {
                        event.getChannel().sendMessageEmbeds(Utils.createEmbed(plugin.getMessages().helpCommandAdmin, 255, 255, 0).build()).queue();
                    } else {
                        event.getChannel().sendMessageEmbeds(Utils.createEmbed(plugin.getMessages().helpCommandDefault, 255, 255, 0).build()).queue();
                    }
                case "+link":
                    switch (message.length) {
                        case 2:
                            uuid = Utils.getUUID(message[1]);
                            if (uuid == null) {
                                event.getChannel().sendMessageEmbeds(Utils.createEmbed(plugin.getMessages().nullUUID, 255, 0, 0).build()).queue();
                                break;
                            }
                            response = plugin.getApi().generateCode(uuid, authorID);
                            if (!response.equals("")) {
                                event.getChannel().sendMessageEmbeds(Utils.createEmbed(response, 255, 0, 0).build()).queue();
                            } else {
                                event.getChannel().sendMessageEmbeds(Utils.createEmbed(plugin.getMessages().linkCodeSent, 0, 255, 0).build()).queue();
                            }
                            break;
                        case 3:
                            if (!plugin.getAdmins().adminsList.contains(authorID)) {
                                event.getChannel().sendMessageEmbeds(Utils.createEmbed(plugin.getMessages().notAdmin, 255, 0, 0).build()).queue();
                                break;
                            }
                            uuid = Utils.getUUID(message[1]);
                            if (uuid == null) {
                                event.getChannel().sendMessageEmbeds(Utils.createEmbed(plugin.getMessages().nullUUID, 255, 0, 0).build()).queue();
                                break;
                            }

                            Long id = null;

                            try {
                                Long.parseLong(message[2]);
                            } catch (NumberFormatException e) {
                                event.getChannel().sendMessageEmbeds(Utils.createEmbed(plugin.getMessages().invalidDiscordID, 255, 0, 0).build()).queue();
                                break;
                            }

                            plugin.getApi().link(uuid, id);

                            if (plugin.getApi().getSecurityUser(uuid) != null) {
                                event.getChannel().sendMessageEmbeds(Utils.createEmbed(plugin.getMessages().minecraftAccountAlreadyRegistered, 255, 0, 0).build()).queue();
                                break;
                            }
                            if (plugin.getApi().getSecurityUser(id) != null) {
                                event.getChannel().sendMessageEmbeds(Utils.createEmbed(plugin.getMessages().discordAccountAlreadyRegistered, 255, 0, 0).build()).queue();
                                break;
                            }
                            break;
                        default:
                            if (plugin.getAdmins().adminsList.contains(authorID)) {
                                event.getChannel().sendMessageEmbeds(Utils.createEmbed(plugin.getMessages().helpCommandAdmin, 255, 255, 0).build()).queue();
                            } else {
                                event.getChannel().sendMessageEmbeds(Utils.createEmbed(plugin.getMessages().helpCommandDefault, 255, 255, 0).build()).queue();
                            }
                            break;

                    }
                    break;
                case "+unlink":
                    switch (message.length) {
                        case 1:
                            response = plugin.getApi().unlink(authorID);
                            if (!response.equals("")) {
                                event.getChannel().sendMessageEmbeds(Utils.createEmbed(response, 255, 0, 0).build()).queue();
                            } else {
                                event.getChannel().sendMessageEmbeds(Utils.createEmbed(plugin.getMessages().unlinkSuccessful, 0, 255, 0).build()).queue();
                            }
                            break;
                        case 2:
                            if (!plugin.getAdmins().adminsList.contains(authorID)) {
                                event.getChannel().sendMessageEmbeds(Utils.createEmbed(plugin.getMessages().notAdmin, 255, 0, 0).build()).queue();
                                break;
                            }
                            if (event.getMessage().getMentionedUsers().size() != 0) {
                                Long mentionID = event.getMessage().getMentionedUsers().get(0).getIdLong();
                                response = plugin.getApi().unlink(mentionID);
                                if (!response.equals("")) {
                                    event.getChannel().sendMessageEmbeds(Utils.createEmbed(response, 255, 0, 0).build()).queue();
                                } else {
                                    event.getChannel().sendMessageEmbeds(Utils.createEmbed(plugin.getMessages().unlinkSuccessful, 0, 255, 0).build()).queue();
                                }
                                break;
                            }
                            try {
                                Long mentionID = Long.parseLong(message[1]);
                                response = plugin.getApi().unlink(mentionID);
                                if (!response.equals("")) {
                                    event.getChannel().sendMessageEmbeds(Utils.createEmbed(response, 255, 0, 0).build()).queue();
                                } else {
                                    event.getChannel().sendMessageEmbeds(Utils.createEmbed(plugin.getMessages().unlinkSuccessful, 0, 255, 0).build()).queue();
                                }
                                break;
                            } catch (NumberFormatException e) {
                                response = plugin.getApi().unlink(message[1]);
                                if (!response.equals("")) {
                                    event.getChannel().sendMessageEmbeds(Utils.createEmbed(response, 255, 0, 0).build()).queue();
                                } else {
                                    event.getChannel().sendMessageEmbeds(Utils.createEmbed(plugin.getMessages().unlinkSuccessful, 0, 255, 0).build()).queue();
                                }
                                break;
                            }
                        default:
                            if (plugin.getAdmins().adminsList.contains(authorID)) {
                                event.getChannel().sendMessageEmbeds(Utils.createEmbed(plugin.getMessages().helpCommandAdmin, 255, 255, 0).build()).queue();
                            } else {
                                event.getChannel().sendMessageEmbeds(Utils.createEmbed(plugin.getMessages().helpCommandDefault, 255, 255, 0).build()).queue();
                            }
                            break;
                    }
                    break;
                case "+admin":
                    if (!plugin.getAdmins().adminsList.contains(authorID)) {
                        event.getChannel().sendMessageEmbeds(Utils.createEmbed(plugin.getMessages().notAdmin, 255, 0, 0).build()).queue();
                        break;
                    }
                    if (message.length != 3) {
                        if (plugin.getAdmins().adminsList.contains(authorID)) {
                            event.getChannel().sendMessageEmbeds(Utils.createEmbed(plugin.getMessages().helpCommandAdmin, 255, 255, 0).build()).queue();
                        } else {
                            event.getChannel().sendMessageEmbeds(Utils.createEmbed(plugin.getMessages().helpCommandDefault, 255, 255, 0).build()).queue();
                        }
                        break;
                    }
                    Long mentionID = null;

                    if (event.getMessage().getMentionedUsers().size() != 0) {
                        mentionID = event.getMessage().getMentionedUsers().get(0).getIdLong();
                    } else {
                        try {
                            mentionID = Long.parseLong(message[1]);
                        } catch (NumberFormatException e) {
                            event.getChannel().sendMessageEmbeds(Utils.createEmbed(plugin.getMessages().invalidDiscordID, 255, 0, 0).build()).queue();
                            break;
                        }
                    }
                    if (message[1].equalsIgnoreCase("add")) {
                        plugin.getAdmins().adminsList.add(mentionID);
                        event.getChannel().sendMessageEmbeds(Utils.createEmbed(plugin.getMessages().adminAdded, 0, 255, 0).build()).queue();
                        break;
                    }
                    if (message[1].equalsIgnoreCase("remove")) {
                        plugin.getAdmins().adminsList.remove(mentionID);
                        event.getChannel().sendMessageEmbeds(Utils.createEmbed(plugin.getMessages().adminRemoved, 0, 255, 0).build()).queue();
                        break;
                    }


            }
        }
    }

    @Override
    public void onButtonClick(ButtonClickEvent event) {
        if (event.getComponentId().equals("authorize_authentication")) {
            event.getChannel().sendMessageEmbeds(Utils.createEmbed("Access Granted", 0, 255, 0).build()).queue();
            SecurityUser user = plugin.getApi().getSecurityUser(event.getUser().getIdLong());
            user.ip = Bukkit.getPlayer(UUID.fromString(user.UUID)).getAddress().getHostName();
            plugin.loggedInPlayers.add(user.UUID);
        } else if (event.getComponentId().equals("deny_authentication")) {
            event.getChannel().sendMessageEmbeds(Utils.createEmbed("Access Denied", 0, 255, 0).build()).queue();
        }
    }

}
