package dev.lightdream.ticketsystem.commands;

import dev.lightdream.jdaextension.commands.DiscordCommand;
import dev.lightdream.jdaextension.dto.context.GuildCommandContext;
import dev.lightdream.jdaextension.dto.context.PrivateCommandContext;
import dev.lightdream.ticketsystem.Main;
import dev.lightdream.ticketsystem.manager.ScheduleManager;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.TextChannel;

import java.util.ArrayList;

public class DebugCommand extends DiscordCommand {
    public DebugCommand() {
        super(Main.instance, "debug", "Debug command", Permission.ADMINISTRATOR, true, new ArrayList<>());
    }

    @Override
    public void executeGuild(GuildCommandContext context) {
        TextChannel textChannel = context.getTextChannel();

        ScheduleManager.lastMessageSent(textChannel, () -> {
        }, userBot -> {
        }, timeLeft -> {
            int m = (int) (timeLeft / 60);
            int h = m / 60;
            int s = (int) (timeLeft % 60);
            m = m % 60;

            sendMessage(context, Main.instance.jdaConfig.timeLeft
                    .parse("h", String.valueOf(h))
                    .parse("m", String.valueOf(m))
                    .parse("s", String.valueOf(s))
            );
        });

    }

    @Override
    public void executePrivate(PrivateCommandContext context) {
        //Impossible
    }

    @Override
    public boolean isMemberSafe() {
        return false;
    }
}
