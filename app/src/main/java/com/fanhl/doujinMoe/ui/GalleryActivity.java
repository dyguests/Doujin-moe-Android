package com.fanhl.doujinMoe.ui;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.view.DirectionalViewPager;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.AppCompatSeekBar;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.SeekBar;

import com.fanhl.doujinMoe.R;
import com.fanhl.doujinMoe.api.BookApi;
import com.fanhl.doujinMoe.model.Book;
import com.fanhl.doujinMoe.ui.adapter.GalleryPagerAdapter;
import com.fanhl.doujinMoe.ui.common.AbsActivity;
import com.fanhl.doujinMoe.util.FullScreenHelper;
import com.fanhl.doujinMoe.util.Utility;
import com.fanhl.util.GsonUtil;

import butterknife.Bind;
import butterknife.ButterKnife;

public class GalleryActivity extends AbsActivity {
    public static final  String TAG                    = GalleryActivity.class.getSimpleName();
    private static final int    AUTO_HIDE_DELAY_MILLIS = 1500;
    public static final  String EXTRA_BOOK_DATA        = "EXTRA_BOOK_DATA";

    @Bind(R.id.toolbar)
    Toolbar              toolbar;
    @Bind(R.id.pager)
    DirectionalViewPager mPager;
    @Bind(R.id.app_bar)
    LinearLayout         mAppBar;
    @Bind(R.id.bottom_bar)
    LinearLayout         mBottomBar;
    @Bind(R.id.seekBar)
    AppCompatSeekBar     mSeekBar;
    @Bind(R.id.total_pages_text)
    AppCompatTextView    mTotalPagesText;

    //custom

    private Book book;

    private FullScreenHelper    mFullScreenHelper;
    private GalleryPagerAdapter mPagerAdapter;

    public static void launch(Activity activity, Book book) {
        Intent intent = new Intent(activity, GalleryActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
        intent.putExtra(EXTRA_BOOK_DATA, GsonUtil.json(book));
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && !Utility.isChrome()) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }

        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().setStatusBarColor(Color.TRANSPARENT);
            getWindow().setNavigationBarColor(Color.TRANSPARENT);
        }

        mFullScreenHelper = new FullScreenHelper(this);
        // 别问我为什么这么干 让我先冷静一下→_→
        mFullScreenHelper.setFullScreen(true);
        mFullScreenHelper.setFullScreen(false);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        setContentView(R.layout.activity_gallery);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        book = GsonUtil.obj(intent.getStringExtra(EXTRA_BOOK_DATA), Book.class);
        assert book != null;
        int tmpPosition = book.position;//详细页点击第三张page时,position=2,此时本地bookJson中position=3.这种情况下使用2这个值.
        //从本地取最新的数据
        book = BookApi.getBookFormJson(this, book);
        book.position = tmpPosition;

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        setTitle(book.name);

        mPagerAdapter = new GalleryPagerAdapter(getFragmentManager(), book);
        mPager.setAdapter(mPagerAdapter);
        mPager.setCurrentItem(book.position, false);
//        mPager.setOffscreenPageLimit(4);

        //注:DirectionalViewPager实现的是过时的方法setOnPageChangeListener
        mPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                Log.d(TAG, "page currentIndex:" + position);
                mSeekBar.setProgress(position);
                mTotalPagesText.setText(getString(R.string.info_total_pages, position + 1, book.count));
                book.position = position;
            }
        });

        refreshForOrientation(getResources().getConfiguration());

        mTotalPagesText.setText(getString(R.string.info_total_pages, book.position + 1, book.count));
        mSeekBar.setKeyProgressIncrement(1);
        mSeekBar.setMax(book.count - 1);
        mSeekBar.setProgress(book.position);
        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int progress = 0;

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                this.progress = progress;
                mTotalPagesText.setText(getString(R.string.info_total_pages, progress + 1, book.count));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                book.position = progress;
                mPager.setCurrentItem(progress, false);
            }
        });
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        new Handler().postDelayed(this::hide, AUTO_HIDE_DELAY_MILLIS);
    }

    @Override
    protected void onStop() {
        super.onStop();
        BookApi.saveBookJson(this, book);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        refreshForOrientation(newConfig);
    }

    private void refreshForOrientation(Configuration newConfig) {
        if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            mPager.setOrientation(DirectionalViewPager.HORIZONTAL);
        } else if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            mPager.setOrientation(DirectionalViewPager.VERTICAL);
        }
    }

    public void toggle() {
        if (mAppBar.getAlpha() != 0f) {
            hide();
        } else if (mAppBar.getAlpha() != 1f) {
            show();
        }
    }

    private void hide() {
        ViewCompat.animate(mAppBar).alpha(0f).start();
        ViewCompat.animate(mBottomBar).alpha(0f).start();
        mFullScreenHelper.setFullScreen(true);
    }

    private void show() {
        ViewCompat.animate(mAppBar).alpha(1f).start();
        ViewCompat.animate(mBottomBar).alpha(1f).start();
        mFullScreenHelper.setFullScreen(false);
    }

    @Override
    public void onBackPressed() {
        if (mAppBar.getAlpha() != 1f) {
            toggle();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onDMDownloadSuccess(Book book) {
        if (this.book.name.equals(book.name)) {
            book.status = Book.Status.DOWNLOADED;
        }
    }

}
