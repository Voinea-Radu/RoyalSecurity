package dev.lightdream.royalsecurity.database;

import dev.lightdream.databasemanager.annotations.database.DatabaseField;
import dev.lightdream.databasemanager.annotations.database.DatabaseTable;
import dev.lightdream.databasemanager.dto.entry.impl.IntegerDatabaseEntry;
import dev.lightdream.jdaextension.dto.context.CommandContext;
import dev.lightdream.logger.Logger;
import dev.lightdream.royalsecurity.Main;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.exceptions.ErrorHandler;
import net.dv8tion.jda.api.requests.ErrorResponse;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@DatabaseTable(table = "users")
public class User extends IntegerDatabaseEntry {

    @DatabaseField(columnName = "uuid", unique = true)
    public UUID uuid;
    @DatabaseField(columnName = "name", unique = true)
    public String name;
    @DatabaseField(columnName = "discord_id")
    public Long discordID;
    @DatabaseField(columnName = "ip")
    public String ip;
    @DatabaseField(columnName = "auto_connect")
    public boolean autoConnect;


    public User(UUID uuid, String name) {
        super(Main.instance);
        this.name = name;
        this.uuid = uuid;
        this.discordID = null;
        this.ip = "";
        this.autoConnect = false;
    }

    public User() {
        super(Main.instance);
    }

    public boolean hasSecurity() {
        return discordID != null;
    }

    public void sendAuth(String ip) {
        if (discordID == null) {
            return;
        }

        Main.instance.bot.retrieveUserById(discordID)
                .queue(user -> user.openPrivateChannel()
                        .queue(channel -> Main.instance.jdaConfig.auth.clone()
                                .parse("player_name", name)
                                .parse("ip", ip)
                                .parse("date", DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss").format(LocalDateTime.now()))
                                .buildMessageAction(channel)
                                .queue()), Throwable::printStackTrace);
    }

    public void sendSecure(CommandContext context, String code, Long discordID, boolean privateResponse) {
        Main.instance.bot.retrieveUserById(discordID)
                .queue(user -> user.openPrivateChannel()
                        .queue(channel -> {
                            context.sendMessage(Main.instance.jdaConfig.codeSent, privateResponse);
                            channel.sendMessageEmbeds(Main.instance.jdaConfig.secure.parse("code", code).build().build())
                                    .queue(null,
                                            new ErrorHandler().handle(ErrorResponse.CANNOT_SEND_TO_USER,
                                                    e -> context.sendMessage(Main.instance.jdaConfig.cannotSendMessage, privateResponse)
                                            ));
                        }));

    }

    public void setDiscordID(Long id) {
        Long pastID = this.discordID;
        this.discordID = id;
        save();

        Guild guild = Main.instance.bot.getGuildById(Main.instance.config.guildID);

        if (guild == null) {
            Logger.error("Guild not found!");
            return;
        }

        Role role = Main.instance.bot.getRoleById(Main.instance.config.roleID);
        if (role == null) {
            Logger.error("Role not found!");
            return;
        }


        if (id == null) {
            if (Main.instance.databaseManager.getUser(id).size() == 0) {
                Main.instance.bot.retrieveUserById(pastID).queue(member -> {
                    guild.removeRoleFromMember(member, role).queue();
                });
            }

            return;
        }

        Main.instance.bot.retrieveUserById(id).queue(member -> {
            guild.addRoleToMember(member, role).queue();
        });
    }

    public void setIP(String ip) {
        this.ip = ip;
        save();
    }

    public void unlink() {
        setDiscordID(null);
    }

    public void changeAutoConnect() {
        autoConnect = !autoConnect;
        save();
    }

    public boolean autoConnect() {
        return autoConnect;
    }

}
