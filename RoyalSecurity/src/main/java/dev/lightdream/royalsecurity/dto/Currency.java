package dev.lightdream.royalsecurity.dto;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
public class Currency {

    public String iso_4217;
    public String symbol;

    @Override
    public String toString() {
        return "Currency{" +
                "iso_4217='" + iso_4217 + '\'' +
                ", symbol='" + symbol + '\'' +
                '}';
    }
}
