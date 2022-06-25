package dev.lightdream.royalsecurity.commands.discord;

import com.google.gson.Gson;
import dev.lightdream.jdaextension.commands.DiscordCommand;
import dev.lightdream.jdaextension.dto.CommandArgument;
import dev.lightdream.jdaextension.dto.JDAEmbed;
import dev.lightdream.jdaextension.dto.context.CommandContext;
import dev.lightdream.jdaextension.dto.context.GuildCommandContext;
import dev.lightdream.jdaextension.dto.context.PrivateCommandContext;
import dev.lightdream.logger.Debugger;
import dev.lightdream.messagebuilder.MessageBuilder;
import dev.lightdream.royalsecurity.Main;
import dev.lightdream.royalsecurity.database.User;
import dev.lightdream.royalsecurity.dto.Payment;
import dev.lightdream.royalsecurity.dto.TebexUser;
import lombok.SneakyThrows;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class PaymentsCommand extends DiscordCommand {
    @SuppressWarnings("ArraysAsListWithZeroOrOneArgument")
    public PaymentsCommand() {
        super(Main.instance, "payments", Main.instance.lang.paymentsCommandDescription, null, true, Arrays.asList(
                new CommandArgument(OptionType.STRING, "username", Main.instance.lang.usernameArgDescription, false)
        ));
    }

    @SneakyThrows
    @Override
    public void executeGuild(GuildCommandContext context) {
        List<User> users = Main.instance.databaseManager.getUser(context.getMember().getIdLong());

        if (context.getArgument("username") == null) {
            if (users.size() == 0) {
                sendMessage(context, Main.instance.jdaConfig.notLinked);
                return;
            }
            if (users.size() > 1) {
                sendMessage(context, Main.instance.jdaConfig.multipleLinked);
                return;
            }

            String username = users.get(0).name;
            sendPayments(context, username);
            return;
        }

        String username = context.getArgument("username").getAsString();
        AtomicBoolean pass = new AtomicBoolean(false);

        users.forEach(user -> {
            if (user.name.equals(username)) {
                pass.set(true);
            }
        });

        if (!pass.get()) {
            if (!context.getMember().hasPermission(Permission.ADMINISTRATOR)) {
                sendMessage(context, Main.instance.jdaConfig.notAllowed);
                return;
            }
        }
        sendPayments(context, username);
    }

    @Override
    public void executePrivate(PrivateCommandContext context) {

    }

    @Override
    public boolean isMemberSafe() {
        return true;
    }

    private void sendPayments(CommandContext context, String username) {
        TebexUser user = getTebexUser(username);

        if (user == null) {
            sendMessage(context, Main.instance.jdaConfig.invalidUser);
            return;
        }

        JDAEmbed embed = Main.instance.jdaConfig.payments.clone()
                .parse("name", username);

        user.payments.forEach(p -> {
            Payment payment = getPayment(p.txn_id);

            if (payment == null || !payment.status.equalsIgnoreCase("Complete")) {
                return;
            }

            if (payment.packages.size() == 0) {
                embed.description += "\n" +
                        new MessageBuilder(Main.instance.jdaConfig.payment)
                                .parse("package_name", "https://server.tebex.io/payments/" + payment.id)
                                .parse("package_quantity", "1")
                                .parse();
                return;
            }

            payment.packages.forEach(aPackage ->
                    embed.description += "\n" +
                            new MessageBuilder(Main.instance.jdaConfig.payment)
                                    .parse("package_name", aPackage.name)
                                    .parse("package_quantity", String.valueOf(aPackage.quantity))
                                    .parse());
        });

        sendMessage(context, embed);
    }

    @SneakyThrows
    private TebexUser getTebexUser(String name) {

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("https://plugin.tebex.io/user/" + name)
                .addHeader("X-Tebex-Secret", Main.instance.config.tebexID)
                .build();

        Response response = client.newCall(request).execute();

        if (response.body() == null) {
            return null;
        }

        String responseString = response.body().string();

        return new Gson().fromJson(responseString, TebexUser.class);
    }

    @SneakyThrows
    private Payment getPayment(String id) {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("https://plugin.tebex.io/payments/" + id)
                .addHeader("X-Tebex-Secret", Main.instance.config.tebexID)
                .build();

        Response response = client.newCall(request).execute();

        if (response.body() == null) {
            return null;
        }

        return new Gson().fromJson(response.body().string(), Payment.class);
    }
}



