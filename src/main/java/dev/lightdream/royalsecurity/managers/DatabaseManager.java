package dev.lightdream.royalsecurity.managers;

import dev.lightdream.api.IAPI;
import dev.lightdream.api.dto.LambdaExecutor;
import dev.lightdream.api.managers.database.HikariDatabaseManager;
import dev.lightdream.api.managers.database.IDatabaseManagerImpl;
import dev.lightdream.api.utils.Debugger;
import dev.lightdream.royalsecurity.database.Cooldown;
import dev.lightdream.royalsecurity.database.Lockdown;
import dev.lightdream.royalsecurity.database.User;
import dev.lightdream.royalsecurity.database.UserPair;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.locks.Lock;

public class DatabaseManager extends HikariDatabaseManager implements IDatabaseManagerImpl {

    public DatabaseManager(IAPI api) {
        super(api);
    }

    @Override
    public void setup() {
        setup(User.class);
        setup(UserPair.class);
        setup(Cooldown.class);
        setup(Lockdown.class);
    }

    //Users
    public @NotNull User getUser(@NotNull UUID uuid) {
        User user = get(User.class, new HashMap<String, Object>() {{
            put("uuid", uuid);
        }}).stream().findFirst().orElse(null);
        if (user == null) {
            user = new User(api, uuid, Bukkit.getOfflinePlayer(uuid).getName(), api.getSettings().baseLang);
            user.save();
            return getUser(uuid);
        }
        return user;
    }

    @SuppressWarnings("unused")
    public @Nullable User getUser(@NotNull String name) {
        return get(User.class, new HashMap<String, Object>() {{
            put("name", name);
        }}).stream().findFirst().orElse(null);
    }

    @SuppressWarnings("unused")
    public @NotNull User getUser(@NotNull OfflinePlayer player) {
        return getUser(player.getUniqueId());
    }

    public @NotNull User getUser(@NotNull Player player) {
        return getUser(player.getUniqueId());
    }

    @SuppressWarnings("unused")
    public @Nullable User getUser(int id) {
        return get(User.class, new HashMap<String, Object>() {{
            put("id", id);
        }}).stream().findFirst().orElse(null);
    }

    @SuppressWarnings("unused")
    public @Nullable dev.lightdream.api.databases.User getUser(@NotNull CommandSender sender) {
        if (sender instanceof Player) {
            return getUser((Player) sender);
        }
        return api.getConsoleUser();
    }

    @SuppressWarnings("unused")
    public List<User> getUser(Long discordID) {
        return get(User.class, new HashMap<String, Object>() {{
            put("discord_id", discordID);
        }});
    }

    public UserPair getUserPair(String code) {
        return get(UserPair.class, new HashMap<String, Object>() {{
            put("code", code);
        }}).stream().findFirst().orElse(null);
    }

    public List<Cooldown> getCooldown(String ip){
        return get(Cooldown.class, new HashMap<String, Object>(){{
            put("ip", ip);
        }});
    }

    public @NotNull Lockdown getLockdown(Long discordID){
        Debugger.info(get(Lockdown.class, new HashMap<String, Object>() {{
            put("discord_id", discordID);
        }}));
        return get(Lockdown.class, new HashMap<String, Object>() {{
            put("discord_id", discordID);
        }}).stream().findFirst().orElse(new Lockdown(discordID));
    }


    @Override
    public HashMap<Class<?>, String> getDataTypes() {
        return null;
    }

    @Override
    public HashMap<Class<?>, LambdaExecutor> getSerializeMap() {
        return null;
    }

    @Override
    public HashMap<Class<?>, LambdaExecutor> getDeserializeMap() {
        return null;
    }
}
