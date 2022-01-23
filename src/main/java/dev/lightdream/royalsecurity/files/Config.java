package dev.lightdream.royalsecurity.files;

import lombok.NoArgsConstructor;

import java.util.Arrays;
import java.util.List;

@NoArgsConstructor
public class Config extends dev.lightdream.api.configs.Config {

    public boolean multiLobby = false;
    public int codeDigits = 8;
    public List<Long> channels = Arrays.asList(901849660052893736L, 904359681303138364L);

    public Long cooldown = 30 * 60 * 1000L;

    public String requiredPermission = ""; //Use '' to disable it and 'none' to force everyone

}
