package com.example.bluesky.lab02;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.util.Log;

import com.google.android.things.pio.Gpio;
import com.google.android.things.pio.GpioCallback;
import com.google.android.things.pio.PeripheralManager;
import com.google.android.things.pio.Pwm;
import com.google.android.things.pio.UartDevice;
import com.google.android.things.pio.UartDeviceCallback;

import java.io.IOException;
import java.util.Arrays;
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
public class MainActivity extends Activity {

    private static final String TAG = "MainActivity";

    // UART Configuration Parameters
    private static final int BAUD_RATE = 9600;
    private static final int DATA_BITS = 8;
    private static final int STOP_BITS = 1;

    // Declare state for app
    private static final int S_STOP = 0;
    private static final int S_GET1 = 1;
    private static final int S_GET2 = 2;
    private static final int S_GET3 = 3;
    private static final int S_GET4 = 4;
    private static final int S_GET5 = 5;
    private static final int S_START = 6;
    private static int mStateApp = S_STOP;

    private final int STATE_LED = 0;
    private final int STATE_RED = 1;
    private final int STATE_GREEN = 2;
    private final int STATE_BLUE = 3;
    private int state = STATE_LED;

    private static final int CHUNK_SIZE = 512;

    //    Set handler for UART, we will create a seperate worker thread for listen and receive UART
    private HandlerThread mInputThread;
    private Handler mInputHandler;

    private UartDevice mLoopbackDevice;
    private Runnable mTransferUartRunnable = new Runnable() {
        @Override
        public void run() {
            transferUartData();
        }
    };

    private Handler mHandler = new Handler();

    //    Prepare for LED
    private boolean mLedStateR = true, mLedStateG = true, mLedStateB = true;
    private Gpio mLedGpioR, mLedGpioG, mLedGpioB;
    private static final int LED_RED = 1;
    private static final int LED_GREEN = 2;
    private static final int LED_BLUE = 3;
    private int mLedState = LED_RED;

    //    Prepare for PWM
    private static final double MIN_ACTIVE_PULSE_DURATION_MS = 0;
    private static final double MAX_ACTIVE_PULSE_DURATION_MS = 20;
    private static final double PULSE_PERIOD_MS = 20;   // Frequency of 50Hz (1000/20)
    private static final double PULSE_CHANGE_PER_STEP_MS = 0.2;
    private static final int INTERVAL_BETWEEN_STEPS_MS = 50;
    private static final String PWM_NAME = BoardDefaults.getGPIOForLed();
    private boolean mIsPulseIncreasing = true;
    private double mActivePulseDuration;

    private Pwm mPwm;
    private Gpio mButtonGpio;
    private int i = 0;

