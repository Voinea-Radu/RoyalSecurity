package dev.lightdream.royalsecurity.dto;

import dev.lightdream.royalsecurity.Main;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;

@AllArgsConstructor
@NoArgsConstructor
public class UserPair {

    public User user;
    public Member member;

    public void pair(String ip) {
        user.setDiscordID(member.getIdLong());
        user.setIP(ip);
        if (Main.instance.databaseManager.getUser(member.getIdLong()).size() == 1) {
            member.modifyNickname(user.name).queue();
            Main.instance.bot.getGuilds().forEach(guild -> Main.instance.config.verifiedRankID.forEach(roleID -> {
                Role role = Main.instance.bot.getRoleById(roleID);
                if (role == null) {
                    return;
                }
                try {
                    guild.addRoleToMember(member, role).queue();
                } catch (Throwable t) {
                    Main.instance.getLogger().warning("Could not change the name of " + member.getEffectiveName() + " on " + guild.getName());
                }
            }));
        }

    }

}
