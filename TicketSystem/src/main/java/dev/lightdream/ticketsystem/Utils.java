package dev.lightdream.ticketsystem;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Utils {

    public static String msToDate(Long ms) {
        Date date = new Date(ms);
        DateFormat format = new SimpleDateFormat(Main.instance.config.dateFormat);
        return format.format(date);
    }

}
