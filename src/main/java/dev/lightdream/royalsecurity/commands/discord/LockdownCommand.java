package dev.lightdream.royalsecurity.commands.discord;

import dev.lightdream.royalsecurity.Main;
import dev.lightdream.royalsecurity.commands.DiscordCommand;
import dev.lightdream.royalsecurity.database.Lockdown;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.User;

import java.util.List;

public class LockdownCommand extends DiscordCommand {
    public LockdownCommand() {
        super("lockdown", Main.instance.lang.lockdownDescription, null, "");
    }

    @Override
    public void execute(Member member, MessageChannel channel, List<String> args) {
        execute(member.getUser(), channel, args);
    }

    @Override
    public void execute(User u, MessageChannel channel, List<String> args) {
        Lockdown lockdown = Main.instance.databaseManager.getLockdown(u.getIdLong());
        lockdown.toggle();
        sendMessage(channel, Main.instance.jdaConfig.lockdown
                .parse("status", String.valueOf(lockdown.status))
        );
    }

    @Override
    public boolean isMemberSafe() {
        return true;
    }
}
