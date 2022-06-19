package dev.lightdream.ticketsystem.manager;

import dev.lightdream.ticketsystem.Main;
import dev.lightdream.ticketsystem.database.BanRecord;
import net.dv8tion.jda.api.entities.TextChannel;

public class BanManager {

    public static void unban(Long id, TextChannel textChannel) {
        BanRecord ban = Main.instance.databaseManager.getBan(id);

        if (ban == null) {
            textChannel.sendMessageEmbeds(Main.instance.jdaConfig.notBanned.build().build()).queue();
            return;
        }

        if (!ban.unban(textChannel)) {
            textChannel.sendMessageEmbeds(Main.instance.jdaConfig.notBanned.build().build()).queue();
        }
    }


}
