package dev.lightdream.royalsecurity.dto;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
public class Package {

    public int quantity;
    public int id;
    public String name;


    @Override
    public String toString() {
        return "Package{" +
                "quantity=" + quantity +
                ", id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
