package com.example.waimai.entity;

import java.io.Serializable;
import java.util.Random;

public class AuthUtils implements Serializable {
    private static final long serialVersionUID = 1L;
    public static String getCode(int length){
        Random random = new Random(System.currentTimeMillis());
        String timeString = String.valueOf(random.nextLong());
        return timeString.substring(timeString.length()-4,timeString.length());
    }
}
