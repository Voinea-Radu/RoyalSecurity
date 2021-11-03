package dev.lightdream.royalsecurity.dto;

import dev.lightdream.api.databases.EditableDatabaseEntry;
import dev.lightdream.libs.fasterxml.annotation.JsonIgnore;
import dev.lightdream.libs.j256.field.DatabaseField;
import dev.lightdream.libs.j256.table.DatabaseTable;
import dev.lightdream.royalsecurity.Main;

@DatabaseTable(tableName = "pairs")
public class UserPair extends EditableDatabaseEntry {

    @DatabaseField(columnName = "id", generatedId = true, canBeNull = false)
    public int id;
    @DatabaseField(columnName = "code")
    public String code;
    @DatabaseField(columnName = "userID")
    public Integer userID;
    @DatabaseField(columnName = "memberID")
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

    @Override
    public Integer getID() {
        return id;
    }

    @Override
    public void save() {
        save(false);
    }
}
