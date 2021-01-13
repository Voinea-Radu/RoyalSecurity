package me.lightdream.royalsecurity;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;

public class DatabaseConnector {
    private static Statement st;
    private static PreparedStatement ps;
    private static Connection con;
    private static ResultSet rs;
    private static String host;
    private static String database;
    private static String user;
    private static String pass;
    private static String table;
    private static String string;
    private static String table1;
    private static int port;

    public DatabaseConnector() {
    }

    public static void sqlSetup() {
        try {
            synchronized(Royalsecurity.getPlugin()) {
                loadStuff();
                if (getCon() != null && !getCon().isClosed()) {
                    return;
                }

                Class.forName("com.mysql.jdbc.Driver");
                setCon(DriverManager.getConnection("jdbc:mysql://" + getHost() + ":" + getPort() + "/?", getUser(), getPass()));
                st = getCon().createStatement();
                st.executeUpdate("USE " + getDatabase());
                Royalsecurity.getPlugin().getServer().getConsoleSender().sendMessage(ChatColor.YELLOW + "[RS SQL] " + ChatColor.GREEN + "DATABASE CONNECTED");
            }
        } catch (SQLException var3) {
            Royalsecurity.getPlugin().getServer().getConsoleSender().sendMessage(ChatColor.YELLOW + "[RS SQL] " + ChatColor.RED + "AN ERROR OCURED WHILE TRYING TO CONNECT TO DATABASE");
            Royalsecurity.getPlugin().getServer().getPluginManager().disablePlugin(Royalsecurity.getPlugin());
        } catch (ClassNotFoundException var4) {
            Royalsecurity.getPlugin().getServer().getConsoleSender().sendMessage(ChatColor.YELLOW + "[RS SQL] " + ChatColor.RED + "JBDC DRIVER NOT FOUND! PLEASE INSTALL IT TO USE THIS PLUGIN");
            Royalsecurity.getPlugin().getServer().getPluginManager().disablePlugin(Royalsecurity.getPlugin());
        }

    }

    public static void sqlRenew() {
        try {
            synchronized(Royalsecurity.getPlugin()) {
                if (getCon() != null && !getCon().isClosed()) {
                    return;
                }

                Class.forName("com.mysql.jdbc.Driver");
                setCon(DriverManager.getConnection("jdbc:mysql://" + getHost() + ":" + getPort() + "/?", getUser(), getPass()));
                st = getCon().createStatement();
                st.executeUpdate("USE " + getDatabase());
                Royalsecurity.getPlugin().getServer().getConsoleSender().sendMessage(ChatColor.YELLOW + "[PinBlock SQL] " + ChatColor.GREEN + "DATABASE CONNECTED");
            }
        } catch (SQLException var3) {
            Royalsecurity.getPlugin().getServer().getConsoleSender().sendMessage(ChatColor.YELLOW + "[PinBlock SQL] " + ChatColor.RED + "AN ERROR OCURED WHILE TRYING TO CONNECT TO DATABASE");
            Royalsecurity.getPlugin().getServer().getPluginManager().disablePlugin(Royalsecurity.getPlugin());
        } catch (ClassNotFoundException var4) {
            Royalsecurity.getPlugin().getServer().getConsoleSender().sendMessage(ChatColor.YELLOW + "[PinBlock SQL] " + ChatColor.RED + "JBDC DRIVER NOT FOUND! PLEASE INSTALL IT TO USE THIS PLUGIN");
            Royalsecurity.getPlugin().getServer().getPluginManager().disablePlugin(Royalsecurity.getPlugin());
        }

    }

    private static void loadStuff() {
        FileConfiguration config = Royalsecurity.getPlugin().getConfig();
        setHost(config.getString("host"));
        setDatabase(config.getString("database"));
        setUser(config.getString("user"));
        setPass(config.getString("password"));
        setPort(config.getInt("port"));
        setTable(config.getString("table"));
        setTable1(config.getString("table-ips"));
    }

    public static Statement getSt() {
        return st;
    }

    public static void setSt(Statement st) {
        DatabaseConnector.st = st;
    }

    public static PreparedStatement getPs() {
        return ps;
    }

    public static void setPs(PreparedStatement ps) {
        DatabaseConnector.ps = ps;
    }

    public static Connection getCon() {
        return con;
    }

    public static void setCon(Connection con) {
        DatabaseConnector.con = con;
    }

    public static ResultSet getRs() {
        return rs;
    }

    public static void setRs(ResultSet rs) {
        DatabaseConnector.rs = rs;
    }

    public static String getHost() {
        return host;
    }

    public static void setHost(String host) {
        DatabaseConnector.host = host;
    }

    public static String getDatabase() {
        return database;
    }

    public static void setDatabase(String database) {
        DatabaseConnector.database = database;
    }

    public static String getUser() {
        return user;
    }

    public static void setUser(String user) {
        DatabaseConnector.user = user;
    }

    public static String getPass() {
        return pass;
    }

    public static void setPass(String pass) {
        DatabaseConnector.pass = pass;
    }

    public static String getTable() {
        return table;
    }

    public static void setTable(String table) {
        DatabaseConnector.table = table;
    }

    public static String getString() {
        return string;
    }

    public static void setString(String string) {
        DatabaseConnector.string = string;
    }

    public static int getPort() {
        return port;
    }

    public static void setPort(int port) {
        DatabaseConnector.port = port;
    }

    public static void setTable1(String table1) {
        DatabaseConnector.table1 = table1;
    }

    public static String getTable1() {
        return table1;
    }
}
