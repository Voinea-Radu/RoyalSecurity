package dev.lightdream.ticketsystem.database;

import dev.lightdream.databasemanager.annotations.database.DatabaseField;
import dev.lightdream.databasemanager.annotations.database.DatabaseTable;
import dev.lightdream.databasemanager.dto.entry.impl.IntegerDatabaseEntry;
import dev.lightdream.ticketsystem.Main;
import net.dv8tion.jda.api.exceptions.ErrorHandler;
import net.dv8tion.jda.api.requests.ErrorResponse;

@DatabaseTable(table = "tickets")
public class Ticket extends IntegerDatabaseEntry {

    @DatabaseField(columnName = "type")
    public String type;
    @DatabaseField(columnName = "channel_id")
    public Long channelID;
    @DatabaseField(columnName = "creator_id")
    public Long creatorID;
    @DatabaseField(columnName = "pinged_manager")
    public boolean pingedManager;
    @DatabaseField(columnName = "active")
    public boolean active;
    @DatabaseField(columnName = "timestamp")
    public long timestamp;

    public Ticket(String type, Long channelID, Long creatorID) {
        super(Main.instance);
        this.type = type;
        this.channelID = channelID;
        this.pingedManager = false;
        this.creatorID = creatorID;
        this.active = true;
        this.timestamp = System.currentTimeMillis();
    }

    @SuppressWarnings("unused")
    public Ticket() {
        super(Main.instance);
    }

    public Transcript getTranscript() {
        Transcript transcript = Main.instance.databaseManager.getTranscript(id);
        if (transcript == null) {
            transcript = new Transcript(id);
        }
        return transcript;
    }

    public void close() {
        this.active = false;
        Main.instance.bot.retrieveUserById(creatorID)
                .queue(user -> user.openPrivateChannel()
                        .queue(channel -> channel.sendMessageEmbeds(Main.instance.jdaConfig.closedTicket
                                        .parse("id", String.valueOf(getTranscript().ticketID)).build().build())
                                .queue(null,
                                        new ErrorHandler().handle(ErrorResponse.CANNOT_SEND_TO_USER,
                                                e -> {
                                                    //empty
                                                }))));
        save();
    }


}
