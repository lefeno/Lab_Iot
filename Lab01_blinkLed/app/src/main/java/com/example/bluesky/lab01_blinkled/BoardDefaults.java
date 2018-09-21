package com.example.bluesky.lab01_blinkled;

import android.os.Build;

public class BoardDefaults {
    //can't change the value of final var, i.e. const
    // method final: can't override it
    // JV doesn't directly support const,
    // but a static final var effectively a const
    // static: var is available without loading an instance of the class it's defined
    // JV const are normally declared in ALL CAPS, separated by underscores
    private static final String DEVICE_RPI3 = "rpi3";

    public static String getGPIOForLED() {
        // ref: https://developer.android.com/reference/android/os/Build#DEVICE
        // for model, version, device,..., use Log.d(Build.DEVICE),...
        switch (Build.DEVICE) {
            case DEVICE_RPI3:
                return "BCM6";
            default:
                throw new IllegalStateException("Unknown Build.DEVICE " + Build.DEVICE);
        }
    }
}
