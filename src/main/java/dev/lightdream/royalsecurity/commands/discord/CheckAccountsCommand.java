package dev.lightdream.royalsecurity.commands.discord;

import dev.lightdream.api.utils.Debugger;
import dev.lightdream.royalsecurity.commands.DiscordCommand;
import lombok.SneakyThrows;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class CheckAccountsCommand extends DiscordCommand {
    public CheckAccountsCommand() {
        super("checkAccounts", "Check all account in the current discord server for linked status", Permission.ADMINISTRATOR, "+checkAccounts");
    }

    @SneakyThrows
    @Override
    public void execute(Member member, TextChannel channel, List<String> args) {
        Member self = channel.getGuild().getSelfMember();
        for (Member m : channel.getGuild().getMembers()) {
            String name = m.getEffectiveName(); // username or nickname
            Debugger.info(name);
            //if (!name.startsWith("[NNN]") && self.canInteract(m)) {
            //    m.modifyNickname("[NNN]" + name).queue();
            //}
        }
        Debugger.info(channel.getGuild().getMembers());
        Debugger.info(channel.getGuild().getMembers().size());
        Debugger.info(channel.getGuild().retrieveMembers);
        //Debugger.info(channel.getGuild().);
    }

    @Override
    public void execute(User user, MessageChannel channel, List<String> args) {
        //impossible
    }

    @Override
    public boolean isMemberSafe() {
        return false;
    }
}
