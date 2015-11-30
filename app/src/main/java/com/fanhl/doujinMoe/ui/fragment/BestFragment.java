package com.fanhl.doujinMoe.ui.fragment;

import android.support.design.widget.Snackbar;
import android.util.Log;

import com.fanhl.doujinMoe.R;
import com.fanhl.doujinMoe.api.HomeApi;
import com.fanhl.doujinMoe.model.Book;

import java.util.List;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Top Rated
 */
public class BestFragment extends AbsBookRecyclerFragment {
    public static final String TAG = NewestFragment.class.getSimpleName();

    public static BestFragment newInstance() {
        return new BestFragment();
    }

    @Override
    protected void refreshData() {
        if (!mSwipeRefreshLayout.isRefreshing()) mSwipeRefreshLayout.setRefreshing(true);
        Observable.<List<Book>>create(subscriber -> {
            try {
                subscriber.onNext(HomeApi.best(1, "year"));
                subscriber.onCompleted();
            } catch (Exception e) {
                subscriber.onError(e);
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(books -> {
                    mSwipeRefreshLayout.setRefreshing(false);
                    mBooks.clear();
                    mBooks.addAll(books);
                    mAdapter.notifyDataSetChanged();
                }, throwable -> {
                    mSwipeRefreshLayout.setRefreshing(false);
                    Log.e(TAG, Log.getStackTraceString(throwable));
                    Snackbar.make(mSwipeRefreshLayout, R.string.text_newest_get_fail, Snackbar.LENGTH_LONG).setAction(R.string.action_retry, v -> refreshData()).show();
                });
    }
}