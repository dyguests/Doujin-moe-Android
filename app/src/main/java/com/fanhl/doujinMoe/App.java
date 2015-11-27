package com.fanhl.doujinMoe;

import android.app.Application;

import com.fanhl.doujinMoe.util.DownloadManager;
import com.fanhl.doujinMoe.util.LocalManager;

/**
 * Created by fanhl on 15/11/18.
 */
public class App extends Application {
    DownloadManager downloadManager;
    LocalManager    localManager;

    @Override
    public void onCreate() {
        super.onCreate();
        localManager = LocalManager.getInstance(this);
        downloadManager = DownloadManager.getInstance(this,localManager);
    }

    public DownloadManager getDownloadManager() {
        return downloadManager;
    }

    public LocalManager getLocalManager() {
        return localManager;
    }
}
