package dev.lightdream.royalsecurity.database;

import dev.lightdream.databasemanager.dto.DatabaseEntry;
import dev.lightdream.libs.fasterxml.annotation.JsonIgnore;
import dev.lightdream.royalsecurity.Main;

@dev.lightdream.api.annotations.database.DatabaseTable(table = "pairs")
public class UserPair extends DatabaseEntry {

    @dev.lightdream.api.annotations.database.DatabaseField(columnName = "code")
    public String code;
    @dev.lightdream.api.annotations.database.DatabaseField(columnName = "userID")
    public Integer userID;
    @dev.lightdream.api.annotations.database.DatabaseField(columnName = "memberID")
    public Long memberID;

    public UserPair(String code, User user, Long memberID) {
        super(Main.instance);
        this.code = code;
        this.userID = user.id;
        this.memberID = memberID;

        save();
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
