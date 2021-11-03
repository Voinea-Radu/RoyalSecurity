package dev.lightdream.royalsecurity.commands.discord;

import dev.lightdream.api.utils.Utils;
import dev.lightdream.royalsecurity.Main;
import dev.lightdream.royalsecurity.commands.DiscordCommand;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.User;

import java.util.List;

public class StatsCommand extends DiscordCommand {
    public StatsCommand() {
        super("stats", Main.instance.lang.statsDescription, Permission.ADMINISTRATOR, "");
    }

    @Override
    public void execute(Member member, MessageChannel channel, List<String> args) {
        execute(member.getUser(), channel, args);
    }

    @Override
    public void execute(User user, MessageChannel channel, List<String> args) {
        sendMessage(channel, Main.instance.jdaConfig.stats
                .parse("ram", String.valueOf(Utils.getRam()))
                .parse("cpu", String.valueOf(Utils.getCpuLoad()))
                .parse("java", Utils.getJava())
        );
    }

    @Override
    public boolean isMemberSafe() {
        return true;
    }


}
