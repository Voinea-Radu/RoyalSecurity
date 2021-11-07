package dev.lightdream.royalsecurity.managers;

import dev.lightdream.api.IAPI;
import dev.lightdream.royalsecurity.dto.User;
import dev.lightdream.royalsecurity.dto.UserPair;
import lombok.SneakyThrows;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

public class DatabaseManager extends dev.lightdream.api.managers.DatabaseManager {

    public DatabaseManager(IAPI api) {
        super(api);
    }

    @Override
    public void setup() {
        setup(User.class);
        setup(UserPair.class);
    }

    @Override
    public <T> List<T> getAll(Class<T> clazz) {
        return getAll(clazz, false);
    }

    @SneakyThrows
    @Deprecated
    public UserPair getUserPairRaw(String code) {
        String[] results = getDao(UserPair.class).queryRaw("SELECT * FROM pairs WHERE code=\"" + code + "\"").getFirstResult();
        if (results == null) {
            return null;
        }
        int id = Integer.parseInt(results[0]);
        Integer userID = Integer.parseInt(results[2]);
        Long discordID = Long.parseLong(results[3]);
        return new UserPair(id, code, userID, discordID);
    }

    //Users
    public @NotNull User getUser(@NotNull UUID uuid) {
        Optional<User> optionalUser = getAll(User.class).stream().filter(user -> user.uuid.equals(uuid)).findFirst();

        if (optionalUser.isPresent()) {
            return optionalUser.get();
        }

        User user = new User(api, uuid, Bukkit.getOfflinePlayer(uuid).getName(), api.getSettings().baseLang);
        user.save();
        return user;
    }

    @SuppressWarnings("unused")
    public @Nullable User getUser(@NotNull String name) {
        Optional<User> optionalUser = getAll(User.class).stream().filter(user -> user.name.equals(name)).findFirst();

        return optionalUser.orElse(null);
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
        return getAll(User.class).stream().filter(user -> user.id == id).findFirst().orElse(null);
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
        return getAll(User.class).stream().filter(user -> {
            if (user.discordID == null) {
                return false;
            }
            return user.discordID.equals(discordID);
        }).collect(Collectors.toList());
    }

    public UserPair getUserPair(String code) {
        return getAll(UserPair.class).stream().filter(userPair -> userPair.code.equals(code)).findFirst().orElse(null);
    }


}
