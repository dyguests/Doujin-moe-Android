package com.fanhl.doujinMoe.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.ImageView;

import com.fanhl.doujinMoe.R;
import com.fanhl.doujinMoe.api.common.DouJinMoeUrl;
import com.fanhl.doujinMoe.exception.GetDataFailException;
import com.fanhl.doujinMoe.model.Book;
import com.fanhl.doujinMoe.model.Page;
import com.fanhl.doujinMoe.ui.adapter.PageListRecyclerAdapter;
import com.fanhl.util.GsonUtil;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class DetailsActivity extends AppCompatActivity {
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

        fab.setOnClickListener(view -> Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show());

        Intent intent = getIntent();
        book = new Gson().fromJson(intent.getStringExtra(EXTRA_BOOK_DATA), Book.class);
//        book = BookApi.getBookFormJson(this, book);//从本地取最新的数据

        Picasso.with(this)
                .load(DouJinMoeUrl.previewUrl(book.token))
                .into(mPreview);
        setTitle(book.name);

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
            PageListRecyclerAdapter.ViewHolder holder = (PageListRecyclerAdapter.ViewHolder) viewHolder;
//            GalleryActivity.launch(this, holder.getItem(),position);
        });

        refreshData();
    }

    private void refreshData() {
        if (!mSwipeRefreshLayout.isRefreshing()) mSwipeRefreshLayout.setRefreshing(true);
        Observable.<List<Page>>create(subscriber -> {
            try {
                subscriber.onNext(PageApi.pages(book));
            } catch (GetDataFailException e) {
                subscriber.onError(e);
            }
            subscriber.onCompleted();
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(pages -> {
                    mSwipeRefreshLayout.setRefreshing(false);
                    book.pages.clear();
                    book.pages.addAll(pages);
                    mAdapter.notifyDataSetChanged();
                }, throwable -> {
                    mSwipeRefreshLayout.setRefreshing(false);
                    Log.e(TAG, Log.getStackTraceString(throwable));
                    Snackbar.make(mSwipeRefreshLayout, R.string.refresh_fail, Snackbar.LENGTH_LONG).setAction(R.string.action_retry, v -> refreshData()).show();
                });
    }
}
