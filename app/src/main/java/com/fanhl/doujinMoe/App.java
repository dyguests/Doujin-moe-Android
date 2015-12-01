package com.fanhl.doujinMoe;

import android.app.Application;

import com.fanhl.doujinMoe.rest.DoujinmoeClient;
import com.fanhl.doujinMoe.util.DownloadManager;
import com.fanhl.doujinMoe.util.LocalManager;

/**
 * Created by fanhl on 15/11/18.
 */
public class App extends Application {

    DoujinmoeClient client;

    DownloadManager downloadManager;
    LocalManager    localManager;

    @Override
    public void onCreate() {
        super.onCreate();

        client = new DoujinmoeClient();

        localManager = LocalManager.getInstance(this);
        downloadManager = DownloadManager.getInstance(this, localManager);
    }

    public DoujinmoeClient getClient() {
        return client;
    }

    public DownloadManager getDownloadManager() {
        return downloadManager;
    }

    public LocalManager getLocalManager() {
        return localManager;
    }
}
