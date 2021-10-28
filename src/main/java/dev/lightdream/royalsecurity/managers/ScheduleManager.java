package dev.lightdream.royalsecurity.managers;

import dev.lightdream.royalsecurity.Main;
import dev.lightdream.royalsecurity.dto.User;
import org.bukkit.Bukkit;

import java.util.List;

public class ScheduleManager {

    private final Main plugin;

    public ScheduleManager(Main plugin) {
        this.plugin = plugin;
        if (!plugin.config.multiLobby) {
            registerNicknameChange();
        }
    }

    public void registerNicknameChange() {
        Bukkit.getScheduler().runTaskTimerAsynchronously(plugin, () ->
                plugin.bot.getGuilds().forEach(guild ->
                        guild.getMembers().forEach(member -> {
                            List<User> users = Main.instance.databaseManager.getUser(member.getIdLong());
                            if (users == null || users.size() == 0 ||
                                    member.getEffectiveName().equals(users.get(0).name)) {
                                return;
                            }
                            member.modifyNickname(users.get(0).name).queue();
                        })), 0, Main.instance.config.nicknameChangeInterval * 60 * 20L);
    }

}
