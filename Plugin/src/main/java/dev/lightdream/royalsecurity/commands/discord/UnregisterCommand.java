package dev.lightdream.royalsecurity.commands.discord;

import dev.lightdream.jdaextension.commands.DiscordCommand;
import dev.lightdream.jdaextension.dto.CommandArgument;
import dev.lightdream.jdaextension.dto.context.GuildCommandContext;
import dev.lightdream.jdaextension.dto.context.PrivateCommandContext;
import dev.lightdream.royalsecurity.Main;
import fr.xephi.authme.api.v3.AuthMeApi;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.interactions.commands.OptionType;

import java.util.Arrays;

public class UnregisterCommand extends DiscordCommand {
    @SuppressWarnings("ArraysAsListWithZeroOrOneArgument")
    public UnregisterCommand() {
        super(Main.instance, "unregister", Main.instance.lang.unregisterDescription, Permission.ADMINISTRATOR, true, Arrays.asList(
                new CommandArgument(OptionType.STRING, "username", Main.instance.lang.usernameArgDescription, true)
        ));
    }


    @Override
    public void executeGuild(GuildCommandContext context) {
        String account = context.getArgument("username").getAsString();

        if (!context.getMember().hasPermission(Permission.ADMINISTRATOR)) {
            sendMessage(context, Main.instance.jdaConfig.notAllowed);
            return;
        }

        AuthMeApi.getInstance().forceUnregister(account);
        sendMessage(context, Main.instance.jdaConfig.unregistered);
    }

    @Override
    public void executePrivate(PrivateCommandContext privateCommandContext) {
        //Impossible
    }

    @Override
    public boolean isMemberSafe() {
        return false;
    }
}
