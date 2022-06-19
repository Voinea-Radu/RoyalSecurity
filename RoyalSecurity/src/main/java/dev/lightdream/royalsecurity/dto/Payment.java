package dev.lightdream.royalsecurity.dto;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
public class Payment {

    public int id;
    public double amount;
    public Gateway gateway;
    public String status;
    public String date;
    public Currency currency;
    public String email;
    public Player player;
    public List<Package> packages;

    @Override
    public String toString() {
        return "Payment{" +
                "id=" + id +
                ", amount=" + amount +
                ", gateway=" + gateway +
                ", status='" + status + '\'' +
                ", date='" + date + '\'' +
                ", currency=" + currency +
                ", email='" + email + '\'' +
                ", player=" + player +
                ", packages=" + packages +
                '}';
    }
}
