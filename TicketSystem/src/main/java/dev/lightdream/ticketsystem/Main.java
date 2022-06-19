package dev.lightdream.ticketsystem;

import dev.lightdream.databasemanager.DatabaseMain;
import dev.lightdream.databasemanager.database.IDatabaseManager;
import dev.lightdream.databasemanager.dto.DriverConfig;
import dev.lightdream.databasemanager.dto.SQLConfig;
import dev.lightdream.filemanager.FileManager;
import dev.lightdream.filemanager.FileManagerMain;
import dev.lightdream.jdaextension.JDAExtensionMain;
import dev.lightdream.jdaextension.commands.commands.StatsCommand;
import dev.lightdream.jdaextension.managers.DiscordCommandManager;
import dev.lightdream.logger.Debugger;
import dev.lightdream.logger.LoggableMain;
import dev.lightdream.logger.Logger;
import dev.lightdream.ticketsystem.commands.*;
import dev.lightdream.ticketsystem.dto.Config;
import dev.lightdream.ticketsystem.dto.JDAConfig;
import dev.lightdream.ticketsystem.dto.Lang;
import dev.lightdream.ticketsystem.manager.DatabaseManager;
import dev.lightdream.ticketsystem.manager.DiscordEventManager;
import dev.lightdream.ticketsystem.manager.ScheduleManager;
import lombok.SneakyThrows;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.requests.GatewayIntent;

import java.io.File;
import java.util.Arrays;

public class Main implements DatabaseMain, LoggableMain, FileManagerMain, JDAExtensionMain {

    public static Main instance;

    public Config config;
    public SQLConfig sqlConfig;
    public DriverConfig driverConfig;
    public JDAConfig jdaConfig;
    public Lang lang;

    public FileManager fileManager;
    public DatabaseManager databaseManager;
    public DiscordCommandManager discordCommandManager;
    public DiscordEventManager discordEventManager;
    public ScheduleManager scheduleManager;

    public JDA bot;

    public static ISecurity security;

    @SneakyThrows
    @SuppressWarnings({"ArraysAsListWithZeroOrOneArgument"})
    public void onEnable(ISecurity securityImpl) {
        security = securityImpl;
        security.sayHello();

        Debugger.init(this);
        Logger.init(this);

        instance = this;

        fileManager = new FileManager(this);
        loadConfigs();

        databaseManager = new DatabaseManager(this);

        bot = JDAExtensionMain.generateBot(this, jdaConfig.token, Arrays.asList(
                GatewayIntent.GUILD_MEMBERS
        ));

        discordCommandManager = new DiscordCommandManager(this, Arrays.asList(
                new StatsCommand(this),
                new BanDetailsCommand(),
                new CloseCommand(),
                new BanCommand(),
                new CheckBanCommand(),
                new HistoryCommand(),
                new UnbanCommand(),
                new AddCommand(),
                new TicketsCommand(),
                new TranscriptCommand(),
                new BlackListCommand(),
                new DebugCommand(),
                new SetupCommand()
        ));
        discordEventManager = new DiscordEventManager(this);
        scheduleManager = new ScheduleManager();

        Logger.good("Ticket System Bot (by https://github.com/L1ghtDream) has started!");

    }

    public void loadConfigs() {
        config = fileManager.load(Config.class);
        sqlConfig = fileManager.load(SQLConfig.class);
        driverConfig = fileManager.load(DriverConfig.class);
        jdaConfig = fileManager.load(JDAConfig.class);
        lang = fileManager.load(Lang.class);
    }

    @Override
    public File getDataFolder() {
        return new File(System.getProperty("user.dir") + "/config/");
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
    public boolean debug() {
        return config.debug;
    }

    @Override
    public void log(String s) {
        System.out.println(s);
    }

    @Override
    public String getVersion() {
        return "1.3.3";
    }

    @Override
    public JDA getBot() {
        return bot;
    }

    @Override
    public JDAConfig getJDAConfig() {
        return jdaConfig;
    }

}
