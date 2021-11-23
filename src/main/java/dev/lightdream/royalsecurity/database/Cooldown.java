package dev.lightdream.royalsecurity.database;

import dev.lightdream.api.annotations.database.DatabaseTable;
import dev.lightdream.api.databases.DatabaseEntry;
import dev.lightdream.libs.j256.field.DatabaseField;
import dev.lightdream.royalsecurity.Main;

@DatabaseTable(table = "cooldown")
public class Cooldown extends DatabaseEntry {


    @DatabaseField(columnName = "ip")
    @dev.lightdream.api.annotations.database.DatabaseField(columnName = "ip")
    public String ip;
    @DatabaseField(columnName = "cooldown")
    @dev.lightdream.api.annotations.database.DatabaseField(columnName = "cooldown")
    public Long cooldown;

    public Cooldown(String ip, Long cooldown){
        super(Main.instance);
        this.ip=ip;
        this.cooldown=cooldown;
    }

    @SuppressWarnings("unused")
    public Cooldown() {
        super(Main.instance);
    }

    public boolean isValid(){
        return cooldown+ Main.instance.config.cooldown>System.currentTimeMillis();
    }

}
