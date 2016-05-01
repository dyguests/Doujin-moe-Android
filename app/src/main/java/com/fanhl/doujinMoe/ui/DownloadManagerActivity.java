package com.fanhl.doujinMoe.ui;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v13.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.fanhl.doujinMoe.R;
import com.fanhl.doujinMoe.interfaces.OnDownloadManagerBookChangeListener;
import com.fanhl.doujinMoe.model.Book;
import com.fanhl.doujinMoe.ui.common.AbsActivity;
import com.fanhl.doujinMoe.ui.fragment.downloadManager.DownloadFailFragment;
import com.fanhl.doujinMoe.ui.fragment.downloadManager.DownloadSuccessFragment;
import com.fanhl.doujinMoe.ui.fragment.downloadManager.DownloadingFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class DownloadManagerActivity extends AbsActivity {
    public static final String TAG = DownloadManagerActivity.class.getSimpleName();

    @Bind(R.id.toolbar)
    Toolbar              toolbar;
    @Bind(R.id.tabs)
    TabLayout            tabLayout;
    @Bind(R.id.appbar)
    AppBarLayout         mAppbar;
    @Bind(R.id.container)
    ViewPager            mViewPager;
    @Bind(R.id.fab)
    FloatingActionButton fab;
    @Bind(R.id.main_content)
    CoordinatorLayout    mMainContent;

    private DownloadManagerPagerAdapter mSectionsPagerAdapter;

    List<OnDownloadManagerBookChangeListener> onDMBookChangeListeners;

    public static void launch(Activity activity) {
        Intent intent = new Intent(activity, DownloadManagerActivity.class);
//        intent.setFlags(Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
//        intent.putExtra(EXTRA_BOOK_DATA, GsonUtil.json(book));
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download_manager);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        mSectionsPagerAdapter = new DownloadManagerPagerAdapter(getFragmentManager());
        mViewPager.setAdapter(mSectionsPagerAdapter);
        mViewPager.setOffscreenPageLimit(2);//使3个fragment都保持活动
        tabLayout.setupWithViewPager(mViewPager);

        fab.setOnClickListener(view -> Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show());


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_download_manager, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDMDownloadSuccess(Book book) {
        Snackbar.make(mMainContent, String.format(getString(R.string.download_book_success), book.name), Snackbar.LENGTH_LONG).setAction(R.string.action_check, v -> {
            // FIXME: 15/11/20 跳转到下载列表页面.
        }).show();

        dispatchOnDownloadManagerBookChanged(book, true);
    }

    @Override
    public void onDMDownloadFail(Book book) {
        Log.d(TAG, "onDMDownloadFail 书籍(" + book.name + ")下载失败");
        Snackbar.make(mMainContent, String.format(getString(R.string.download_book_fail), book.name), Snackbar.LENGTH_LONG).show();

        dispatchOnDownloadManagerBookChanged(book, false);
    }

    private void dispatchOnDownloadManagerBookChanged(Book book, boolean success) {
        if (onDMBookChangeListeners != null) {
            for (OnDownloadManagerBookChangeListener listener : onDMBookChangeListeners) {
                if (listener != null) {
                    listener.onDownloadBookChanged(book, success);
                }
            }
        }
    }

    public void addOnDownloadManagerBookChangeListener(OnDownloadManagerBookChangeListener onDownloadManagerBookChangeListener) {
        if (onDMBookChangeListeners == null) {
            onDMBookChangeListeners = new ArrayList<>();
        }
        onDMBookChangeListeners.add(onDownloadManagerBookChangeListener);
    }

    public void removeOnDownloadManagerBookChangeListener(OnDownloadManagerBookChangeListener onDownloadManagerBookChangeListener) {
        if (onDMBookChangeListeners != null) {
            onDMBookChangeListeners.remove(onDownloadManagerBookChangeListener);
        }
    }

    public void clearOnDownloadManagerBookChangeListener() {
        if (onDMBookChangeListeners != null) {
            onDMBookChangeListeners.clear();
        }
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class DownloadManagerPagerAdapter extends FragmentPagerAdapter {

        private DownloadingFragment     downloadingFragment;
        private DownloadSuccessFragment downloadSuccessFragment;
        private DownloadFailFragment    downloadFailFragment;

        public DownloadManagerPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    if (downloadingFragment == null) {
                        downloadingFragment = DownloadingFragment.newInstance();
                    }
                    return downloadingFragment;
                case 1:
                    if (downloadSuccessFragment == null) {
                        downloadSuccessFragment = DownloadSuccessFragment.newInstance();
                    }
                    return downloadSuccessFragment;
                case 2:
                    if (downloadFailFragment == null) {
                        downloadFailFragment = DownloadFailFragment.newInstance();
                    }
                    return downloadFailFragment;
            }
            return null;
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return getString(R.string.fragment_downloading_title);
                case 1:
                    return getString(R.string.fragment_download_success_title);
                case 2:
                    return getString(R.string.fragment_download_fail_title);
            }
            return null;
        }
    }
}