    private static int intervalBetweenBlink = 2000;
    private static final int INTERVAL_05s = 500;
    private static final int INTERVAL_1s = 1000;
    private static final int INTERVAL_2s = 2000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "Loopback Created");

        // Create a background looper thread for I/O
        mInputThread = new HandlerThread("InputThread");
        mInputThread.start();
        mInputHandler = new Handler(mInputThread.getLooper());

        // Attempt to access the UART device
        try {
//            Initialize UART
            openUart(BoardDefaults.getUartName(), BAUD_RATE);
            // Read any initially buffered data
            mInputHandler.post(mTransferUartRunnable);

//            Initialize LedRGB
            PeripheralManager manager = PeripheralManager.getInstance();
            String ledPinR = BoardDefaults.getGPIOForLedR();
            mLedGpioR = manager.openGpio(ledPinR);
            mLedGpioR.setDirection(Gpio.DIRECTION_OUT_INITIALLY_HIGH);

            String ledPinB = BoardDefaults.getGPIOForLedB();
            mLedGpioB = manager.openGpio(ledPinB);
            mLedGpioB.setDirection(Gpio.DIRECTION_OUT_INITIALLY_HIGH);

            Log.d(TAG, "On  create");
            String ledPinG = BoardDefaults.getGPIOForLedG();
            mLedGpioG = manager.openGpio(ledPinG);
            mLedGpioG.setDirection(Gpio.DIRECTION_OUT_INITIALLY_HIGH);

//            Set PWM
            mActivePulseDuration = MIN_ACTIVE_PULSE_DURATION_MS;
            mPwm = manager.openPwm(PWM_NAME);
            mPwm.setPwmFrequencyHz(1000 / PULSE_PERIOD_MS);
            mPwm.setPwmDutyCycle(100);
            mPwm.setEnabled(true);

//            Set Button
            String buttonPin = BoardDefaults.getGPIOForButton();
            mButtonGpio = manager.openGpio(buttonPin);
            mButtonGpio.setDirection(Gpio.DIRECTION_IN);
            mButtonGpio.setActiveType(Gpio.ACTIVE_HIGH);
            mButtonGpio.setEdgeTriggerType(Gpio.EDGE_FALLING);
        } catch (IOException e) {
            Log.e(TAG, "Unable to open UART device", e);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "Loopback Destroyed");

        mHandler.removeCallbacks(mEx12);
        mHandler.removeCallbacks(mEx34);
        mHandler.removeCallbacks(mRunnableLedR);
        mHandler.removeCallbacks(mRunnableLedG);
        mHandler.removeCallbacks(mRunnableLedB);

        // Terminate the worker thread
        if (mInputThread != null) {
            mInputThread.quitSafely();
        }

        // Attempt to close the UART device
        try {
            closeUart();
        } catch (IOException e) {
            Log.e(TAG, "Error closing UART device:", e);
        } finally {
            mPwm = null;
            mLedGpioR = null;
            mLedGpioG = null;
            mLedGpioB = null;
            mButtonGpio = null;
        }
    }

    /**
     * Callback invoked when UART receives new incoming data.
     */
    private UartDeviceCallback mCallback = new UartDeviceCallback() {
        @Override
        public boolean onUartDeviceDataAvailable(UartDevice uart) {
            Log.d(TAG, "Enter UartDeviceCallback");
            // Queue up a data transfer
            transferUartData();
            //Continue listening for more interrupts
            return true;
        }

        @Override
        public void onUartDeviceError(UartDevice uart, int error) {
            Log.w(TAG, uart + ": Error event " + error);
        }
    };

    /* Private Helper Methods */

    /**
     * Access and configure the requested UART device for 8N1.
     *
     * @param name     Name of the UART peripheral device to open.
     * @param baudRate Data transfer rate. Should be a standard UART baud,
     *                 such as 9600, 19200, 38400, 57600, 115200, etc.
     * @throws IOException if an error occurs opening the UART port.
     */
    private void openUart(String name, int baudRate) throws IOException {
        Log.d(TAG, "Enter openUart");
        mLoopbackDevice = PeripheralManager.getInstance().openUartDevice(name);
        // Configure the UART
        mLoopbackDevice.setBaudrate(baudRate);
        mLoopbackDevice.setDataSize(DATA_BITS);
        mLoopbackDevice.setParity(UartDevice.PARITY_NONE);
        mLoopbackDevice.setStopBits(STOP_BITS);

        mLoopbackDevice.registerUartDeviceCallback(mInputHandler, mCallback);
    }

    /**
     * Close the UART device connection, if it exists
     */
    private void closeUart() throws IOException {
        if (mLoopbackDevice != null) {
            Log.d(TAG, "Enter closeUart");
            mLoopbackDevice.unregisterUartDeviceCallback(mCallback);
            try {
                mLoopbackDevice.close();
            } finally {
                mLoopbackDevice = null;
            }
        }
    }

    /**
     * Loop over the contents of the UART RX buffer, transferring each
     * one back to the TX buffer to create a loopback service.
     * <p>
     * Potentially long-running operation. Call from a worker thread.
     */
    private void transferUartData() {
        if (mLoopbackDevice != null) {
            Log.d(TAG, "Enter transferUartData");
            // Loop until there is no more data in the RX buffer.
            try {
//                it will write from buffer[0]-> (CHUNK_SIZE - 1)
//                if we send hello, buffer[0] = 'h' = 104
//                if use putty, can read only a char, should use Terminal instead
//                the buffer limited to 512 bytes
                byte[] buffer = new byte[CHUNK_SIZE];
                int read;
//                Log.d(TAG,"Buffer.length" + buffer.length); -> 512
                while ((read = mLoopbackDevice.read(buffer, buffer.length)) > 0) {
                    changeState(buffer[0]);

                    int count = mLoopbackDevice.write(buffer, read);
//                    Log.d(TAG,"Buffer: " + count + " " + Arrays.toString(buffer));
                }
            } catch (IOException e) {
                Log.w(TAG, "Unable to transfer data over UART", e);
            }
        }
    }

    private void changeState(byte in) {
        if (in == 'O') {
            Log.d(TAG, "App starts ready to receive commands");
            mStateApp = S_START;
        }
        if (in == 'F') {
            Log.d(TAG, "App stops any running");
            mStateApp = S_STOP;
        }

        if (mStateApp != S_STOP) {
            switch (in) {
                case '1':
                    try {
                        mPwm.setPwmDutyCycle(100);
                    } catch (IOException e) {
                        Log.e(TAG, "Error: ", e);
                    }

                    mStateApp = S_GET1;
                    intervalBetweenBlink = 1000;
                    Log.d(TAG, "Ex 1 running");
                    mHandler.removeCallbacks(mEx12);
                    mHandler.post(mEx12);
                    break;
                case '2':
                    try {
                        mPwm.setPwmDutyCycle(100);
                    } catch (IOException e) {
                        Log.e(TAG, "Error: ", e);
                    }
                    intervalBetweenBlink = INTERVAL_2s;
                    mStateApp = S_GET2;
                    Log.d(TAG, "Ex 2 running");
                    try {
                        mButtonGpio.registerGpioCallback(mButtonListener);
                    } catch (IOException e) {
                        Log.e(TAG, "Error: ", e);
                    }
                    mHandler.removeCallbacks(mEx12);
                    mHandler.post(mEx12);
                    break;
                case '3':
                    mStateApp = S_GET3;
                    Log.d(TAG, "Ex 3 running");
                    mHandler.removeCallbacks(mEx34);
                    mHandler.post(mEx34);
                    break;
                case '4':
                    mStateApp = S_GET4;
                    Log.d(TAG, "Ex 4 running");
                    try {
                        mButtonGpio.registerGpioCallback(mButtonListener);
                    } catch (IOException e) {
                        Log.e(TAG, "Error: ", e);
                    }
                    mHandler.removeCallbacks(mEx34);
                    mHandler.post(mEx34);
                    break;
                case '5':
                    mStateApp = S_GET5;
                    Log.d(TAG, "Ex 5 running");
                    mHandler.post(mRunnableLedB);
                    mHandler.post(mRunnableLedG);
                    mHandler.post(mRunnableLedR);
                    break;
                default:
                    Log.d(TAG, "Unknown command");
                    break;
            }
        }
    }

    private Runnable mRunnableLedR = new Runnable() {
        @Override
        public void run() {
            mLedStateR = !mLedStateR;
            try{
                mLedGpioR.setValue(mLedStateR);
                Log.d(TAG,"Red");
                if(mStateApp == S_GET5){
                    mHandler.postDelayed(mRunnableLedR,INTERVAL_05s);
                }
            } catch (IOException e){
                Log.e(TAG,"Error: ",e);
            }
        }
    };

    private Runnable mRunnableLedG = new Runnable() {
        @Override
        public void run() {
            mLedStateG = !mLedStateG;
            try{
                Log.d(TAG,"Green");
                mLedGpioG.setValue(mLedStateG);
                if(mStateApp == S_GET5){
                    mHandler.postDelayed(mRunnableLedG,INTERVAL_1s);
                }
            } catch (IOException e){
                Log.e(TAG,"Error: ",e);
            }
        }
    };

    private Runnable mRunnableLedB = new Runnable() {
        @Override
        public void run() {
            mLedStateB = !mLedStateB;
            try{
                Log.d(TAG,"Blue");
                mLedGpioB.setValue(mLedStateB);
                if(mStateApp == S_GET5){
                    mHandler.postDelayed(mRunnableLedB,INTERVAL_2s);
                }
            } catch (IOException e){
                Log.e(TAG,"Error: ",e);
            }
        }
    };

    private GpioCallback mButtonListener = new GpioCallback() {
        @Override
        public boolean onGpioEdge(Gpio gpio) {
            if (mStateApp == S_GET2) {
                switch (intervalBetweenBlink) {
                    case INTERVAL_2s:
                        intervalBetweenBlink = 1000;
                        Log.i(TAG, "Blink in 1s");
                        break;

                    case INTERVAL_1s:
                        intervalBetweenBlink = 500;
                        Log.i(TAG, "Blink in 0.5s");
                        break;

                    case INTERVAL_05s:
                        intervalBetweenBlink = 2000;
                        Log.i(TAG, "Blink in 2s");
                        break;
                    default:
                        throw new IllegalStateException("State is incorrect");
                }
            } else if (mStateApp == S_GET4) {
                switch (state) {
                    case STATE_LED:
                        state = STATE_RED;
                        break;
                    case STATE_RED:
                        state = STATE_GREEN;
                        break;
                    case STATE_GREEN:
                        state = STATE_BLUE;
                        break;
                    case STATE_BLUE:
                        state = STATE_LED;
                        break;
                    default:
                        Log.e(TAG, "Error");
                        break;
                }
            }
            return true;
        }
    };

    private Runnable mEx12 = new Runnable() {
        @Override
        public void run() {
            if (mLedGpioB == null || mLedGpioG == null || mLedGpioR == null || mButtonGpio == null) {
                return;
            }
            changeLedState();
            if (mStateApp == S_GET1 || mStateApp == S_GET1) {
                mHandler.postDelayed(mEx12, intervalBetweenBlink);
            }
        }
    };

    private Runnable mEx34 = new Runnable() {
        @Override
        public void run() {
            if (mPwm == null || mLedGpioB == null || mLedGpioG == null || mLedGpioR == null) {
                return;
            }
            if (mIsPulseIncreasing) {
                mActivePulseDuration += PULSE_CHANGE_PER_STEP_MS;
            } else {
                mActivePulseDuration -= PULSE_CHANGE_PER_STEP_MS;
            }

            if (mActivePulseDuration > MAX_ACTIVE_PULSE_DURATION_MS) {
                mActivePulseDuration = MAX_ACTIVE_PULSE_DURATION_MS;
                mIsPulseIncreasing = !mIsPulseIncreasing;
            } else if (mActivePulseDuration < MIN_ACTIVE_PULSE_DURATION_MS) {
                mActivePulseDuration = MIN_ACTIVE_PULSE_DURATION_MS;
                mIsPulseIncreasing = !mIsPulseIncreasing;
            }

            ++i;
            if (i == 60) {
                changeLedState();
                i = 0;
                mActivePulseDuration = MIN_ACTIVE_PULSE_DURATION_MS;
            }

            try {
                if (mStateApp == S_GET3){
                    mPwm.setPwmDutyCycle(100 * mActivePulseDuration / PULSE_PERIOD_MS);
                }
                else if (state == STATE_LED || state == mLedState - 1 || (state == STATE_BLUE && mLedState == LED_RED)) {
                    mPwm.setPwmDutyCycle(100 * mActivePulseDuration / PULSE_PERIOD_MS);
                } else {
                    mPwm.setPwmDutyCycle(100);
                }
            } catch (IOException e) {
                Log.d(TAG, "Error", e);
            }

            if (mStateApp == S_GET3 || mStateApp == S_GET4) {
                mHandler.postDelayed(mEx34, INTERVAL_BETWEEN_STEPS_MS);
            }
        }
    };

    private void changeLedState() {
        switch (mLedState) {
            case LED_RED:
                mLedStateR = false;
                mLedStateB = true;
                mLedStateG = true;
                mLedState = LED_GREEN;
//                        Log.d(TAG, "Led Red");
                break;
            case LED_GREEN:
                mLedStateG = false;
                mLedStateB = true;
                mLedStateR = true;
                mLedState = LED_BLUE;
//                        Log.d(TAG, "Led Green");
                break;
            case LED_BLUE:
                mLedStateB = false;
                mLedStateR = true;
                mLedStateG = true;
                mLedState = LED_RED;
//                        Log.d(TAG, "Led blue");
                break;
            default:
                break;
        }
        try {
            mLedGpioR.setValue(mLedStateR);
            mLedGpioB.setValue(mLedStateB);
            mLedGpioG.setValue(mLedStateG);
        } catch (IOException e) {
            Log.d(TAG, "Error: ", e);
        }
    }

    private void reset(){

    }
}