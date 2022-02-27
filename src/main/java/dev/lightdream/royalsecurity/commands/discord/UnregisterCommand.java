package dev.lightdream.royalsecurity.commands.discord;

import dev.lightdream.jdaextension.commands.DiscordCommand;
import dev.lightdream.royalsecurity.Main;
import fr.xephi.authme.api.v3.AuthMeApi;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;

import java.util.List;

public class UnregisterCommand extends DiscordCommand {
    public UnregisterCommand() {
        super(Main.instance, "unregister", Main.instance.lang.unregisterDescription, Permission.ADMINISTRATOR, "<username>", true);
    }

    @Override
    public void execute(Member member, TextChannel channel, List<String> args) {
        if (args.size() == 0) {
            sendUsage(channel);
            return;
        }

        if (!member.hasPermission(Permission.ADMINISTRATOR)) {
            sendMessage(channel, Main.instance.jdaConfig.notAllowed);
            return;
        }

        AuthMeApi.getInstance().forceUnregister(args.get(0));
        sendMessage(channel, Main.instance.jdaConfig.unregistered);
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
