package com.JonasAmme.JonasAmme.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DataHelper {

    public static LocalDateTime getCurrentTimeStamp(){
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        return LocalDateTime.now();
    }
}
