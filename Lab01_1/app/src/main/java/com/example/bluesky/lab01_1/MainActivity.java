package com.example.bluesky.lab01_1;

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
    private Gpio mLedGpioR, mLedGpioG, mLedGpioB;
    private static final int INTERVAL_BETWEEN_BLINKS_MS = 1000;
    private Handler mHandler = new Handler();

//    private boolean mLedStateR = false;
//    private boolean mLedStateG = false;
//    private boolean mLedStateB = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try{
            PeripheralManager manager = PeripheralManager.getInstance();
            String ledPinR = BoardDefault.getGPIOforLedR();
//          Config for Led, output, low
            mLedGpioR = manager.openGpio(ledPinR);
            mLedGpioR.setDirection(Gpio.DIRECTION_OUT_INITIALLY_LOW);

            String ledPinB = BoardDefault.getGPIOforLedB();
//          Config for Led, output, low
            mLedGpioB = manager.openGpio(ledPinB);
            mLedGpioB.setDirection(Gpio.DIRECTION_OUT_INITIALLY_LOW);

            Log.d(TAG,"On create");
            String ledPinG = BoardDefault.getGPIOforLedG();
//          Config for Led, output, low
            mLedGpioG = manager.openGpio(ledPinG);
            mLedGpioG.setDirection(Gpio.DIRECTION_OUT_INITIALLY_LOW);

            mHandler.post(mBlinkRunnableR);

            mHandler.post(mBlinkRunnableG);

            mHandler.post(mBlinkRunnableB);

        } catch (IOException e){
            // untill I do something with GPIO, like openGPIO
            // IOException didn't cause error
            Log.e(TAG,"Error on PeripheralIO API", e);
        }
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        try{
            mLedGpioR.close();
            mLedGpioB.close();
            mLedGpioG.close();
        }
        catch (IOException e){
            Log.e(TAG,"Error on PeripheralIO API", e);
        }
        finally {
            Log.d(TAG,"On destroy");
            mLedGpioR = null;
            mLedGpioG = null;
            mLedGpioB = null;
        }
    }

    private Runnable mBlinkRunnableR = new Runnable() {
        @Override
        public void run() {
            if(mLedGpioB == null || mLedGpioG == null || mLedGpioR == null){
                return;
            }

            try{
                mLedGpioR.setValue(false);
                mLedGpioB.setValue(true);
                mLedGpioG.setValue(true);

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e){
                    Log.e(TAG,"error: ", e);
                }
                Log.d(TAG, "Led Red");
                mHandler.postDelayed(mBlinkRunnableR,INTERVAL_BETWEEN_BLINKS_MS);
            } catch (IOException e){
                Log.e(TAG,"Error on PeripheralIO API", e);
            }
        }
    };

    private Runnable mBlinkRunnableG = new Runnable() {
        @Override
        public void run() {
            if(mLedGpioB == null || mLedGpioG == null || mLedGpioR == null){
                return;
            }

            try{
                mLedGpioR.setValue(true);
                mLedGpioB.setValue(true);
                mLedGpioG.setValue(false);

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e){
                    Log.e(TAG,"error: ", e);
                }
                Log.d(TAG, "Led Green");
                mHandler.postDelayed(mBlinkRunnableG,INTERVAL_BETWEEN_BLINKS_MS);
            } catch (IOException e){
                Log.e(TAG,"Error on PeripheralIO API", e);
            }
        }
    };

    private Runnable mBlinkRunnableB = new Runnable() {
        @Override
        public void run() {
            if(mLedGpioB == null || mLedGpioG == null || mLedGpioR == null){
                return;
            }

            try{
                mLedGpioR.setValue(true);
                mLedGpioB.setValue(false);
                mLedGpioG.setValue(true);

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e){
                    Log.e(TAG,"error: ", e);
                }
                Log.d(TAG, "Led Blue");
                mHandler.postDelayed(mBlinkRunnableB,INTERVAL_BETWEEN_BLINKS_MS);
            } catch (IOException e){
                Log.e(TAG,"Error on PeripheralIO API", e);
            }
        }
    };
}
