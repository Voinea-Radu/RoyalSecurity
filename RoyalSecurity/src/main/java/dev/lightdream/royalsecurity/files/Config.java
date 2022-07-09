package dev.lightdream.royalsecurity.files;

import dev.lightdream.messagebuilder.MessageBuilder;
import lombok.NoArgsConstructor;

import java.util.Arrays;
import java.util.List;

@NoArgsConstructor
public class Config {

    public boolean debug = false;
    public boolean multiLobby = false;
    public int codeDigits = 8;
    public List<Long> channels = Arrays.asList(0L, 0L);
    public Long cooldown = 30 * 60 * 1000L;
    public String requiredPermission = ""; //Use '' to disable it and 'none' to force everyone
    public String tebexID = "";
    public Long guildID = 0L;
    public Long roleID = 0L;

    public MessageBuilder banCommand = new MessageBuilder(
            "ban %username% %duration%s %reason%"
    );

    public MessageBuilder unbanCommand = new MessageBuilder(
            "unban %username%"
    );


}
