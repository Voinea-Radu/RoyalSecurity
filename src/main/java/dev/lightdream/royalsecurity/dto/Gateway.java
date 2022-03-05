package dev.lightdream.royalsecurity.dto;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
public class Gateway {

    public int id;
    public String name;

    @Override
    public String toString() {
        return "Gateway{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
