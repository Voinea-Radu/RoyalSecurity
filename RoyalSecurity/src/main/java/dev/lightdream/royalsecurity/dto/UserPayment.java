package dev.lightdream.royalsecurity.dto;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
public class UserPayment {

    public String txn_id;
    public Long time;
    public double price;
    public String currency;
    public int status;

    @Override
    public String toString() {
        return "Payment{" +
                "txn_id='" + txn_id + '\'' +
                ", time=" + time +
                ", price=" + price +
                ", currency='" + currency + '\'' +
                ", status=" + status +
                '}';
    }
}
