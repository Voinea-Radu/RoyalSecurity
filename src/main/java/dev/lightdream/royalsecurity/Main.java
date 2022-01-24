package dev.lightdream.royalsecurity;

import dev.lightdream.api.LightDreamPlugin;
import dev.lightdream.databasemanager.dto.SQLConfig;
import dev.lightdream.royalsecurity.commands.discord.*;
import dev.lightdream.royalsecurity.files.Config;
import dev.lightdream.royalsecurity.files.JdaConfig;
import dev.lightdream.royalsecurity.files.Lang;
import dev.lightdream.royalsecurity.managers.SecurityManager;
import dev.lightdream.royalsecurity.managers.*;
import lombok.SneakyThrows;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

public final class Main extends LightDreamPlugin {

    public static Main instance;

    //Settings
    public Config config;
    public Lang lang;
    public JdaConfig jdaConfig;

    //Managers
    public DiscordCommandManager discordCommandManager;
    public SecurityManager securityManager;
    public DatabaseManager databaseManager;
    public DiscordEventManager discordEventManager;
    public MinecraftEventManager minecraftEventManager;

    @SneakyThrows
    @Override
    public void onEnable() {
        instance = this;

        init("RoyalSecurity", "royalsecurity");

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
        this.databaseManager = (DatabaseManager) getDatabaseManager();
        if (!config.multiLobby) {
            this.discordEventManager = new DiscordEventManager(this);
        }
        this.minecraftEventManager = new MinecraftEventManager(this);
    }


    @Override
    public @NotNull String parsePapi(OfflinePlayer offlinePlayer, String s) {
        return "";
    }

    @Override
    public void loadConfigs() {
        sqlConfig = fileManager.load(SQLConfig.class);
        config = fileManager.load(Config.class);
        lang = fileManager.load(Lang.class);
        baseLang = lang;
        jdaConfig = fileManager.load(JdaConfig.class);
        baseJdaConfig = jdaConfig;
    }

    @Override
    public dev.lightdream.api.managers.DatabaseManager registerDatabaseManager() {
        return new DatabaseManager(this);
    }

    @Override
    public void disable() {
    }

    @Override
    public void registerFileManagerModules() {
    }

    @Override
    public void registerUser(Player player) {
        databaseManager.createUser(player);
    }


}