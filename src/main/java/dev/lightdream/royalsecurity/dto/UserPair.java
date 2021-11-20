package dev.lightdream.royalsecurity.dto;

import dev.lightdream.api.databases.DatabaseEntry;
import dev.lightdream.libs.fasterxml.annotation.JsonIgnore;
import dev.lightdream.libs.j256.field.DatabaseField;
import dev.lightdream.libs.j256.table.DatabaseTable;
import dev.lightdream.royalsecurity.Main;

@DatabaseTable(tableName = "pairs")
@dev.lightdream.api.annotations.DatabaseTable(table = "pairs")
public class UserPair extends DatabaseEntry {

    @DatabaseField(columnName = "id", generatedId = true, canBeNull = false)
    @dev.lightdream.api.annotations.DatabaseField(columnName = "id", autoGenerate = true)
    public int id;
    @DatabaseField(columnName = "code")
    @dev.lightdream.api.annotations.DatabaseField(columnName = "code")
    public String code;
    @DatabaseField(columnName = "userID")
    @dev.lightdream.api.annotations.DatabaseField(columnName = "userID")
    public Integer userID;
    @DatabaseField(columnName = "memberID")
    @dev.lightdream.api.annotations.DatabaseField(columnName = "memberID")
    public Long memberID;

    public UserPair(String code, User user, Long memberID) {
        super(Main.instance);
        this.code = code;
        this.userID = user.id;
        this.memberID = memberID;

        save();
    }

    public UserPair(int id, String code, Integer userID, Long memberID) {
        super(Main.instance);
        this.id = id;
        this.code = code;
        this.userID = userID;
        this.memberID = memberID;
    }

    @SuppressWarnings("unused")
    public UserPair() {
        super(Main.instance);
    }

    @JsonIgnore
    public User getUser() {
        return Main.instance.databaseManager.getUser(userID);
    }

    public void pair(String ip) {
        User user = getUser();

        if (user == null) {
            return;
        }

        user.setDiscordID(memberID);
        user.setIP(ip);
        delete();
    }
}
