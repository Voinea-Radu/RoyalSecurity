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
            Main.instance.bot.getGuilds().forEach(guild -> {
                Role role = Main.instance.bot.getRoleById(Main.instance.config.verifiedRankID);
                if (role == null) {
                    Main.instance.getLogger().severe("The provided verified rank ID is not valid");
                    return;
                }
                guild.addRoleToMember(member, role).queue();
            });
        }

    }

}
