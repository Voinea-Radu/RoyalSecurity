package dev.lightdream.royalsecurity.commands.discord;

import dev.lightdream.royalsecurity.Main;
import dev.lightdream.royalsecurity.commands.DiscordCommand;
import dev.lightdream.royalsecurity.dto.User;
import fr.xephi.authme.api.v3.AuthMeApi;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageChannel;
import org.jetbrains.annotations.NotNull;

import java.sql.SQLOutput;
import java.util.List;

public class ChangePassword extends DiscordCommand {
    public ChangePassword() {
        super("changePassword", Main.instance.lang.changePasswordDescription, null, "[new password] - IN DMs");
    }

    @Override
    public void execute(Member member, MessageChannel channel, List<String> args) {
        //todo send message informing user to use the command in dms
    }

    @Override
    public void execute(net.dv8tion.jda.api.entities.User user, MessageChannel channel, List<String> args) {
        System.out.println("In private messages");

    }

    @Override
    public boolean isMemberSafe() {
        return true;
    }

    private void changePassword(){

    }
}
