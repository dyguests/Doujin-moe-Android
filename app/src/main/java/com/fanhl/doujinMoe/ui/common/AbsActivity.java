package com.fanhl.doujinMoe.ui.common;

import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.fanhl.doujinMoe.App;
import com.fanhl.doujinMoe.R;

/**
 * Created by fanhl on 15/10/30.
 */
public abstract class AbsActivity extends AppCompatActivity {

    protected App app;

    /**
     * 后台任务用handler
     */
//    protected Handler downloadHandler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.primary));
            getWindow().setNavigationBarColor(getResources().getColor(R.color.primary));
        }

        app = ((App) getApplication());

//        BackgroundThread backgroundThread = new BackgroundThread();
//        backgroundThread.start();
//        downloadHandler = new Handler(backgroundThread.getLooper());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            this.onBackPressed();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


//    public static class BackgroundThread extends HandlerThread {
//        public BackgroundThread() {
//            super("SchedulerSample-BackgroundThread", THREAD_PRIORITY_BACKGROUND);
//        }
//    }
}
