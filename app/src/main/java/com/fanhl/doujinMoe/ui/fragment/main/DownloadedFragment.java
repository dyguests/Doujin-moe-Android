package com.fanhl.doujinMoe.ui.fragment.main;

import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.util.Log;

import com.fanhl.doujinMoe.R;
import com.fanhl.doujinMoe.api.BookApi;
import com.fanhl.doujinMoe.model.Book;
import com.fanhl.doujinMoe.ui.adapter.BookGridRecyclerAdapter;
import com.fanhl.util.ThreadUtil;

import java.util.List;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.android.schedulers.HandlerScheduler;

/**
 * 已下载
 * Created by fanhl on 15/11/20.
 */
public class DownloadedFragment extends AbsBookRecyclerFragment {
    public static final String TAG = DownloadedFragment.class.getSimpleName();

    private Handler downloadedHandler;

    public static DownloadedFragment newInstance() {
        return new DownloadedFragment();
    }

    @Override
    protected void assignViews() {
        super.assignViews();

        mAdapter.setOnItemLongClickListener((position, viewHolder) -> {
            Log.d(TAG, "删除书籍确认.");
            BookGridRecyclerAdapter.ViewHolder holder = (BookGridRecyclerAdapter.ViewHolder) viewHolder;
            Snackbar.make(mRecyclerView, getResources().getString(R.string.text_book_delete, holder.item.name), Snackbar.LENGTH_LONG).setAction(R.string.action_delete, v -> {
                if (BookApi.deleteBook(getActivity(), holder.item)) {
                    app().getLocalManager().refresh(() -> getActivity().runOnUiThread(this::refreshData));
                }
            }).show();
            return true;
        });
    }

    @Override
    protected void initData() {
        super.initData();
        downloadedHandler = ThreadUtil.createBackgroundHandler("已下载用线程");
    }

    @Override
    protected void refreshData() {
        if (!mSwipeRefreshLayout.isRefreshing()) mSwipeRefreshLayout.setRefreshing(true);
        Observable.<List<Book>>create(subscriber -> {
            try {
                subscriber.onNext(app().getLocalManager().getDownloadedBooks());
                subscriber.onCompleted();
            } catch (Exception e) {
                subscriber.onError(e);
            }
        }).subscribeOn(HandlerScheduler.from(downloadedHandler))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(books -> {
                    mSwipeRefreshLayout.setRefreshing(false);
                    mBooks.clear();
                    mBooks.addAll(books);
                    mAdapter.notifyDataSetChanged();
                }, throwable -> {
                    mSwipeRefreshLayout.setRefreshing(false);
                    Log.e(TAG, Log.getStackTraceString(throwable));
                    Snackbar.make(mSwipeRefreshLayout, R.string.text_downloaded_get_fail, Snackbar.LENGTH_LONG).setAction(R.string.action_retry, v -> refreshData()).show();
                });
    }
}
