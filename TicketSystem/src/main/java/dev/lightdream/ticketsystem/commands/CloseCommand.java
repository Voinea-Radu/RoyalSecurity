package dev.lightdream.ticketsystem.commands;

import dev.lightdream.jdaextension.commands.DiscordCommand;
import dev.lightdream.jdaextension.dto.context.GuildCommandContext;
import dev.lightdream.jdaextension.dto.context.PrivateCommandContext;
import dev.lightdream.ticketsystem.Main;
import dev.lightdream.ticketsystem.manager.TicketManager;

import java.util.ArrayList;

public class CloseCommand extends DiscordCommand {
    public CloseCommand() {
        super(Main.instance, "close", Main.instance.lang.closeCommandDescription, null, false, new ArrayList<>());
    }

    @Override
    public void executeGuild(GuildCommandContext context) {
        sendMessage(context, TicketManager.closeTicket(context.getTextChannel(), context.getUser()));
    }

    @Override
    public void executePrivate(PrivateCommandContext commandContext) {
        //Imposible
    }

    @Override
    public boolean isMemberSafe() {
        return false;
    }
}
