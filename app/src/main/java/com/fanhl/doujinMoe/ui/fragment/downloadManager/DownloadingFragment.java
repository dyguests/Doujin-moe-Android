package com.fanhl.doujinMoe.ui.fragment.downloadManager;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fanhl.doujinMoe.R;
import com.fanhl.doujinMoe.model.Book;
import com.fanhl.doujinMoe.ui.adapter.downloadManager.DownloadingRecyclerAdapter;
import com.fanhl.doujinMoe.util.DownloadManager;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by fanhl on 15/11/24.
 */
public class DownloadingFragment extends AbsDownloadManagerFragment {

    @Bind(R.id.recycler_view)
    RecyclerView mRecyclerView;

    DownloadingRecyclerAdapter mAdapter;
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
        mAdapter = new DownloadingRecyclerAdapter(getActivity(), mRecyclerView, downloadManager);
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public void onDownloadBookChanged(Book book, boolean success) {
        mAdapter.notifyDataSetChanged();
    }
}
