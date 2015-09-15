package com.bean.swstack.beanledstrip;

import android.content.Context;
import android.util.Log;

import com.punchthrough.bean.sdk.Bean;
import com.punchthrough.bean.sdk.BeanDiscoveryListener;
import com.punchthrough.bean.sdk.BeanListener;
import com.punchthrough.bean.sdk.BeanManager;
import com.punchthrough.bean.sdk.message.BeanError;
import com.punchthrough.bean.sdk.message.ScratchBank;


/**************************************
 * Message Definitions
 *************************************/
enum Command {
    get_led_power_state,
    set_intensity,
    set_red,
    set_green,
    set_blue,
}


/**************************************
 * Global State
 *************************************/
enum LEDState {
    ON,
    OFF,
}


public class BeanLEDStrip {

    private static final String TAG = "BeanLEDStrip";
    private static final String BEAN_NAME = "bean_led_strip";

    protected Context context;
    protected Bean ledStrip;
    protected LEDState ledState;

    public BeanLEDStrip(Context context) {
        this.context = context;
        this.ledStrip = null;
        this.ledState = LEDState.OFF;
    }

    public Boolean isConnected() {
        return this.ledStrip.isConnected();
    }

    public void sync() {
        Log.d(TAG, "Start Sync...");
        if (ledStrip == null) {
            Log.d(TAG, "Starting Bean discovery...");
            BeanManager.getInstance().startDiscovery(discoveryListener);
        }
        if (!ledStrip.isConnected()) {
            Log.d(TAG, "Starting to connect to bean named " + BEAN_NAME);
            ledStrip.connect(this.context, beanListener);
        }
        Log.d(TAG, "Sync complete...");
    }

    private BeanDiscoveryListener discoveryListener = new BeanDiscoveryListener() {
        @Override
        public void onBeanDiscovered(Bean bean, int rssi) {
            Log.d(TAG, "Bean found");

            if (bean.getDevice().getName().equals(BEAN_NAME)) {
                Log.d(TAG, "LED strip found!");
                ledStrip = bean;
                sync();
            }
        }

        @Override
        public void onDiscoveryComplete() {
            Log.d(TAG, "Bean Discovery Complete");
        }
    };

    private BeanListener beanListener = new BeanListener() {
        @Override
        public void onConnected() {
            Log.d(TAG, "Connected");
        }

        @Override
        public void onConnectionFailed() {
            Log.d(TAG, "Connection Failed");
        }

        @Override
        public void onDisconnected() {
            Log.d(TAG, "Disconnected");
        }

        @Override
        public void onSerialMessageReceived(byte[] bytes) {
            Log.d(TAG, "Serial Message Received");
        }

        @Override
        public void onScratchValueChanged(ScratchBank scratchBank, byte[] bytes) {
            Log.d(TAG, "Scratch Value Changed");
        }

        @Override
        public void onError(BeanError beanError) {
            Log.d(TAG, "Bean Error");
        }
    };

}
