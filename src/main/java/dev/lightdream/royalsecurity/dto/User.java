package dev.lightdream.royalsecurity.dto;

import dev.lightdream.api.IAPI;
import dev.lightdream.libs.j256.field.DatabaseField;
import dev.lightdream.libs.j256.table.DatabaseTable;
import dev.lightdream.royalsecurity.Main;
import lombok.NoArgsConstructor;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.exceptions.ErrorHandler;
import net.dv8tion.jda.api.requests.ErrorResponse;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@DatabaseTable(tableName = "users")
@NoArgsConstructor
public class User extends dev.lightdream.api.databases.User {

    @DatabaseField(columnName = "discord_id")
    public Long discordID;
    @DatabaseField(columnName = "ip")
    public String ip;

    public User(IAPI api, UUID uuid, String name, String lang) {
        super(api, uuid, name, lang);
        this.discordID = null;
        this.ip = "";
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
                        .queue(channel ->
                                Main.instance.jdaConfig.auth
                                        .clone()
                                        .parse("player_name", name)
                                        .parse("ip", ip)
                                        .parse("date", DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss").format(LocalDateTime.now()))
                                        .buildMessageAction(channel).queue()), Throwable::printStackTrace);
    }

    public void sendSecure(MessageChannel originChannel, String code, Long discordID) {
        Main.instance.bot.retrieveUserById(discordID)
                .queue(user -> user.openPrivateChannel()
                        .queue(channel -> channel.sendMessageEmbeds(Main.instance.jdaConfig.secure
                                .parse("code", code)
                                .build().build()).queue(null, new ErrorHandler()
                                .handle(ErrorResponse.CANNOT_SEND_TO_USER, e ->
                                        originChannel.sendMessageEmbeds(Main.instance.jdaConfig.cannotSendMessage.build().build()).queue()))));
    }

    public void setDiscordID(Long id) {
        this.discordID = id;
        save();
    }

    public void setIP(String ip) {
        this.ip = ip;
        save();
    }

    public void unlink() {
        setDiscordID(null);
    }

    @Override
    public void save() {
        save(false);
    }

    @Override
    public String toString() {
        return "User{" +
                "discordID=" + discordID +
                ", ip='" + ip + '\'' +
                ", id=" + id +
                ", uuid=" + uuid +
                ", name='" + name + '\'' +
                ", lang='" + lang + '\'' +
                '}';
    }

    @Deprecated
    public void giveRank() {
        if (discordID == null) {
            return;
        }

        Main.instance.bot.getGuilds().forEach(guild -> {
            Member member = guild.getMemberById(discordID);
            if (member == null) {
                return;
            }

            Main.instance.config.verifiedRankID.forEach(roleID -> {
                Role role = guild.getRoleById(roleID);
                if (role == null) {
                    return;
                }

                guild.addRoleToMember(member, role).queue();
            });
        });
    }

    @Deprecated
    public void changeNickname() {
        if (discordID == null) {
            return;
        }

        Main.instance.bot.getGuilds().forEach(guild -> {
            Member member = guild.getMemberById(discordID);
            if (member == null) {
                return;
            }

            member.modifyNickname(this.name).queue();
        });
    }
}
