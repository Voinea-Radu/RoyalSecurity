package dev.lightdream.royalsecurity.database;

import dev.lightdream.api.annotations.database.DatabaseTable;
import dev.lightdream.libs.j256.field.DatabaseField;
import dev.lightdream.royalsecurity.Main;

@DatabaseTable(table = "cooldown")
public class Cooldown {

    @DatabaseField(columnName = "id", generatedId = true, canBeNull = false)
    @dev.lightdream.api.annotations.database.DatabaseField(columnName = "id", autoGenerate = true)
    public int id;
    @DatabaseField(columnName = "ip")
    @dev.lightdream.api.annotations.database.DatabaseField(columnName = "ip")
    public String ip;
    @DatabaseField(columnName = "cooldown")
    @dev.lightdream.api.annotations.database.DatabaseField(columnName = "cooldown")
    public Long cooldown;

    public Cooldown(String ip, Long cooldown){
        this.ip=ip;
        this.cooldown=cooldown;
    }

    public boolean isValid(){
        return cooldown+ Main.instance.config.cooldown>System.currentTimeMillis();
    }

}
