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
        downloadManager = DownloadManager.getInstance(this);
        localManager = LocalManager.getInstance(this);
    }

    public DownloadManager getDownloadManager() {
        return downloadManager;
    }

    public LocalManager getLocalManager() {
        return localManager;
    }
}
