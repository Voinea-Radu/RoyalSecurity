package dev.lightdream.royalsecurity.dto;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
public class TebexUser {

    public PlayerPayment player;
    public int banCount;
    public int chargebackRate;
    public List<UserPayment> payments;
    public HashMap<String, Double> purchaseTotals;

    @Override
    public String toString() {
        return "TebexUser{" +
                "player=" + player +
                ", banCount=" + banCount +
                ", chargebackRate=" + chargebackRate +
                ", payments=" + payments +
                ", purchaseTotals=" + purchaseTotals +
                '}';
    }
}
