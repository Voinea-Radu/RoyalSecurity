package dev.lightdream.royalsecurity.files;

import java.util.Arrays;
import java.util.List;

public class Config extends dev.lightdream.api.configs.Config {

    public boolean multiLobby = false;
    public int codeDigits = 8;
    public List<Long> channels = Arrays.asList(
            901849660052893736L,
            904359681303138364L
    );
    @Deprecated
    public int nicknameChangeInterval = 30; //minutes
    @Deprecated
    public List<Long> verifiedRankID = Arrays.asList(
            902534331082375188L,
            903291352261996644L
    );

}
