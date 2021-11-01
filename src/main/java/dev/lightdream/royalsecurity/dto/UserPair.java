package dev.lightdream.royalsecurity.dto;

import dev.lightdream.api.databases.EditableDatabaseEntry;
import dev.lightdream.libs.j256.field.DatabaseField;
import dev.lightdream.libs.j256.table.DatabaseTable;
import dev.lightdream.royalsecurity.Main;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;

@DatabaseTable(tableName = "pairs")
public class UserPair extends EditableDatabaseEntry {

    @DatabaseField(columnName = "id", generatedId = true, canBeNull = false)
    public int id;
    @DatabaseField(columnName = "code")
    public String code;
    @DatabaseField(columnName = "user", foreign = true)
    public User user;
    @DatabaseField(columnName = "memberID")
    public Long memberID;

    public UserPair(String code, User user, Long memberID) {
        super(Main.instance);
        this.code = code;
        this.user = user;
        this.memberID = memberID;

        save();
    }

    @SuppressWarnings("unused")
    public UserPair() {
        super(Main.instance);
    }

    public void pair(String ip) {

        try {
            user.setDiscordID(memberID);
            user.setIP(ip);

            if (Main.instance.databaseManager.getUser(memberID).size() == 1) {
                Main.instance.bot.getGuilds().forEach(guild -> {
                    Member member = guild.getMemberById(memberID);
                    if (member == null) {
                        return;
                    }
                    member.modifyNickname(user.name).queue();
                    Main.instance.config.verifiedRankID.forEach(roleID -> {
                        Role role = Main.instance.bot.getRoleById(roleID);
                        if (role == null) {
                            return;
                        }
                        guild.addRoleToMember(member, role).queue();
                    });
                });
            }
        } catch (Throwable t) {
            Main.instance.getLogger().info("An error has occurred");
            //Main.instance.getLogger().warning("Could not change the name/role of " + member.get().getEffectiveName());
        }
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
