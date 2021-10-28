package dev.lightdream.royalsecurity.commands.minecraft;

import dev.lightdream.api.IAPI;
import dev.lightdream.api.commands.SubCommand;
import dev.lightdream.api.databases.User;
import dev.lightdream.royalsecurity.Main;
import dev.lightdream.royalsecurity.dto.UserPair;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class BaseLinkCommand extends SubCommand {
    public BaseLinkCommand(@NotNull IAPI api) {
        super(api, "", true, false, "[code]");
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    public void execute(User user, List<String> args) {
        if (args.size() != 1) {
            sendUsage(user);
            return;
        }

        UserPair pair = Main.instance.securityManager.linkCodes.get(args.get(0));

        if (pair == null) {
            user.sendMessage(api, Main.instance.lang.invalidCode);
            return;
        }

        if (!pair.user.equals(user)) {
            user.sendMessage(api, Main.instance.lang.invalidCode);
            return;
        }

        pair.pair(user.getPlayer().getAddress().getHostName());
        user.sendMessage(api, Main.instance.lang.linked);
    }

    @Override
    public List<String> onTabComplete(User user, List<String> list) {
        return new ArrayList<>();
    }
}
