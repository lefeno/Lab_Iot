package com.example.bluesky;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import com.google.android.things.pio.Gpio;
import com.google.android.things.pio.PeripheralManager;

import java.io.IOException;
import java.lang.ref.PhantomReference;

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
    private static final int LED_RED = 1;
    private static final int LED_GREEN = 2;
    private static final int LED_BLUE = 3;
    private int mLedState = LED_RED;
    private Gpio mLedGpioR, mLedGpioG, mLedGpioB;
    private static final int INTERVAL_BETWEEN_BLINKS_MS = 1000;
    private Handler mHandler = new Handler();

    private boolean mLedStateR = true;
    private boolean mLedStateG = true;
    private boolean mLedStateB = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            PeripheralManager manager = PeripheralManager.getInstance();
            String ledPinR = BoardDefaults.getGPIOForLedR();
//          Config for Led, output, high
            mLedGpioR = manager.openGpio(ledPinR);
            mLedGpioR.setDirection(Gpio.DIRECTION_OUT_INITIALLY_HIGH);

            String ledPinB = BoardDefaults.getGPIOForLedB();
//          Config for Led, output, high
            mLedGpioB = manager.openGpio(ledPinB);
            mLedGpioB.setDirection(Gpio.DIRECTION_OUT_INITIALLY_HIGH);

            Log.d(TAG, "On create");
            String ledPinG = BoardDefaults.getGPIOForLedG();
//          Config for Led, output, high
            mLedGpioG = manager.openGpio(ledPinG);
            mLedGpioG.setDirection(Gpio.DIRECTION_OUT_INITIALLY_HIGH);

            mHandler.post(mBlinkRunnable);

        } catch (IOException e) {
            // untill I do something with GPIO, like openGPIO
            // IOException didn't cause error
            Log.e(TAG, "Error on PeripheralIO API", e);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            mLedGpioR.close();
            mLedGpioB.close();
            mLedGpioG.close();
        } catch (IOException e) {
            Log.e(TAG, "Error on PeripheralIO API", e);
        } finally {
            Log.d(TAG, "On destroy");
            mLedGpioR = null;
            mLedGpioG = null;
            mLedGpioB = null;
        }
    }

    private Runnable mBlinkRunnable = new Runnable() {
        @Override
        public void run() {
            if (mLedGpioB == null || mLedGpioG == null || mLedGpioR == null) {
                return;
            }

            try {
                switch (mLedState) {
                    case LED_RED:
                        mLedStateR = false;
                        mLedStateB = true;
                        mLedStateG = true;
                        mLedState = LED_GREEN;
                        Log.d(TAG, "Led Red");
                        break;
                    case LED_GREEN:
                        mLedStateG = false;
                        mLedStateB = true;
                        mLedStateR = true;
                        mLedState = LED_BLUE;
                        Log.d(TAG, "Led Green");
                        break;
                    case LED_BLUE:
                        mLedStateB = false;
                        mLedStateR = true;
                        mLedStateG = true;
                        mLedState = LED_RED;
                        Log.d(TAG, "Led blue");
                        break;
                    default:
                        break;
                }
                mLedGpioR.setValue(mLedStateR);
                mLedGpioB.setValue(mLedStateB);
                mLedGpioG.setValue(mLedStateG);

                mHandler.postDelayed(mBlinkRunnable, INTERVAL_BETWEEN_BLINKS_MS);
            } catch (IOException e) {
                Log.e(TAG, "Error on PeripheralIO API", e);
            }
        }
    };
}