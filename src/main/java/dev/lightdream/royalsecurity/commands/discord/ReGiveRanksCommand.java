package dev.lightdream.royalsecurity.commands.discord;

import dev.lightdream.royalsecurity.Main;
import dev.lightdream.royalsecurity.commands.DiscordCommand;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.User;
import org.bukkit.Bukkit;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Deprecated
public class ReGiveRanksCommand extends DiscordCommand {
    public ReGiveRanksCommand() {
        super("reGiveRanks", Main.instance.lang.reGiveRanksDescription, Permission.ADMINISTRATOR, "");
    }

    @Override
    public void execute(Member member, MessageChannel channel, List<String> args) {
        execute(member.getUser(), channel, args);
    }

    @Override
    public void execute(User user, MessageChannel channel, List<String> args) {
        Bukkit.getScheduler().runTaskAsynchronously(Main.instance, () -> {
            List<dev.lightdream.royalsecurity.dto.User> users = Main.instance.databaseManager.getAll(dev.lightdream.royalsecurity.dto.User.class);
            AtomicInteger counter = new AtomicInteger();
            users.forEach(u -> {
                if (u.discordID == null) {
                    return;
                }
                u.giveRank();
                u.changeNickname();
                counter.getAndIncrement();
            });
            sendMessage(channel, Main.instance.jdaConfig.reGiven
                    .parse("count", String.valueOf(counter.get()))
            );
        });

    }

    @Override
    public boolean isMemberSafe() {
        return true;
    }
}
