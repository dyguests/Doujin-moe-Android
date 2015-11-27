package com.fanhl.doujinMoe.ui.fragment.downloadManager;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fanhl.doujinMoe.R;
import com.fanhl.doujinMoe.ui.adapter.downloadManager.DownloadManagerRecyclerAdapter;
import com.fanhl.doujinMoe.ui.fragment.AbsFragment;
import com.fanhl.doujinMoe.util.DownloadManager;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by fanhl on 15/11/24.
 */
public class DownloadingFragment extends AbsFragment {

    @Bind(R.id.recycler_view)
    RecyclerView mRecyclerView;

    DownloadManagerRecyclerAdapter mAdapter;
    private DownloadManager downloadManager;

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
        ButterKnife.unbind(this);
    }

    private void assignViews() {
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, true);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setHasFixedSize(true);

        //mRecyclerView
        downloadManager = getAbsActivity().getDownloadManager();
        mAdapter = new DownloadManagerRecyclerAdapter(getActivity(), mRecyclerView, downloadManager);
        mRecyclerView.setAdapter(mAdapter);
    }
}
