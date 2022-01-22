package dev.lightdream.royalsecurity.commands.discord;

import dev.lightdream.api.utils.Utils;
import dev.lightdream.royalsecurity.Main;
import dev.lightdream.royalsecurity.commands.DiscordCommand;
import dev.lightdream.royalsecurity.database.UserPair;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;

import java.util.List;

public class StatsCommand extends DiscordCommand {
    public StatsCommand() {
        super("stats", Main.instance.lang.statsDescription, Permission.ADMINISTRATOR, "");
    }

    @Override
    public void execute(Member member, TextChannel channel, List<String> args) {
        execute(member.getUser(), channel, args);
    }

    @Override
    public void execute(User user, MessageChannel channel, List<String> args) {
        sendMessage(channel,
                Main.instance.jdaConfig.stats.parse("ram", String.valueOf(Utils.getRam()))
                        .parse("cpu", String.valueOf(Utils.getCpuLoad()))
                        .parse("java", Utils.getJava())
                        .parse("users",
                                String.valueOf((int) Main.instance.databaseManager.getAll(dev.lightdream.royalsecurity.database.User.class)
                                        .stream()
                                        .filter(dev.lightdream.royalsecurity.database.User::hasSecurity)
                                        .count()))
                        .parse("codes", String.valueOf(Main.instance.databaseManager.getAll(UserPair.class).size())));
    }

    @Override
    public boolean isMemberSafe() {
        return true;
    }


}
