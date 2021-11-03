package dev.lightdream.royalsecurity;

import dev.lightdream.api.LightDreamPlugin;
import dev.lightdream.api.configs.SQLConfig;
import dev.lightdream.api.databases.User;
import dev.lightdream.royalsecurity.commands.discord.*;
import dev.lightdream.royalsecurity.commands.minecraft.BaseLinkCommand;
import dev.lightdream.royalsecurity.commands.minecraft.CheckCode;
import dev.lightdream.royalsecurity.files.Config;
import dev.lightdream.royalsecurity.files.JdaConfig;
import dev.lightdream.royalsecurity.files.Lang;
import dev.lightdream.royalsecurity.managers.SecurityManager;
import dev.lightdream.royalsecurity.managers.*;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.HashMap;

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
    public ScheduleManager scheduleManager;

    @Override
    public void onEnable() {
        instance = this;

        init("RoyalSecurity", "link", "1.1");

        if (!config.multiLobby) {
            this.discordCommandManager = new DiscordCommandManager(this, Arrays.asList(
                    new HelpCommand(),
                    new LinkCommand(),
                    new UnlinkCommand(),
                    new ChangePassword(),
                    new UnregisterCommand(),
                    new StatsCommand(),
                    new ReGiveRanksCommand(),
                    new AccountsCommand()
            ));
        }
        this.securityManager = new SecurityManager(this);
        this.databaseManager = new DatabaseManager(this);
        if (!config.multiLobby) {
            this.discordEventManager = new DiscordEventManager(this);
        }
        this.minecraftEventManager = new MinecraftEventManager(this);
        this.scheduleManager = new ScheduleManager(this);

    }


    @Override
    public @NotNull String parsePapi(OfflinePlayer offlinePlayer, String s) {
        return "";
    }

    @Override
    public void loadConfigs() {
        sqlConfig = fileManager.load(SQLConfig.class);
        config = fileManager.load(Config.class);
        baseConfig = config;
        lang = fileManager.load(Lang.class, fileManager.getFile(baseConfig.baseLang));
        baseLang = lang;
        jdaConfig = fileManager.load(JdaConfig.class);
        baseJdaConfig = jdaConfig;
    }

    @Override
    public void disable() {

    }

    @Override
    public void registerFileManagerModules() {

    }

    @Override
    public void loadBaseCommands() {
        baseSubCommands.add(new BaseLinkCommand(this));
        baseSubCommands.add(new CheckCode());
    }

    @Override
    public dev.lightdream.api.managers.MessageManager instantiateMessageManager() {
        return new dev.lightdream.api.managers.MessageManager(this, Main.class);
    }

    @Override
    public void registerLangManager() {
        dev.lightdream.api.API.instance.langManager.register(Main.class, getLangs());
    }

    @Override
    public HashMap<String, Object> getLangs() {
        HashMap<String, Object> langs = new HashMap<>();

        baseConfig.langs.forEach(lang -> {
            dev.lightdream.api.configs.Lang l = fileManager.load(dev.lightdream.api.configs.Lang.class, fileManager.getFile(lang));
            langs.put(lang, l);
        });

        return langs;
    }

    @Override
    public dev.lightdream.api.managers.DatabaseManager getDatabaseManager() {
        return databaseManager;
    }

    @Override
    public void setLang(Player player, String s) {
        User user = databaseManager.getUser(player);
        user.setLang(s);
        databaseManager.save(user);
    }
}

/*

TODO
- unregister - admin
- chnagepass - owner

 */