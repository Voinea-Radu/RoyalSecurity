package dev.lightdream.royalsecurity;

import dev.lightdream.commandmanager.CommandMain;
import dev.lightdream.commandmanager.dto.CommandLang;
import dev.lightdream.commandmanager.manager.CommandManager;
import dev.lightdream.databasemanager.DatabaseMain;
import dev.lightdream.databasemanager.database.IDatabaseManager;
import dev.lightdream.databasemanager.dto.DriverConfig;
import dev.lightdream.databasemanager.dto.SQLConfig;
import dev.lightdream.filemanager.FileManager;
import dev.lightdream.filemanager.FileManagerMain;
import dev.lightdream.jdaextension.JDAExtensionMain;
import dev.lightdream.jdaextension.commands.commands.HelpCommand;
import dev.lightdream.jdaextension.commands.commands.StatsCommand;
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
import dev.lightdream.royalsecurity.managers.DatabaseManager;
import dev.lightdream.royalsecurity.managers.DiscordEventManager;
import dev.lightdream.royalsecurity.managers.MinecraftEventManager;
import dev.lightdream.royalsecurity.managers.SecurityManager;
import lombok.SneakyThrows;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import org.bukkit.plugin.java.JavaPlugin;

import javax.security.auth.login.LoginException;
import java.util.Arrays;

public final class Main extends JavaPlugin implements DatabaseMain, LoggableMain, FileManagerMain, JDAExtensionMain, CommandMain {
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
    public CommandManager commandManager;

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
                    Arrays.asList(new HelpCommand(this),
                            new LinkCommand(),
                            new UnlinkCommand(),
                            new ChangePassword(),
                            new UnregisterCommand(),
                            new StatsCommand(this),
                            new AccountsCommand(),
                            new LockdownCommand()));
        }
        this.securityManager = new SecurityManager(this);
        this.databaseManager = new DatabaseManager(this);
        if (!config.multiLobby) {
            this.discordEventManager = new DiscordEventManager(this);
        }
        this.minecraftEventManager = new MinecraftEventManager(this);

        commandManager = new CommandManager(this, "dev.lightdream.royalsecurity");
    }

    @Override
    public CommandLang getLang() {
        return null;
    }

    public void loadConfigs() {
        sqlConfig = fileManager.load(SQLConfig.class);
        driverConfig = fileManager.load(DriverConfig.class);
        config = fileManager.load(Config.class);
        lang = fileManager.load(Lang.class);
        jdaConfig = fileManager.load(JdaConfig.class);
    }

    @Override
    public String getProjectName() {
        return "Royal Security";
    }

    @Override
    public String getProjectVersion() {
        return "1.21";
    }

    @Override
    public CommandManager getCommandManager() {
        return commandManager;
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
    public JdaEmbed getHelpEmbed() {
        return jdaConfig.helpEmbed;
    }

    @Override
    public boolean debug() {
        return config.debug;
    }

    @Override
    public void log(String s) {
        System.out.println(s);
    }
}