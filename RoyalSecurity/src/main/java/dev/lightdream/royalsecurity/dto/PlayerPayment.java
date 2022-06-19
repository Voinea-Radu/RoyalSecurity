package dev.lightdream.royalsecurity.dto;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
public class PlayerPayment {

    public String id;
    public String created_at;
    public String updated_at;
    public String cache_expire;
    public String username;
    public String meta;
    public int plugin_username_id;

    @Override
    public String toString() {
        return "Player{" +
                "id='" + id + '\'' +
                ", created_at='" + created_at + '\'' +
                ", updated_at='" + updated_at + '\'' +
                ", cache_expire='" + cache_expire + '\'' +
                ", username='" + username + '\'' +
                ", meta='" + meta + '\'' +
                ", plugin_username_id=" + plugin_username_id +
                '}';
    }
}
