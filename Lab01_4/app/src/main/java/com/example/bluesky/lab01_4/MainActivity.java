package com.example.bluesky.lab01_4;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import com.google.android.things.pio.Gpio;
import com.google.android.things.pio.GpioCallback;
import com.google.android.things.pio.PeripheralManager;

import java.io.IOException;

/**
 * Skeleton of an Android Things activity.
 * <p>
 * Android Things peripheral APIs are accessible through the class
 * PeripheralManagerService. For example, the snippet below will open a GPIO pin and
 * set it to HIGH:
 * <p>
 * <pre>{@code
 * PeripheralManagerService service = new PeripheralManagerService();
 * mLedGpio = service.openGpio("BCM6");
 * mLedGpio.setDirection(Gpio.DIRECTION_OUT_INITIALLY_LOW);
 * mLedGpio.setValue(true);
 * }</pre>
 * <p>
 * For more complex peripherals, look for an existing user-space driver, or implement one if none
 * is available.
 *
 * @see <a href="https://github.com/androidthings/contrib-drivers#readme">https://github.com/androidthings/contrib-drivers#readme</a>
 */
public class MainActivity extends Activity {
    private static final String TAG = MainActivity.class.getSimpleName();
    private static final String ledPin = BoardDefaults.getGPIOForLed();
    private static final String buttonPin = BoardDefaults.getGPIOForButton();

    private Gpio mButtonGpio, mLedGpio;
    private boolean mLedState = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG, "Starting ButtonActivity hihi");
        try {
            PeripheralManager manager = PeripheralManager.getInstance();

            mLedGpio = manager.openGpio(ledPin);
            mLedGpio.setDirection(Gpio.DIRECTION_OUT_INITIALLY_LOW);
            mLedGpio.setValue(false);

            mButtonGpio = manager.openGpio(buttonPin);
            mButtonGpio.setDirection(Gpio.DIRECTION_IN);
            mButtonGpio.setActiveType(Gpio.ACTIVE_HIGH);
            mButtonGpio.setEdgeTriggerType(Gpio.EDGE_FALLING);

            Log.i(TAG,"Gonna go register for button");
            mButtonGpio.registerGpioCallback(new GpioCallback() {
                @Override
                public boolean onGpioEdge(Gpio gpio) {
                    Log.i(TAG, "Button pressed");
                    try{
                        mLedState = !mLedState;
                        mLedGpio.setValue(mLedState);
                    } catch (IOException e){
                        Log.e(TAG,"IO Error: ",e);
                    }

                    return true;
                }
            });
        } catch (IOException e) {
            Log.e(TAG, "Error press button", e);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mButtonGpio != null) {
            // Close the Gpio pin
            Log.i(TAG, "Closing Button GPIO pin");
            try {
                mLedGpio.close();
                mButtonGpio.close();
            } catch (IOException e) {
                Log.e(TAG, "Error on PeripheralIO API27", e);
            } finally {
                mButtonGpio = null;
                mLedGpio = null;
            }
        }
    }
}
