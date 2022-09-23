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
import dev.lightdream.jdaextension.commands.commands.StatsCommand;
import dev.lightdream.jdaextension.dto.JDAConfig;
import dev.lightdream.jdaextension.managers.DiscordCommandManager;
import dev.lightdream.logger.LoggableMain;
import dev.lightdream.logger.Logger;
import dev.lightdream.messagebuilder.MessageBuilderManager;
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
import net.dv8tion.jda.api.requests.GatewayIntent;
import org.bukkit.plugin.java.JavaPlugin;

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

    @SuppressWarnings("ArraysAsListWithZeroOrOneArgument")
    @SneakyThrows
    @Override
    public void onEnable() {
        instance = this;

        Logger.init(this);
        fileManager = new FileManager(this);
        MessageBuilderManager.init(fileManager);
        loadConfigs();

        bot = JDAExtensionMain.generateBot(this, jdaConfig.token, Arrays.asList(
                GatewayIntent.GUILD_MEMBERS
        ));

        if (bot == null) {
            Logger.error("Bot has not been created. Exiting!");
            return;
        }

        if (!config.multiLobby) {
            this.discordCommandManager = new DiscordCommandManager(this,
                    Arrays.asList(
                            new LinkCommand(),
                            new UnlinkCommand(),
                            new ChangePasswordCommand(),
                            new UnregisterCommand(),
                            new PaymentsCommand(),
                            new StatsCommand(this),
                            new AccountsCommand()
                    )
            );
        }
        this.securityManager = new SecurityManager(this);
        this.databaseManager = new DatabaseManager(this);
        if (!config.multiLobby) {
            this.discordEventManager = new DiscordEventManager(this);
        }
        this.minecraftEventManager = new MinecraftEventManager(this);

        commandManager = new CommandManager(this);

        if (!config.multiLobby) {
            new dev.lightdream.ticketsystem.Main().onEnable(new SecurityImpl());
        }
    }

    @Override
    public CommandLang getLang() {
        return lang;
    }

    public void loadConfigs() {
        sqlConfig = fileManager.load(SQLConfig.class);
        driverConfig = fileManager.load(DriverConfig.class);
        config = fileManager.load(Config.class);
        lang = fileManager.load(Lang.class);
        jdaConfig = fileManager.load(JdaConfig.class);
    }

    @Override
    public CommandManager getCommandManager() {
        return commandManager;
    }

    @Override
    public String getPackageName() {
        return "dev.lightdream.royalsecurity";
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
    public String getVersion() {
        return "1.22";
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
    public boolean debug() {
        return config.debug;
    }

    @Override
    public void log(String s) {
        System.out.println(s);
    }
}