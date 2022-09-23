package dev.lightdream.royalsecurity.commands.discord;

import dev.lightdream.jdaextension.commands.DiscordCommand;
import dev.lightdream.jdaextension.dto.CommandArgument;
import dev.lightdream.jdaextension.dto.context.GuildCommandContext;
import dev.lightdream.jdaextension.dto.context.PrivateCommandContext;
import dev.lightdream.royalsecurity.Main;
import dev.lightdream.royalsecurity.database.User;
import net.dv8tion.jda.api.interactions.commands.OptionType;

import java.util.Arrays;

public class LinkCommand extends DiscordCommand {
    @SuppressWarnings("ArraysAsListWithZeroOrOneArgument")
    public LinkCommand() {
        super(Main.instance, "link", Main.instance.lang.linkCommandDescription, null, true, Arrays.asList(
                new CommandArgument(OptionType.STRING, "username", Main.instance.lang.usernameArgDescription, true)
        ));
    }


    @Override
    public void executeGuild(GuildCommandContext context) {
        String username = context.getArgument("username").getAsString();

        User user = Main.instance.databaseManager.getUser(username);

        if (user == null) {
            sendMessage(context, Main.instance.jdaConfig.invalidUser);
            return;
        }

        if (user.hasSecurity()) {
            sendMessage(context, Main.instance.jdaConfig.alreadyLinked);
            return;
        }

        user.sendSecure(context, Main.instance.securityManager.generateCode(user, context.getMember().getIdLong()),
                context.getMember().getIdLong(), privateResponse);
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
