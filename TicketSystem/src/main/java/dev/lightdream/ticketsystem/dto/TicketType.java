package dev.lightdream.ticketsystem.dto;

import java.util.List;

public class TicketType {

    public String name;
    public String id;
    public Long categoryID;
    public List<Long> associatedRanks;

    public TicketType() {
    }

    public TicketType(String name, String id, Long categoryID, List<Long> associatedRanks) {
        this.name = name;
        this.id = id;
        this.categoryID = categoryID;
        this.associatedRanks = associatedRanks;
    }
}
