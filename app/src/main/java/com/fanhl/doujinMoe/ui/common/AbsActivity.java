package com.fanhl.doujinMoe.ui.common;

import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.fanhl.doujinMoe.App;
import com.fanhl.doujinMoe.R;
import com.fanhl.doujinMoe.model.Book;
import com.fanhl.doujinMoe.util.DownloadManager;

/**
 * Created by fanhl on 15/10/30.
 */
public abstract class AbsActivity extends AppCompatActivity implements DownloadManager.OnDownloadManagerInteractionListener {

    protected App app;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.primary));
            getWindow().setNavigationBarColor(getResources().getColor(R.color.primary));
        }

        app = ((App) getApplication());
    }

    @Override
    protected void onResume() {
        super.onResume();
        app.getDownloadManager().registerOnDownloadManagerInteractionListener(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        app.getDownloadManager().unregisterOnDownloadManagerInteractionListener(this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            this.onBackPressed();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDMDownloadSuccess(Book book) {
        //在需要响应的Activity中实现
    }

    @Override
    public void onDMDownloadFail(Book book) {
        //在需要响应的Activity中实现
    }
}
