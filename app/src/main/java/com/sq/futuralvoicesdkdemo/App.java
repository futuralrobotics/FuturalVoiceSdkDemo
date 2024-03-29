package com.sq.futuralvoicesdkdemo;

import android.app.Application;

import com.jack.commonlib.MyVoice;

public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        MyVoice.getInstance().init(this);
    }
}
