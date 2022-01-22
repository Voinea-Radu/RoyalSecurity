package dev.lightdream.royalsecurity.database;

import dev.lightdream.api.annotations.database.DatabaseTable;
import dev.lightdream.databasemanager.dto.DatabaseEntry;
import dev.lightdream.royalsecurity.Main;

@DatabaseTable(table = "cooldown")
public class Cooldown extends DatabaseEntry {


    @dev.lightdream.api.annotations.database.DatabaseField(columnName = "ip")
    public String ip;
    @dev.lightdream.api.annotations.database.DatabaseField(columnName = "cooldown")
    public Long cooldown;

    public Cooldown(String ip) {
        super(Main.instance);
        this.ip = ip;
        this.cooldown = System.currentTimeMillis();
        save();
    }

    @SuppressWarnings("unused")
    public Cooldown() {
        super(Main.instance);
    }

    public boolean isValid() {
        return cooldown + Main.instance.config.cooldown > System.currentTimeMillis();
    }

}
