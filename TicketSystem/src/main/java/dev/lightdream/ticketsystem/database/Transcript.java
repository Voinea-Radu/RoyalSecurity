package dev.lightdream.ticketsystem.database;

import dev.lightdream.databasemanager.annotations.database.DatabaseField;
import dev.lightdream.databasemanager.annotations.database.DatabaseTable;
import dev.lightdream.databasemanager.dto.entry.impl.IntegerDatabaseEntry;
import dev.lightdream.jdaextension.dto.context.CommandContext;
import dev.lightdream.lambda.LambdaExecutor;
import dev.lightdream.ticketsystem.Main;
import me.kaimu.hastebin.Hastebin;
import net.dv8tion.jda.api.entities.User;

import java.util.ArrayList;
import java.util.List;

@DatabaseTable(table = "transcripts")
public class Transcript extends IntegerDatabaseEntry {

    @DatabaseField(columnName = "ticket_id")
    public int ticketID;
    @DatabaseField(columnName = "messages")
    public List<String> messages;


    public Transcript(int ticketID) {
        super(Main.instance);
        this.ticketID = ticketID;
        this.messages = new ArrayList<>();
    }

    @SuppressWarnings("unused")
    public Transcript() {
        super(Main.instance);
    }

    public void record(User user, String record) {
        messages.add(
                user.getName() + " (" + user.getId() + "): " + record
        );
        save();
    }

    public void send(CommandContext context, boolean privateMessage) {
        LambdaExecutor.LambdaCatch.NoReturnLambdaCatch.executeCatch(() -> {
            Hastebin hastebin = new Hastebin();

            StringBuilder text = new StringBuilder();
            messages.forEach(message -> {
                text.append(message);
                text.append("\n");
            });

            String url = hastebin.post(text.toString(), false);

            context.sendMessage(
                    Main.instance.jdaConfig.transcript
                            .parse("id", String.valueOf(ticketID))
                            .parse("url", url),
                    privateMessage
            );
        });
    }
}
