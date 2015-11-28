package com.fanhl.doujinMoe.ui.fragment.downloadManager;

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
import com.fanhl.doujinMoe.model.Book;
import com.fanhl.doujinMoe.ui.DetailsActivity;
import com.fanhl.doujinMoe.ui.adapter.downloadManager.AbsDownloadManagerRecyclerAdapter;
import com.fanhl.doujinMoe.util.DownloadManager;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by fanhl on 15/11/24.
 */
public class DownloadingFragment extends AbsDownloadManagerFragment {
    public static final String TAG = DownloadingFragment.class.getSimpleName();

//    @Bind(R.id.swipe_refresh_layout)
//    SwipeRefreshLayout mSwipeRefreshLayout;
    @Bind(R.id.recycler_view)
    RecyclerView       mRecyclerView;

    AbsDownloadManagerRecyclerAdapter mAdapter;
    private DownloadManager downloadManager;

    public static DownloadingFragment newInstance() {
        return new DownloadingFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recycle, container, false);
        ButterKnife.bind(this, view);
        assignViews();
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mRecyclerView.setAdapter(null);
        ButterKnife.unbind(this);
    }

    private void assignViews() {
        //流式布局
        StaggeredGridLayoutManager mLayoutManager = new StaggeredGridLayoutManager(getResources().getInteger(R.integer.span_count_book), StaggeredGridLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setHasFixedSize(true);

        //mRecyclerView
        downloadManager = getAbsActivity().getDownloadManager();
        mAdapter = new AbsDownloadManagerRecyclerAdapter(getActivity(), mRecyclerView, downloadManager) {
            @Override
            public void onBindViewHolder(ViewHolder holder, int position) {
                super.onBindViewHolder(holder, position);
                // FIXME: 15/11/27 存在一个正在下载,多个未下载时.当正在下载完成后.再notifyDataChanged后显示不正常.
                if (downloadManager.getDownloadingBook() != null) {
                    if (position == 0) {
                        holder.bind(downloadManager.getDownloadingBook(), downloadManager);
                    } else {
                        holder.bind(downloadManager.getWaitBooks().get(position - 1), downloadManager);
                    }
                } else {
                    holder.bind(downloadManager.getWaitBooks().get(position), downloadManager);
                }

            }

            @Override
            public int getItemCount() {
                return downloadManager.getWaitBooks().size() + (downloadManager.getDownloadingBook() == null ? 0 : 1);
            }
        };

        mRecyclerView.setAdapter(mAdapter);

        mAdapter.setOnItemClickListener((position, viewHolder) -> {
            DetailsActivity.launch(getActivity(), ((AbsDownloadManagerRecyclerAdapter.ViewHolder) viewHolder).item);
        });
        mAdapter.setOnItemLongClickListener((position, holder) -> {
            Log.d(TAG, "取消下载确认.");
            Snackbar.make(mRecyclerView, R.string.text_cancel_download, Snackbar.LENGTH_LONG).setAction(R.string.action_cancel, v -> {
                downloadManager.cancelDownload(((AbsDownloadManagerRecyclerAdapter.ViewHolder) holder).item);
            }).show();
            return true;
        });
    }

    @Override
    public void onDownloadBookChanged(Book book, boolean success) {
        Log.d(TAG, "onDownloadBookChanged 刷新列表");
        mAdapter.notifyDataSetChanged();
    }
}
