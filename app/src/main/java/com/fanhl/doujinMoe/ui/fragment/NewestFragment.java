package com.fanhl.doujinMoe.ui.fragment;

import android.support.design.widget.Snackbar;
import android.util.Log;

import com.fanhl.doujinMoe.R;
import com.fanhl.doujinMoe.rest.model.FolderResponse;

import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * Created by fanhl on 15/11/8.
 */
public class NewestFragment extends AbsBookRecyclerFragment {
    public static final String TAG = NewestFragment.class.getSimpleName();

    public static NewestFragment newInstance() {
        return new NewestFragment();
    }

    @Override
    protected void refreshData() {
        if (!mSwipeRefreshLayout.isRefreshing()) mSwipeRefreshLayout.setRefreshing(true);
        app().getClient().getHomeService().bookList("newest", 0, 5, "name").enqueue(new Callback<FolderResponse>() {
            @Override
            public void onResponse(Response<FolderResponse> response, Retrofit retrofit) {
                if (mSwipeRefreshLayout == null) return;
                mSwipeRefreshLayout.setRefreshing(false);
                mBooks.clear();
                mBooks.addAll(response.body().folders);
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Throwable t) {
                if (mSwipeRefreshLayout == null) return;
                mSwipeRefreshLayout.setRefreshing(false);
                Log.e(TAG, Log.getStackTraceString(t));
                Snackbar.make(mSwipeRefreshLayout, R.string.text_newest_get_fail, Snackbar.LENGTH_LONG).setAction(R.string.action_retry, v -> refreshData()).show();
            }
        });
    }
}
