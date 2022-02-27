package dev.lightdream.royalsecurity.managers;

import dev.lightdream.databasemanager.DatabaseMain;
import dev.lightdream.databasemanager.database.ProgrammaticHikariDatabaseManager;
import dev.lightdream.databasemanager.dto.QueryConstrains;
import dev.lightdream.royalsecurity.database.Cooldown;
import dev.lightdream.royalsecurity.database.User;
import dev.lightdream.royalsecurity.database.UserPair;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.UUID;

public class DatabaseManager extends ProgrammaticHikariDatabaseManager {


    public DatabaseManager(DatabaseMain main) {
        super(main);
    }

    @Override
    public void setup() {
        setup(User.class);
        setup(UserPair.class);
        setup(Cooldown.class);
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
            user = new User(player.getUniqueId(), player.getName());
        }
        user.save();
        return user;
    }

    public @Nullable User getUser(@NotNull UUID uuid) {
        return get(User.class).query(new QueryConstrains().equals("uuid", uuid))
                .query()
                .stream()
                .findFirst()
                .orElse(null);
    }

    @SuppressWarnings("unused")
    public @Nullable User getUser(@NotNull String name) {
        return get(User.class).query(new QueryConstrains().equals("name", name))
                .query()
                .stream()
                .findFirst()
                .orElse(null);
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
        return get(User.class).query(new QueryConstrains().equals("id", id))
                .query()
                .stream()
                .findFirst()
                .orElse(null);
    }

    @SuppressWarnings("unused")
    public @Nullable User getUser(@NotNull CommandSender sender) {
        if (sender instanceof Player) {
            return getUser((Player) sender);
        }
        return null;
    }

    @SuppressWarnings("unused")
    public List<User> getUser(Long discordID) {
        return get(User.class).query(new QueryConstrains().equals("discord_id", discordID))
                .query();
    }

    public UserPair getUserPair(String code) {
        return get(UserPair.class).query(new QueryConstrains().equals("code", code))
                .query()
                .stream()
                .findFirst()
                .orElse(null);
    }

    public List<Cooldown> getCooldown(String ip) {
        return get(Cooldown.class).query(new QueryConstrains().equals("ip", ip))
                .query();
    }


}
