package dev.lightdream.royalsecurity.database;

import dev.lightdream.api.IAPI;
import dev.lightdream.databasemanager.annotations.database.DatabaseField;
import dev.lightdream.databasemanager.annotations.database.DatabaseTable;
import dev.lightdream.royalsecurity.Main;
import lombok.NoArgsConstructor;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.exceptions.ErrorHandler;
import net.dv8tion.jda.api.requests.ErrorResponse;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@DatabaseTable(table = "users")
@NoArgsConstructor
public class User extends dev.lightdream.api.databases.User {

    @DatabaseField(columnName = "discord_id")
    public Long discordID;
    @DatabaseField(columnName = "ip")
    public String ip;
    @DatabaseField(columnName = "auto_connect")
    public int autoConnect;


    public User(IAPI api, UUID uuid, String name) {
        super(api, uuid, name);
        this.discordID = null;
        this.ip = "";
        this.autoConnect = 0;
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

    public void sendSecure(MessageChannel originChannel, String code, Long discordID) {
        Main.instance.bot.retrieveUserById(discordID)
                .queue(user -> user.openPrivateChannel()
                        .queue(channel -> channel.sendMessageEmbeds(Main.instance.jdaConfig.secure.parse("code", code).build().build())
                                .queue(null,
                                        new ErrorHandler().handle(ErrorResponse.CANNOT_SEND_TO_USER,
                                                e -> originChannel.sendMessageEmbeds(Main.instance.jdaConfig.cannotSendMessage.build().build())
                                                        .queue()))));
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

    public void changeAutoConnect() {
        if(autoConnect==0){
            autoConnect=1;
        }else{
            autoConnect=0;
        }
        save();
    }

    public boolean autoConnect(){
        return autoConnect==1;
    }

}
