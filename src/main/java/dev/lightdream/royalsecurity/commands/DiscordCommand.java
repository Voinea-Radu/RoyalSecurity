package dev.lightdream.royalsecurity.commands;

import dev.lightdream.api.dto.jda.JdaEmbed;
import dev.lightdream.royalsecurity.Main;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.internal.utils.PermissionUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public abstract class DiscordCommand {

    public final @NotNull List<String> aliases = new ArrayList<>();
    public final @NotNull String description;
    public final @NotNull String usage;
    public final Permission permission;

    @SuppressWarnings("unused")
    public DiscordCommand(@NotNull List<String> aliases, @NotNull String description, Permission permission, @NotNull String usage) {
        for (String alias : aliases) {
            this.aliases.add(alias.toLowerCase());
        }
        this.description = description;
        this.usage = usage;
        this.permission = permission;
    }

    public DiscordCommand(@NotNull String alias, @NotNull String description, Permission permission, @NotNull String usage) {
        this.aliases.add(alias.toLowerCase());
        this.description = description;
        this.usage = usage;
        this.permission = permission;
    }

    @SuppressWarnings("unused")
    public boolean hasPermission(Member member) {
        if (permission == null) {
            return true;
        }
        if (member == null) {
            return false;
        }
        return PermissionUtil.checkPermission(member, permission);

    }

    public void execute(@Nullable Member member, User user, TextChannel channel, List<String> args) {
        if (!isMemberSafe()) {
            if (member == null) {
                sendMessage(channel, Main.instance.jdaConfig.serverCommand);
                return;
            }
        }
        if (member == null) {
            execute(user, channel, args);
            return;
        }
        execute(member, channel, args);
    }

    public abstract void execute(Member member, TextChannel channel, List<String> args);

    public abstract void execute(User user, MessageChannel channel, List<String> args);

    public void sendUsage(MessageChannel channel) {
        channel.sendMessageEmbeds(Main.instance.jdaConfig.usage
                .parse("command", aliases.get(0))
                .parse("usage", usage)
                .build().build()
        ).queue();
    }

    public void sendMessage(MessageChannel channel, JdaEmbed embed) {
        channel.sendMessageEmbeds(embed.build().build()).queue();
    }


    /**
     * False  if it requires the member to be not null
     * True   if it does not require the member to be not null
     */
    public abstract boolean isMemberSafe();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DiscordCommand that = (DiscordCommand) o;
        for (String alias : aliases) {
            if (that.aliases.contains(alias)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(aliases);
    }

}
