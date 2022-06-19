package dev.lightdream.ticketsystem.commands;

import dev.lightdream.jdaextension.commands.DiscordCommand;
import dev.lightdream.jdaextension.dto.CommandArgument;
import dev.lightdream.jdaextension.dto.context.GuildCommandContext;
import dev.lightdream.jdaextension.dto.context.PrivateCommandContext;
import dev.lightdream.ticketsystem.Main;
import dev.lightdream.ticketsystem.database.Ticket;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.interactions.commands.OptionType;

import java.util.Collections;

public class AddCommand extends DiscordCommand {
    public AddCommand() {
        super(Main.instance, "add", Main.instance.lang.addCommandDescription, Permission.MANAGE_CHANNEL, false, Collections.singletonList(
                new CommandArgument(OptionType.USER, "user", Main.instance.lang.userIDDescription, true)
        ));
    }

    @Override
    public void executeGuild(GuildCommandContext context) {
        Ticket ticket = Main.instance.databaseManager.getTicket(context.getTextChannel().getIdLong());

        if (ticket == null) {
            sendMessage(context, Main.instance.jdaConfig.notTicket);
            return;
        }

        Member member = context.getArgument("user").getAsMember();
        if (member == null) {
            sendMessage(context, Main.instance.jdaConfig.invalidUser);
            return;
        }

        context.getTextChannel().putPermissionOverride(member).setAllow(
                Permission.MESSAGE_SEND, Permission.MESSAGE_HISTORY,
                Permission.MESSAGE_ATTACH_FILES, Permission.VIEW_CHANNEL
        ).queue();

        sendMessage(context, Main.instance.jdaConfig.addedToTicket
                .parse("name", member.getEffectiveName()));

    }

    @Override
    public void executePrivate(PrivateCommandContext commandContext) {
        //Impossible
    }

    @Override
    public boolean isMemberSafe() {
        return false;
    }
}
