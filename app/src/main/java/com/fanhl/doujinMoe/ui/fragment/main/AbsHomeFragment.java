package com.fanhl.doujinMoe.ui.fragment.main;

import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.util.Log;

import com.fanhl.doujinMoe.R;
import com.fanhl.doujinMoe.common.Constants;
import com.fanhl.doujinMoe.rest.model.FolderResponse;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 首页(newest,best,artist...的公共父类
 * Created by fanhl on 15/12/1.
 */
public abstract class AbsHomeFragment extends AbsBookRecyclerFragment {
    public static final String TAG = AbsHomeFragment.class.getSimpleName();

    /*是滞已加载完所有数据*/
    private boolean isLoadingComplete = false;
    /*loadMore用*/
    private boolean isLoadingData     = false;
    /*当前数据加载是 refresh 还是 loadMore*/
    private boolean isLoadMore        = false;
    private int     offset            = 0;
    /*处理从服务器取得的数据的观察者*/
    private Subscriber<FolderResponse> subscriber;

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        subscriber.unsubscribe();
    }

    @Override
    protected void initData() {
        super.initData();
        subscriber = new Subscriber<FolderResponse>() {
            @Override
            public void onNext(FolderResponse folderResponse) {
                isLoadingData = false;
                isLoadingComplete = folderResponse.complete;
                offset += Constants.PAGE_BOOK_COUNT_MAX;
                if (mSwipeRefreshLayout == null) return;
                mSwipeRefreshLayout.setRefreshing(false);
                if (!isLoadMore) mAdapter.clear();//mBooks.clear();
//                mBooks.addAll(folderResponse.folders);
//                mAdapter.notifyDataSetChanged();
                mAdapter.addItems(folderResponse.folders);
            }

            @Override
            public void onError(Throwable e) {
                isLoadingData = false;
                mSwipeRefreshLayout.setRefreshing(false);
                if (mSwipeRefreshLayout == null) return;
                Log.e(NewestFragment.TAG, Log.getStackTraceString(e));
                Snackbar.make(mSwipeRefreshLayout, getLoadFailMsgResId(), Snackbar.LENGTH_LONG).setAction(R.string.action_retry, v -> refreshData()).show();
            }

            @Override
            public void onCompleted() {
            }
        };
    }

    @Override
    protected void refreshData() {
        if (!mSwipeRefreshLayout.isRefreshing()) mSwipeRefreshLayout.setRefreshing(true);
        offset = 0;
        loadData(false);
    }

    @Override
    protected void loadMore() {
        // 到底部自动加载
        loadData(true);
    }

    private void loadData(boolean isLoadMore) {
        this.isLoadMore = isLoadMore;
        if (!isLoadingData && !(this.isLoadMore && isLoadingComplete)) {
            Log.d(TAG, "loading more");
            isLoadingData = true;

            app().getClient().getHomeService()
                    .bookList(getToken(), offset, Constants.PAGE_BOOK_COUNT_MAX, getSort(), getParam())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
//                    .filter(folderResponse -> {
//                        if (folderResponse.success) {
//                            offset += Constants.PAGE_BOOK_COUNT_MAX;
//
//                        }else{
//                            // FIXME: 15/12/2 有办法失败走onError么?
//                        }
//                        return folderResponse.success;
//                    })
//                    .flatMap(folderResponse -> Observable.<Book>from(folderResponse.folders))
                    .subscribe(subscriber);
        }
    }

    /**
     * 取得版块类型
     * <p>
     * <li>newest</li>
     * <li>best</li>
     * <li>artist</li>
     *
     * @return
     */
    @NonNull
    protected abstract String getToken();

    /**
     * 取得排序方式
     * <li>name</li>
     * <p>
     * <li>ever</li>
     * <li>year</li>
     * <li>mouth</li>
     *
     * @return
     */
    @NonNull
    protected abstract String getSort();

    @NonNull
    protected String getParam() {
        return "";
    }

    protected abstract int getLoadFailMsgResId();
}
