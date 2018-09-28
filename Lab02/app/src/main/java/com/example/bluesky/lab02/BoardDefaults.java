package com.example.bluesky.lab02;

import android.os.Build;

public class BoardDefaults {

    private static final String DEVICE_RPI3 = "rpi3";

    /**
     * Return the UART for loopback.
     */
    public static String getUartName() {
        switch (Build.DEVICE) {
            case DEVICE_RPI3:
                return "UART0";
            default:
                throw new IllegalStateException("Unknown Build.DEVICE " + Build.DEVICE);
        }
    }

    public static String getGPIOForLedB(){
        switch (Build.DEVICE) {
            case DEVICE_RPI3:
                return "BCM2";
            default:
                throw new IllegalStateException("Unknown Build.DEVICE " + Build.DEVICE);
        }
    }

    public static String getGPIOForLedR(){
        switch(Build.DEVICE){
            case DEVICE_RPI3:
                return "BCM3";
            default:
                throw new IllegalStateException("Unknown Build.DEVICE " + Build.DEVICE);
        }
    }

    public static String getGPIOForLedG(){
        switch(Build.DEVICE){
            case DEVICE_RPI3:
                return "BCM4";
            default:
                throw new IllegalStateException("Unknown Build.DEVICE " + Build.DEVICE);
        }
    }

    public static String getGPIOForLed(){
        switch(Build.DEVICE){
            case DEVICE_RPI3:
                return "PWM0";
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

