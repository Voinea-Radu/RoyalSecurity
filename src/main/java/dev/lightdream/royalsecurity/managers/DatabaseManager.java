package dev.lightdream.royalsecurity.managers;

import dev.lightdream.api.IAPI;
import dev.lightdream.databasemanager.dto.LambdaExecutor;
import dev.lightdream.royalsecurity.Main;
import dev.lightdream.royalsecurity.database.Cooldown;
import dev.lightdream.royalsecurity.database.Lockdown;
import dev.lightdream.royalsecurity.database.User;
import dev.lightdream.royalsecurity.database.UserPair;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class DatabaseManager extends dev.lightdream.api.managers.DatabaseManager {

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
    public @NotNull User createUser(@NotNull OfflinePlayer player) {
        User user = getUser(player.getUniqueId());
        if (user == null) {
            user = getUser(player.getName());
        }
        if (user != null) {
            user.uuid = player.getUniqueId();
        } else {
            user = new User(Main.instance, player.getUniqueId(), player.getName());
        }
        user.save();
        return user;
    }

    public @Nullable User getUser(@NotNull UUID uuid) {
        return get(User.class, new HashMap<String, Object>() {{
            put("uuid", uuid);
        }}).stream().findFirst().orElse(null);
    }

    @SuppressWarnings("unused")
    public @Nullable User getUser(@NotNull String name) {
        return get(User.class, new HashMap<String, Object>() {{
            put("name", name);
        }}).stream().findFirst().orElse(null);
    }

    @SuppressWarnings("unused")
    public @NotNull User getUser(@NotNull OfflinePlayer player) {
        return createUser(player);
    }

    public @NotNull User getUser(@NotNull Player player) {
        return createUser(player);
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
        return Main.instance.getConsoleUser();
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

    public List<Cooldown> getCooldown(String ip) {
        return get(Cooldown.class, new HashMap<String, Object>() {{
            put("ip", ip);
        }});
    }

    public @NotNull Lockdown getLockdown(Long discordID) {
        Lockdown lockdown = get(Lockdown.class, new HashMap<String, Object>() {{
            put("discord_id", discordID);
        }}).stream().findFirst().orElse(null);
        if (lockdown == null) {
            lockdown = new Lockdown(discordID);
        }
        return lockdown;
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
