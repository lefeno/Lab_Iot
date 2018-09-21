package com.example.bluesky.lab01_blinkled;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import com.google.android.things.pio.Gpio;
import com.google.android.things.pio.PeripheralManager;

import java.io.IOException;
import java.util.List;

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

//Type of console
//Log.v(); // Verbose
//Log.d(); // Debug
//Log.i(); // Info
//Log.w(); // Warning
//Log.e(); // Error
//Ref: https://stackoverflow.com/questions/7959263/android-log-v-log-d-log-i-log-w-log-e-when-to-use-each-one

public class MainActivity extends Activity {
    private static final String TAG = MainActivity.class.getSimpleName();
    private static final int INTERVAL_BETWEEN_BLINKS_MS = 2000;
    private Handler mHandler = new Handler();
    private Gpio mLedGpio;
    private boolean mLedState = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
//      Explain about savedInstanceState:
//      https://content.pivotal.io/blog/android-savedinstancestate-bundle-faq

//      In JV, when u inherit from a class, u can completely replace the method
//      or extending the existing parent class' method
//      By calling super.onCreate(savedInstanceState); u tell VM to run your
//      code in addition to the existing code in the onCreate() of the parent
//      class. Leave this line out -> only your code is run
//      However, you must include this super call in your method, because if you
//      don't then the onCreate() code in Activity is never run, and your app
//      will run into all sorts of problem like having no Context assigned to the
//      Activity (though you'll hit a SuperNotCalledException before you have a
//      chance to figure out that you have no context).
        super.onCreate(savedInstanceState);
        Log.i(TAG, "Starting BlinkActivity");
        try {
            String pinName = BoardDefaults.getGPIOForLED();
            PeripheralManager manager = PeripheralManager.getInstance();
            List<String> portList = manager.getGpioList();
            if(portList.isEmpty()){
                Log.i(TAG, "No GPIO port is available on this device");
            } else {
                Log.i(TAG,"List of available ports: " + portList);
            }
            mLedGpio = manager.openGpio(pinName);
            mLedGpio.setDirection(Gpio.DIRECTION_OUT_INITIALLY_LOW);
            Log.i(TAG, "Start blinking LED GPIO pin");
            mHandler.post(mBlinkRunnable);
        } catch (IOException e) {
            Log.e(TAG, "Error on PeripheralIO API", e);
        }
    }
//  Use thread in Android:
//  http://laptrinhandroid.vn/huong-dan-su-dung-thread-va-handler-trong-android/
    private Runnable mBlinkRunnable = new Runnable() {
        @Override
        public void run() {
            if (mLedGpio == null) {// Exit Runnable if the GPIO is already closed
                return;
            }
            try {
                // Toggle the GPIO state
                mLedState = !mLedState;
                mLedGpio.setValue(mLedState);
                Log.d(TAG, "State set to " + mLedState);
                // Reschedule the same runnable in {#INTERVAL_BETWEEN_BLINKS_MS}milliseconds
                mHandler.postDelayed(mBlinkRunnable, INTERVAL_BETWEEN_BLINKS_MS);
            } catch (IOException e) {
                Log.e(TAG, "Error on PeripheralIO API", e);
            }
        }
    };
    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Remove pending blink Runnable from the handler.
        mHandler.removeCallbacks(mBlinkRunnable);
        // Close the Gpio pin.
        Log.i(TAG, "Closing LED GPIO pin");
        try {
            mLedGpio.close();
//          A port configured as an output retains its last output value even
//          after the close() method is called.
        } catch (IOException e) {
            Log.e(TAG, "Error on PeripheralIO API", e);
        } finally {
            mLedGpio = null;
        }
    }

}
