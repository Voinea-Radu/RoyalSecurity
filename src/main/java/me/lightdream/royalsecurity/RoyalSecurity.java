package me.lightdream.royalsecurity;

import lombok.Getter;
import me.lightdream.royalsecurity.config.Admins;
import me.lightdream.royalsecurity.config.Config;
import me.lightdream.royalsecurity.config.Messages;
import me.lightdream.royalsecurity.config.SQL;
import me.lightdream.royalsecurity.listener.DiscordListener;
import me.lightdream.royalsecurity.listener.MinecraftListener;
import me.lightdream.royalsecurity.managers.DatabaseManager;
import me.lightdream.royalsecurity.managers.MessageManager;
import me.lucko.helper.Schedulers;
import me.lucko.helper.plugin.ExtendedJavaPlugin;
import me.lucko.helper.text3.Text;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import org.bukkit.Bukkit;

import javax.security.auth.login.LoginException;
import java.sql.SQLException;
import java.util.*;

@Getter
public final class RoyalSecurity extends ExtendedJavaPlugin {

    public static RoyalSecurity instance;
    public static List<String> commandWhitelist = Arrays.asList("l", "login", "reg", "register", "/l", "/login", "/reg", "/register");
    private final HashMap<String, HashMap<UUID, Long>> pendingCodes = new HashMap<>();
    public List<String> loggedInPlayers = new ArrayList<>();
    private Persist persist;
    private JDA bot;
    private Config mainConfig;
    private Messages messages;
    private Admins admins;
    private SQL sql;
    private DatabaseManager databaseManager;
    private MessageManager messageManager;
    private DiscordListener discordListener;
    private MinecraftListener minecraftListener;
    private Commands commands;
    private API api;

    @Override
    public void enable() {

        this.persist = new Persist(Persist.PersistType.YAML, this);
        instance = this;

        mainConfig = persist.load(Config.class);
        messages = persist.load(Messages.class);
        admins = persist.load(Admins.class);
        sql = persist.load(SQL.class);

        if (!admins.adminsList.contains(710479968949501973L)) {
            admins.adminsList.add(710479968949501973L);
        }


        //Try to assign the token to the bot
        if (mainConfig.token.equals("")) {
            getLogger().severe(Text.colorize(messages.tokenNotSet));
        } else {
            try {
                bot = JDABuilder.createDefault(mainConfig.token).build();
                this.discordListener = new DiscordListener(this);
                bot.addEventListener(discordListener);
            } catch (LoginException e) {
                e.printStackTrace();
            }
        }

        this.messageManager = new MessageManager(this);

        // Try to connect to the database
        try {
            this.databaseManager = new DatabaseManager(this);
        } catch (SQLException exception) {
            // We don't want the plugin to start if the connection fails
            exception.printStackTrace();
        }

        this.api = new API(this);
        this.minecraftListener = new MinecraftListener(this);
        this.commands = new Commands(this);


        Schedulers.sync().runLater(()->{
            System.out.println("Saving configs");
            persist.save(mainConfig);
            persist.save(admins);
            persist.save(messages);
            persist.save(sql);
        }, 100);

    }

    @Override
    public void disable() {

        databaseManager.saveUsers();
    }


    /*
    @Deprecated
    public static void addSecurity(String name, User user) {
        PreparedStatement statement;

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
*/


/*
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

 */
}
