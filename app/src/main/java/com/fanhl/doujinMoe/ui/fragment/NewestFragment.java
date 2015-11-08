package com.fanhl.doujinMoe.ui.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fanhl.doujinMoe.R;
import com.fanhl.doujinMoe.api.HomeApi;
import com.fanhl.doujinMoe.api.form.NewestForm;
import com.fanhl.doujinMoe.model.Book;
import com.fanhl.doujinMoe.ui.DetailsActivity;
import com.fanhl.doujinMoe.ui.adapter.BookListRecyclerAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by fanhl on 15/11/8.
 */
public class NewestFragment extends Fragment {
    public static final String TAG = NewestFragment.class.getSimpleName();

    @Bind(R.id.swipe_refresh_layout)
    SwipeRefreshLayout mSwipeRefreshLayout;
    @Bind(R.id.recycler_view)
    RecyclerView       mRecyclerView;

    //custom

    protected BookListRecyclerAdapter mAdapter;
    protected List<Book>              mBooks;

    public static NewestFragment newInstance() {
        return new NewestFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_newest, container, false);
        ButterKnife.bind(this, view);
        assignViews();
        refreshData();
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    private void assignViews() {
        mSwipeRefreshLayout.setColorSchemeColors(getResources().getIntArray(R.array.refresh_array));
        mSwipeRefreshLayout.setOnRefreshListener(this::refreshData);

        //流式布局
        StaggeredGridLayoutManager mLayoutManager = new StaggeredGridLayoutManager(getResources().getInteger(R.integer.span_count_book), StaggeredGridLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setHasFixedSize(true);

        //mRecyclerView
        mBooks = new ArrayList<>();
        mAdapter = new BookListRecyclerAdapter(getActivity(), mRecyclerView, mBooks);
        mRecyclerView.setAdapter(mAdapter);

        mAdapter.setOnItemClickListener((position, viewHolder) -> {
            BookListRecyclerAdapter.ViewHolder holder = (BookListRecyclerAdapter.ViewHolder) viewHolder;
            DetailsActivity.launch(getActivity(), holder.getItem());
        });
    }

    private void refreshData() {
        if (!mSwipeRefreshLayout.isRefreshing()) mSwipeRefreshLayout.setRefreshing(true);
        Observable.<NewestForm>create(subscriber -> {
            try {
                subscriber.onNext(HomeApi.newest(1));
                subscriber.onCompleted();
            } catch (Exception e) {
                subscriber.onError(e);
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(newestForm -> {
                    mSwipeRefreshLayout.setRefreshing(false);
                    mBooks.clear();
                    mBooks.addAll(newestForm.newest);
                    mAdapter.notifyDataSetChanged();
                }, throwable -> {
                    mSwipeRefreshLayout.setRefreshing(false);
                    Log.e(TAG, Log.getStackTraceString(throwable));
                    Snackbar.make(mSwipeRefreshLayout, R.string.text_newest_get_fail, Snackbar.LENGTH_LONG).setAction(R.string.action_retry, v -> refreshData()).show();
                });
    }
}
