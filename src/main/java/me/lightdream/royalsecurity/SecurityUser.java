package me.lightdream.royalsecurity;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@DatabaseTable(tableName = "security_users")
public class SecurityUser {

    @DatabaseField(columnName = "UUID")
    public String UUID;
    @DatabaseField(columnName = "discord_id")
    public Long discordID;
    @DatabaseField(columnName = "ip")
    public String ip;

    public SecurityUser(String UUID, Long discordID, String ip){
        this.UUID = UUID;
        this.discordID = discordID;
        this.ip = ip;
    }
}
