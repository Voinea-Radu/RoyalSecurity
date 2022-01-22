package dev.lightdream.royalsecurity.database;

import dev.lightdream.api.annotations.database.DatabaseField;
import dev.lightdream.api.annotations.database.DatabaseTable;
import dev.lightdream.databasemanager.dto.DatabaseEntry;
import dev.lightdream.royalsecurity.Main;

@DatabaseTable(table = "lockdown")
public class Lockdown extends DatabaseEntry {

    @DatabaseField(columnName = "discord_id",
            unique = true)
    public Long discordID;
    @DatabaseField(columnName = "status")
    public boolean status;

    public Lockdown(Long discordID) {
        super(Main.instance);
        this.discordID = discordID;
        this.status = false;
        save();
    }

    @SuppressWarnings("unused")
    public Lockdown() {
        super(Main.instance);
    }

    public void toggle() {
        this.status = !this.status;
        save();
    }


}
