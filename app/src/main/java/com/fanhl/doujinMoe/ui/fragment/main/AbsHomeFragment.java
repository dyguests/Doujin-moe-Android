package com.fanhl.doujinMoe.ui.fragment.main;

import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.util.Log;

import com.fanhl.doujinMoe.R;
import com.fanhl.doujinMoe.common.Constants;
import com.fanhl.doujinMoe.rest.model.FolderResponse;

import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

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
    private int     offset            = 0;

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
        if (!isLoadingData && !(isLoadMore && isLoadingComplete)) {
            Log.d(TAG, "loading more");
            isLoadingData = true;

            app().getClient().getHomeService()
                    .bookList(getSection(), offset, Constants.PAGE_BOOK_COUNT_MAX, getSortType(), getParam())
                    .enqueue(new Callback<FolderResponse>() {
                        @Override
                        public void onResponse(Response<FolderResponse> response, Retrofit retrofit) {
                            if (mSwipeRefreshLayout == null) return;
                            isLoadingData = false;
                            isLoadingComplete = response.body().complete;
                            offset += Constants.PAGE_BOOK_COUNT_MAX;

                            mSwipeRefreshLayout.setRefreshing(false);
                            if (!isLoadMore) mBooks.clear();
                            mBooks.addAll(response.body().folders);
                            mAdapter.notifyDataSetChanged();
                        }

                        @Override
                        public void onFailure(Throwable t) {
                            isLoadingData = false;
                            if (mSwipeRefreshLayout == null) return;
                            mSwipeRefreshLayout.setRefreshing(false);
                            Log.e(NewestFragment.TAG, Log.getStackTraceString(t));
                            Snackbar.make(mSwipeRefreshLayout, R.string.text_newest_get_fail, Snackbar.LENGTH_LONG).setAction(R.string.action_retry, v -> refreshData()).show();
                        }
                    });
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
    protected abstract String getSection();

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
    protected abstract String getSortType();

    @NonNull
    protected String getParam() {
        return "";
    }
}
