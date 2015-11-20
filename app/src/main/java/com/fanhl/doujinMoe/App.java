package com.fanhl.doujinMoe;

import android.app.Application;

import com.fanhl.doujinMoe.util.DownloadManager;

/**
 * Created by fanhl on 15/11/18.
 */
public class App extends Application {
    DownloadManager downloadManager;

    @Override
    public void onCreate() {
        super.onCreate();
        downloadManager = DownloadManager.getInstance(this);
    }

    public DownloadManager getDownloadManager() {
        return downloadManager;
    }
}
