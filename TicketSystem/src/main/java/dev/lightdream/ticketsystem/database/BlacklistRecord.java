package dev.lightdream.ticketsystem.database;

import dev.lightdream.databasemanager.annotations.database.DatabaseField;
import dev.lightdream.databasemanager.annotations.database.DatabaseTable;
import dev.lightdream.databasemanager.dto.entry.impl.IntegerDatabaseEntry;
import dev.lightdream.ticketsystem.Main;

@DatabaseTable(table = "blacklist")

public class BlacklistRecord extends IntegerDatabaseEntry {

    @DatabaseField(columnName = "user_id")
    public Long userID;

    public BlacklistRecord(Long userID) {
        super(Main.instance);
        this.userID = userID;
    }

    @SuppressWarnings("unused")
    public BlacklistRecord() {
        super(Main.instance);
    }

}
