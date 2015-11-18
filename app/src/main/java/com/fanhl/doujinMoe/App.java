package com.fanhl.doujinMoe;

import android.app.Application;

import com.facebook.drawee.backends.pipeline.Fresco;

/**
 * Created by fanhl on 15/11/18.
 */
public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Fresco.initialize(this);
    }
}
