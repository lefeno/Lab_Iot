package com.example.bluesky.lab01_4;

import android.os.Build;

public class BoardDefaults {
    private static final String DEVICE_RPI3 = "rpi3";

    public static String getGPIOForLed(){
        switch(Build.DEVICE){
            case DEVICE_RPI3:
                return "BCM6";
            default:
                throw new IllegalStateException("Unknown Build.DEVICE " + Build.DEVICE);
        }
    }

    public static String getGPIOForButton(){
        switch(Build.DEVICE){
            case DEVICE_RPI3:
                return "BCM5";
            default:
                throw new IllegalStateException("Unknown Build.DEVICE " + Build.DEVICE);
        }
    }
}
