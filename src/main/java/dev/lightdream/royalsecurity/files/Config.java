package dev.lightdream.royalsecurity.files;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Config extends dev.lightdream.api.configs.Config {

    public boolean multiLobby = false;
    public int codeDigits = 8;
    public List<Long> channels = Arrays.asList(
            901849660052893736L
    );
    public int nicknameChangeInterval = 30; //minutes
    public Long verifiedRankID = 902534331082375188L;
    public Long gameBannedRankID = 902533759692324935L; //todo add litebans dependency

}
