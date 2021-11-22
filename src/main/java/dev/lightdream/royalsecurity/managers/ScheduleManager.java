package dev.lightdream.royalsecurity.managers;

import dev.lightdream.royalsecurity.Main;
import dev.lightdream.royalsecurity.database.User;
import org.bukkit.Bukkit;

import java.util.List;

public class ScheduleManager {

    private final Main plugin;

    @SuppressWarnings("StatementWithEmptyBody")
    public ScheduleManager(Main plugin) {
        this.plugin = plugin;
        if (!plugin.config.multiLobby) {
            //registerNicknameChange();
        }
    }

    @SuppressWarnings("unused")
    @Deprecated
    public void registerNicknameChange() {
        Bukkit.getScheduler().runTaskTimerAsynchronously(plugin, () ->
                plugin.bot.getGuilds().forEach(guild ->
                        guild.getMembers().forEach(member -> {
                            List<User> users = Main.instance.databaseManager.getUser(member.getIdLong());
                            if (users == null || users.size() == 0 ||
                                    member.getEffectiveName().equals(users.get(0).name)) {
                                return;
                            }
                            try {
                                member.modifyNickname(users.get(0).name).queue();
                            } catch (Throwable t) {
                                Main.instance.getLogger().warning("Could not change the name of " + member.getEffectiveName() + " on " + guild.getName());
                            }
                        })), 0, Main.instance.config.nicknameChangeInterval * 60 * 20L);
    }

}
