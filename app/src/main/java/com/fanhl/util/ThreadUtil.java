package com.fanhl.util;

import android.os.Handler;
import android.os.HandlerThread;
import android.support.annotation.NonNull;

import static android.os.Process.THREAD_PRIORITY_BACKGROUND;

/**
 * Created by fanhl on 15/11/20.
 */
public class ThreadUtil {
    /**
     * 创建后台线程,Rx用
     *
     * @param handlerThreadName
     * @return
     */
    @NonNull
    public static Handler createBackgroundHandler(String handlerThreadName) {
        HandlerThread downloadThread = new HandlerThread(handlerThreadName, THREAD_PRIORITY_BACKGROUND);
        downloadThread.start();
        return new Handler(downloadThread.getLooper());
    }

    public static void sleep(int time) {
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
