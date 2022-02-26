package dev.lightdream.royalsecurity;

import dev.lightdream.api.LightDreamPlugin;
import dev.lightdream.databasemanager.DatabaseMain;
import dev.lightdream.databasemanager.database.IDatabaseManager;
import dev.lightdream.databasemanager.dto.DriverConfig;
import dev.lightdream.databasemanager.dto.SQLConfig;
import dev.lightdream.filemanager.FileManager;
import dev.lightdream.filemanager.FileManagerMain;
import dev.lightdream.jdaextension.JDAExtensionMain;
import dev.lightdream.jdaextension.dto.JDAConfig;
import dev.lightdream.jdaextension.dto.JdaEmbed;
import dev.lightdream.jdaextension.managers.DiscordCommandManager;
import dev.lightdream.logger.Debugger;
import dev.lightdream.logger.LoggableMain;
import dev.lightdream.logger.Logger;
import dev.lightdream.royalsecurity.commands.discord.*;
import dev.lightdream.royalsecurity.files.Config;
import dev.lightdream.royalsecurity.files.JdaConfig;
import dev.lightdream.royalsecurity.files.Lang;
import dev.lightdream.royalsecurity.managers.SecurityManager;
import dev.lightdream.royalsecurity.managers.*;
import lombok.SneakyThrows;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import javax.security.auth.login.LoginException;
import java.util.Arrays;

public final class Main extends JavaPlugin implements DatabaseMain, LoggableMain, FileManagerMain, JDAExtensionMain {
    public static Main instance;

    //Settings
    public Config config;
    public Lang lang;
    public JdaConfig jdaConfig;
    public SQLConfig sqlConfig;
    public DriverConfig driverConfig;

    //Managers
    public DiscordCommandManager discordCommandManager;
    public SecurityManager securityManager;
    public DatabaseManager databaseManager;
    public DiscordEventManager discordEventManager;
    public MinecraftEventManager minecraftEventManager;
    public FileManager fileManager;

    //JDA
    public JDA bot;

    @SneakyThrows
    @Override
    public void onEnable() {
        instance = this;

        Debugger.init(this);
        Logger.init(this);
        fileManager = new FileManager(this, FileManager.PersistType.YAML);
        loadConfigs();

        try {
            bot = JDABuilder.createDefault(jdaConfig.token).build();
        } catch (LoginException e) {
            Logger.error("The bot token seems to be missing or incorrect, please check if it!");
            return;
        }

        if (!config.multiLobby) {
            this.discordCommandManager = new DiscordCommandManager(this,
                    Arrays.asList(new HelpCommand(),
                            new LinkCommand(),
                            new UnlinkCommand(),
                            new ChangePassword(),
                            new UnregisterCommand(),
                            new StatsCommand(),
                            new AccountsCommand(),
                            new LockdownCommand()));
        }
        this.securityManager = new SecurityManager(this);
        this.databaseManager = new DatabaseManager(this);
        if (!config.multiLobby) {
            this.discordEventManager = new DiscordEventManager(this);
        }
        this.minecraftEventManager = new MinecraftEventManager(this);
    }

    public void loadConfigs() {
        sqlConfig = fileManager.load(SQLConfig.class);
        driverConfig = fileManager.load(DriverConfig.class);
        config = fileManager.load(Config.class);
        lang = fileManager.load(Lang.class);
        jdaConfig = fileManager.load(JdaConfig.class);
    }

    @Override
    public SQLConfig getSqlConfig() {
        return sqlConfig;
    }

    @Override
    public DriverConfig getDriverConfig() {
        return driverConfig;
    }

    @Override
    public IDatabaseManager getDatabaseManager() {
        return databaseManager;
    }

    @Override
    public JDA getBot() {
        return bot;
    }

    @Override
    public JDAConfig getJDAConfig() {
        return jdaConfig;
    }

    @Override
    public dev.lightdream.jdaextension.managers.DiscordCommandManager getDiscordCommandManager() {
        return discordCommandManager;
    }

    @Override
    public String getPrefix() {
        return null;
    }

    @Override
    public JdaEmbed getHelpEmbed() {
        return null;
    }

    @Override
    public boolean debug() {
        return false;
    }

    @Override
    public void log(String s) {

    }
}