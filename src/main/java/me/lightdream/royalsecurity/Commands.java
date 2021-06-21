package me.lightdream.royalsecurity;

import me.lucko.helper.text3.Text;

public class Commands {

    RoyalSecurity plugin;

    public Commands(RoyalSecurity plugin) {
        this.plugin = plugin;
        registerCommands();
    }

    public void registerCommands() {
        me.lucko.helper.Commands.create()
                .assertPlayer()
                .handler(c -> {
                    if (plugin.getPendingCodes().containsKey((c.rawArg(0)))) {
                        if (plugin.getPendingCodes().get(c.rawArg(0)).containsKey(c.sender().getUniqueId())) {
                            plugin.getApi().link(c.sender().getUniqueId(), plugin.getPendingCodes().get(c.rawArg(0)).get(c.sender().getUniqueId()));
                            c.sender().sendMessage(Text.colorize(plugin.getMessages().linkSuccessful));
                            plugin.loggedInPlayers.add(c.sender().getUniqueId().toString());
                        } else {
                            c.sender().sendMessage(Text.colorize(plugin.getMessages().invalidCode));
                        }
                    } else {
                        c.sender().sendMessage(Text.colorize(plugin.getMessages().invalidCode));
                    }


                }).registerAndBind(plugin, "rs-link");
    }
}
