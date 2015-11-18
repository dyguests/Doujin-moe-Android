package com.fanhl.doujinMoe.ui;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.AppCompatSeekBar;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.Toolbar;
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
    public static final String TAG             = GalleryActivity.class.getSimpleName();
    public static final String EXTRA_BOOK_DATA = "EXTRA_BOOK_DATA";

    @Bind(R.id.toolbar)
    Toolbar           toolbar;
    @Bind(R.id.pager)
    ViewPager         mPager;
    @Bind(R.id.app_bar)
    LinearLayout      mAppBar;
    @Bind(R.id.bottom_bar)
    LinearLayout      mBottomBar;
    @Bind(R.id.seekBar)
    AppCompatSeekBar  mSeekBar;
    @Bind(R.id.total_pages_text)
    AppCompatTextView mTotalPagesText;

    //custom

    private Book book;

    private FullScreenHelper mFullScreenHelper;

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
        //从本地取最新的数据
        book = BookApi.getBookFormJson(this, book);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        setTitle(book.name);

        mPagerAdapter = new GalleryPagerAdapter(getFragmentManager(), book);
        mPager.setAdapter(mPagerAdapter);
        mPager.setCurrentItem(book.position, false);
        mPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                mSeekBar.setProgress(position);
                mTotalPagesText.setText(String.format(getString(R.string.info_total_pages), position + 1, book.count));
                book.position = position;
            }
        });
        mTotalPagesText.setText(String.format(getString(R.string.info_total_pages), book.position + 1, book.count));
        mSeekBar.setKeyProgressIncrement(1);
        mSeekBar.setMax(book.count - 1);
        mSeekBar.setProgress(book.position);
        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int progress = 0;

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                this.progress = progress;
                mTotalPagesText.setText(String.format(getString(R.string.info_total_pages), progress + 1, book.count));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mPager.setCurrentItem(progress, false);
            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        BookApi.saveBookJson(this, book);
    }

    public void toggle() {
        if (mAppBar.getAlpha() != 0f) {
            ViewCompat.animate(mAppBar).alpha(0f).start();
            ViewCompat.animate(mBottomBar).alpha(0f).start();
            mFullScreenHelper.setFullScreen(true);
        } else if (mAppBar.getAlpha() != 1f) {
            ViewCompat.animate(mAppBar).alpha(1f).start();
            ViewCompat.animate(mBottomBar).alpha(1f).start();
            mFullScreenHelper.setFullScreen(false);
        }
    }

    @Override
    public void onBackPressed() {
        if (mAppBar.getAlpha() != 1f) {
            toggle();
        } else {
            super.onBackPressed();
        }
    }
}
