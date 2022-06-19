package dev.lightdream.ticketsystem.dto;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@SuppressWarnings("ArraysAsListWithZeroOrOneArgument")
public class Config {

    public boolean debug = false;
    public Long ticketsChanel = 0L;
    public Long unbanTicketsChanel = 0L;

    public List<TicketType> ticketTypes = Arrays.asList(
            new TicketType("\uD83D\uDCDC General", "general", 0L, Arrays.asList(
                    0L
            )),
            new TicketType("\uD83C\uDFC6 Tops", "tops", 0L, Arrays.asList(
                    0L
            )),
            new TicketType("\uD83D\uDCB0 Donations", "donations", 0L, Arrays.asList(
                    0L
            )),
            new TicketType("\uD83D\uDD12 Lost Passwords", "lost-passwords", 0L, Arrays.asList(
                    0L
            ))
    );

    public TicketType unbanTicket = new TicketType("\uD83D\uDD13 Unban Request", "unban-request", 0L, new ArrayList<>());

    public Long bannedRank = 0L;
    public Long managerPingRank = 0L;

    public String dateFormat = "HH:mm dd MMM yyyy";

}
