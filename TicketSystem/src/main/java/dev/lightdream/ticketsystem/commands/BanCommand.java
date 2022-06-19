package dev.lightdream.ticketsystem.commands;

import dev.lightdream.jdaextension.commands.DiscordCommand;
import dev.lightdream.jdaextension.dto.CommandArgument;
import dev.lightdream.jdaextension.dto.context.GuildCommandContext;
import dev.lightdream.jdaextension.dto.context.PrivateCommandContext;
import dev.lightdream.lambda.LambdaExecutor;
import dev.lightdream.ticketsystem.Main;
import dev.lightdream.ticketsystem.database.BanRecord;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.exceptions.HierarchyException;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BanCommand extends DiscordCommand {
    public BanCommand() {
        super(Main.instance, "ban", Main.instance.lang.banCommandDescription, Permission.BAN_MEMBERS, true, Arrays.asList(
                new CommandArgument(OptionType.USER, "user", Main.instance.lang.userIDDescription, true),
                new CommandArgument(OptionType.STRING, "reason", Main.instance.lang.banReasonDescription, true),
                new CommandArgument(OptionType.STRING, "duration", Main.instance.lang.banReasonDescription, false)
        ));
    }

    @Override
    public void executeGuild(GuildCommandContext context) {
        String reason = context.getArgument("reason").getAsString();

        List<Long> ranks = new ArrayList<>();
        Member member = context.getArgument("user").getAsMember();

        OptionMapping durationOption = context.getArgument("duration");
        long duration = 0L;

        if (durationOption != null) {
            String durationString = durationOption.getAsString();
            duration = LambdaExecutor.LambdaCatch.ReturnLambdaCatch.executeCatch(() -> {
                List<String> digits = Arrays.asList(durationString.split("[^0-9]+"));
                String unit = durationString.substring(durationString.length() - 1);
                long output;

                if (digits.contains(unit)) {
                    unit = "h";
                    output = Long.parseLong(durationString);
                } else {
                    String amountString = durationString.substring(0, durationString.length() - 1);
                    output = Long.parseLong(amountString);
                }

                switch (unit) {
                    case "m":
                        output *= 1000 * 60;
                        break;
                    case "h":
                        output *= 1000 * 60 * 60;
                        break;
                    case "d":
                        output *= 1000 * 60 * 60 * 24;
                        break;
                    default:
                        output *= 1000 * 60 * 60; // Use hours as default
                }

                return output;
            }, e -> {
                sendMessage(context, Main.instance.jdaConfig.banDurationInvalid);
                return -1L;
            });
        }

        if (duration == -1) {
            return;
        }

        if (member == null) {
            sendMessage(context, Main.instance.jdaConfig.invalidUser);
            return;
        }

        if (Main.instance.databaseManager.getBan(member.getIdLong()) != null) {
            sendMessage(context, Main.instance.jdaConfig.alreadyBanned);
            return;
        }

        member.getRoles().forEach(role -> ranks.add(role.getIdLong()));

        Guild guild = context.getGuild();

        for (Long rank : ranks) {
            Role role = guild.getRoleById(rank);
            if (role == null) {
                continue;
            }
            try {
                guild.removeRoleFromMember(member, role).queue(s -> {

                }, e -> {

                });
            } catch (HierarchyException e) {
                sendMessage(context, Main.instance.jdaConfig.cannotBan);
                return;
            }
        }

        Role role = guild.getRoleById(Main.instance.config.bannedRank);

        if (role == null) {
            sendMessage(context, Main.instance.jdaConfig.invalidBannedRole);
            return;
        }

        guild.addRoleToMember(member, role).queue();

        new BanRecord(member.getIdLong(), context.getUser().getIdLong(), ranks, reason, System.currentTimeMillis(), duration).save();
        sendMessage(context, Main.instance.jdaConfig.userBanned
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
