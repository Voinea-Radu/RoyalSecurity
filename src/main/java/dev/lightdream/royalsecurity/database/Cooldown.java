package dev.lightdream.royalsecurity.database;

import dev.lightdream.databasemanager.annotations.database.DatabaseField;
import dev.lightdream.databasemanager.annotations.database.DatabaseTable;
import dev.lightdream.databasemanager.dto.entry.impl.StringDatabaseEntry;
import dev.lightdream.royalsecurity.Main;

@DatabaseTable(table = "cooldown")
public class Cooldown extends StringDatabaseEntry {


    @DatabaseField(columnName = "ip")
    public String ip;
    @DatabaseField(columnName = "cooldown")
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
