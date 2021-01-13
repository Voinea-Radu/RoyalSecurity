package me.lightdream.royalsecurity;

import java.io.File;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import javax.security.auth.login.LoginException;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.requests.RestAction;
import org.apache.commons.lang.RandomStringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public final class Royalsecurity extends JavaPlugin {
    private static Royalsecurity plugin;
    public static JDA bot;
    public static String CommandsChannel;
    public static String AlertsChannel;
    public static HashMap<String, HashMap<String, User>> pendingCodes = new HashMap();
    public static List<String> logedInPlayers = new ArrayList();
    public static List<String> commandWhitelist = new ArrayList(Arrays.asList("l", "login", "reg", "register", "/l", "/login", "/reg", "/register"));
    public static List<String> perms = new ArrayList();
    public static List<Long> discordAdmin = new ArrayList();

    public Royalsecurity() {
    }

    public void onEnable() {
        this.saveDefaultConfig();
        this.getConfig().options().copyDefaults(true);
        plugin = this;
        String token = this.getConfig().getString("token");
        if (token == "") {
            this.getServer().getPluginManager().disablePlugin(this);
            this.getLogger().severe("Va rog sa adaugati un token in config.yml");
        } else {
            try {
                bot = JDABuilder.createDefault(token).build();
                bot.addEventListener(new Object[]{new DiscordListener()});
            } catch (LoginException var3) {
                var3.printStackTrace();
            }
        }
        CommandsChannel = this.getConfig().getString("commands-id");
        AlertsChannel = this.getConfig().getString("alerts-id");
        this.getCommand("gw").setExecutor(new Commands(this));
        this.getServer().getPluginManager().registerEvents(new MinecraftListener(), this);
        DatabaseConnector.sqlSetup();
        BukkitRunnable.renewDatabaseConnection();
        loadAdmins();
    }

    public void onDisable() {
        this.saveDefaultConfig();
        saveAdmins();
    }

    public static Royalsecurity getPlugin() {
        return plugin;
    }

    public static String generateCode(String name, User user) {
        String code = RandomStringUtils.random(getPlugin().getConfig().getInt("code-digits"), true, true);
        addCode(code, name, user);
        return code;
    }

    public static void addSecurity(String name, User user) {
        PreparedStatement statement = null;

        try {
            ResultSet result = DatabaseConnector.getCon().prepareStatement("SELECT COUNT(*) FROM " + DatabaseConnector.getTable() + " WHERE NAME='" + name + "'").executeQuery();
            if (result.next()) {
                if (result.getInt("COUNT(*)") == 0) {
                    statement = DatabaseConnector.getCon().prepareStatement("INSERT INTO " + DatabaseConnector.getTable() + " (DISCORD_ID, NAME) VALUES (?,?)");
                    statement.setLong(1, user.getIdLong());
                    statement.setString(2, name);
                    statement.executeUpdate();
                }
            } else {
                System.out.println("[ERROR] Player already exists");
            }
        } catch (SQLException var4) {
            var4.printStackTrace();
        }

    }

    public static void addCode(String code, String name, User user) {
        HashMap<String, User> hashMap = new HashMap();
        hashMap.put(name, user);
        pendingCodes.put(code, hashMap);
    }

    public static void sendAuthMessage(JDA jda, Long userId, String ip) {
        RestAction<User> action = jda.retrieveUserById(userId);
        action.queue((user) -> {
            user.openPrivateChannel().queue((channel) -> {
                channel.sendMessage(DiscordListener.createEmbed("Te-ai conectat pe server de pe ip-ul: " + ip, 255, 255, 0).build()).queue((message) -> {
                    message.addReaction("✔").queue();
                    message.addReaction("❌").queue();
                });
            });
        }, (error) -> {
            error.printStackTrace();
        });
    }

    public static String getName(Long ID) {
        PreparedStatement statement = null;

        try {
            statement = DatabaseConnector.getCon().prepareStatement("SELECT NAME FROM " + DatabaseConnector.getTable() + " WHERE DISCORD_ID='" + ID + "'");
            ResultSet result = statement.executeQuery();
            result.first();
            return result.getString("NAME");
        } catch (SQLException var3) {
            var3.printStackTrace();
            return null;
        }
    }

    public static Long getID(String name) {
        PreparedStatement statement = null;

        try {
            statement = DatabaseConnector.getCon().prepareStatement("SELECT DISCORD_ID FROM " + DatabaseConnector.getTable() + " WHERE NAME='" + name + "'");
            ResultSet result = statement.executeQuery();
            result.first();
            return result.getLong("DISCORD_ID");
        } catch (SQLException var3) {
            var3.printStackTrace();
            return 0L;
        }
    }

    public static boolean secured(String name) {
        Object var1 = null;

        try {
            ResultSet result = DatabaseConnector.getCon().prepareStatement("SELECT COUNT(*) FROM " + DatabaseConnector.getTable() + " WHERE NAME='" + name + "'").executeQuery();
            if (result.next()) {
                return result.getInt("COUNT(*)") == 1;
            }
        } catch (SQLException var3) {
            var3.printStackTrace();
        }

        return false;
    }

    public static boolean secured(long ID) {
        Object var2 = null;

        try {
            ResultSet result = DatabaseConnector.getCon().prepareStatement("SELECT COUNT(*) FROM " + DatabaseConnector.getTable() + " WHERE DISCORD_ID='" + ID + "'").executeQuery();
            if (result.next()) {
                return result.getInt("COUNT(*)") == 1;
            }
        } catch (SQLException var4) {
            var4.printStackTrace();
        }

        return false;
    }

    public static void removeSecurity(Long ID) {
        Object var1 = null;

        try {
            DatabaseConnector.getCon().prepareStatement("DELETE FROM " + DatabaseConnector.getTable() + " WHERE DISCORD_ID=" + ID).executeUpdate();
        } catch (SQLException var3) {
            var3.printStackTrace();
        }

    }

    public static String getIp(String name) {
        Object var1 = null;

        try {
            ResultSet result = DatabaseConnector.getCon().prepareStatement("SELECT IP FROM " + DatabaseConnector.getTable1() + " WHERE NAME='" + name + "'").executeQuery();
            if (result.next()) {
                return result.getString("IP");
            }
        } catch (SQLException var3) {
            var3.printStackTrace();
        }

        return "0.0.0.0";
    }

    public static void setIp(String name, String IP) {
        PreparedStatement statement = null;

        try {
            statement = DatabaseConnector.getCon().prepareStatement("SELECT COUNT(*) FROM " + DatabaseConnector.getTable1() + " WHERE NAME = '" + name + "'");
            ResultSet result = statement.executeQuery();
            if (result.next()) {
                if (result.getInt("COUNT(*)") == 1) {
                    statement = DatabaseConnector.getCon().prepareStatement("UPDATE " + DatabaseConnector.getTable1() + " SET IP='" + IP + "' WHERE NAME='" + name + "'");
                    statement.executeUpdate();
                } else {
                    System.out.println("INSERT INTO " + DatabaseConnector.getTable1() + " VALUES('" + name + "', '" + IP + "')");
                    statement = DatabaseConnector.getCon().prepareStatement("INSERT INTO " + DatabaseConnector.getTable1() + " VALUES('" + name + "', '" + IP + "')");
                    statement.executeUpdate();
                }
            }
        } catch (SQLException var4) {
            var4.printStackTrace();
        }

    }

    public static String color(String text) {
        return ChatColor.translateAlternateColorCodes('&', text);
    }

    public static void saveAdmins() {
        File file = new File(Bukkit.getServer().getPluginManager().getPlugin("Royalsecurity").getDataFolder(), "minecraft.yml");
        FileConfiguration minecraft = YamlConfiguration.loadConfiguration(file);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException var8) {
                var8.printStackTrace();
            }
        }

        minecraft.set("admins", perms);
        System.out.println("Minecraft admins has been saved");

        try {
            minecraft.save(file);
        } catch (IOException var7) {
            var7.printStackTrace();
        }

        File file2 = new File(Bukkit.getServer().getPluginManager().getPlugin("Royalsecurity").getDataFolder(), "discord.yml");
        FileConfiguration discord = YamlConfiguration.loadConfiguration(file2);
        if (!file.exists()) {
            try {
                file2.createNewFile();
            } catch (IOException var6) {
                var6.printStackTrace();
            }
        }

        discord.set("admins", discordAdmin);
        System.out.println("Discord admins has been saved");

        try {
            discord.save(file2);
        } catch (IOException var5) {
            var5.printStackTrace();
        }

    }

    public static void loadAdmins() {
        File file = new File(Bukkit.getServer().getPluginManager().getPlugin("Royalsecurity").getDataFolder(), "minecraft.yml");
        FileConfiguration minecraft = YamlConfiguration.loadConfiguration(file);
        if (file.exists()) {
            perms = minecraft.getStringList("admins");
            System.out.println("Minecraft admins has been loaded");
        }

        File file2 = new File(Bukkit.getServer().getPluginManager().getPlugin("Royalsecurity").getDataFolder(), "discord.yml");
        FileConfiguration discord = YamlConfiguration.loadConfiguration(file2);
        if (file2.exists()) {
            discordAdmin = discord.getLongList("admins");
            System.out.println("Discord admins has been loaded");
        }

    }
}
