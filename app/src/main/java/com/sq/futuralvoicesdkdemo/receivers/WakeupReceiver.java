package com.sq.futuralvoicesdkdemo.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import com.sq.futuralvoicesdkdemo.event.WakeupEvent;

import org.greenrobot.eventbus.EventBus;

public class WakeupReceiver extends BroadcastReceiver {
    String TAG = WakeupReceiver.class.getSimpleName();
    String WAKEUP_ACTION = "com.jack.wakeup";

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent == null) {
            Log.e(TAG, "intent is null, do nothing");
            return;
        }
        String action = intent.getAction();
        String type = intent.getStringExtra("type");
        int angle = intent.getIntExtra("angle", -1);
        Log.e(TAG, "type = " + type + ", angle = " + angle);
        if (WAKEUP_ACTION.equals(action)) {
            EventBus.getDefault().post(new WakeupEvent());
        }
    }
}
