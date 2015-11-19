package com.fanhl.doujinMoe.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;

import com.fanhl.doujinMoe.R;
import com.fanhl.doujinMoe.api.PageApi;
import com.fanhl.doujinMoe.api.common.DouJinMoeUrl;
import com.fanhl.doujinMoe.model.Book;
import com.fanhl.doujinMoe.ui.adapter.PageListRecyclerAdapter;
import com.fanhl.doujinMoe.ui.common.AbsActivity;
import com.fanhl.util.GsonUtil;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class DetailsActivity extends AbsActivity {
    public static final String TAG = DetailsActivity.class.getSimpleName();

    public static final String EXTRA_BOOK_DATA = "EXTRA_BOOK_DATA";

    @Bind(R.id.app_bar)
    AppBarLayout            mAppBar;
    @Bind(R.id.toolbar_layout)
    CollapsingToolbarLayout mToolbarLayout;
    @Bind(R.id.preview)
    ImageView               mPreview;
    @Bind(R.id.toolbar)
    Toolbar                 toolbar;
    @Bind(R.id.fab)
    FloatingActionButton    fab;
    @Bind(R.id.recycler_view)
    RecyclerView            mRecyclerView;
    @Bind(R.id.swipe_refresh_layout)
    SwipeRefreshLayout      mSwipeRefreshLayout;

    //custom

    private Book book;

    protected PageListRecyclerAdapter mAdapter;

    public static void launch(Activity activity, Book book) {
        Intent intent = new Intent(activity, DetailsActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
        intent.putExtra(EXTRA_BOOK_DATA, GsonUtil.json(book));
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);

        fab.setEnabled(false);//取得数据后再能点详细
        fab.setOnClickListener(view -> GalleryActivity.launch(DetailsActivity.this, book));

        Intent intent = getIntent();
        book = new Gson().fromJson(intent.getStringExtra(EXTRA_BOOK_DATA), Book.class);
//        book = BookApi.getBookFormJson(this, book);//从本地取最新的数据

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        Picasso.with(this)
                .load(DouJinMoeUrl.previewUrl(book.token))
                .into(mPreview);

        setTitle(book.name);

        mAppBar.addOnOffsetChangedListener((appBarLayout, verticalOffset) -> mSwipeRefreshLayout.setEnabled(verticalOffset == 0));

        mSwipeRefreshLayout.setColorSchemeColors(getResources().getIntArray(R.array.refresh_array));
        mSwipeRefreshLayout.setOnRefreshListener(this::refreshData);

        //流式布局
        StaggeredGridLayoutManager mLayoutManager = new StaggeredGridLayoutManager(getResources().getInteger(R.integer.span_count_page), StaggeredGridLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setHasFixedSize(true);


        //mRecyclerView
        mAdapter = new PageListRecyclerAdapter(this, mRecyclerView, book);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener((position, viewHolder) -> {
            book.position = position;
            GalleryActivity.launch(DetailsActivity.this, book);
        });

        refreshData();
    }

    private void refreshData() {
        if (!mSwipeRefreshLayout.isRefreshing()) mSwipeRefreshLayout.setRefreshing(true);
        Observable.<Void>create(subscriber -> {
            try {
                subscriber.onNext(PageApi.pages(book));
            } catch (Exception e) {
                subscriber.onError(e);
            }
            subscriber.onCompleted();
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(aVoid -> {
                    mSwipeRefreshLayout.setRefreshing(false);
                    fab.setEnabled(true);
                    mAdapter.notifyDataSetChanged();
                }, throwable -> {
                    mSwipeRefreshLayout.setRefreshing(false);
                    Log.e(TAG, Log.getStackTraceString(throwable));
                    Snackbar.make(mSwipeRefreshLayout, R.string.refresh_fail, Snackbar.LENGTH_LONG).setAction(R.string.action_retry, v -> refreshData()).show();
                });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_details, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_download) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
