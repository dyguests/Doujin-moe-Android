package com.fanhl.doujinMoe.ui.fragment.downloadManager;

import android.os.Bundle;
import android.support.annotation.Nullable;
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
 * Created by fanhl on 15/11/27.
 */
public class DownloadSuccessFragment extends AbsDownloadManagerFragment {
    public static final String TAG = DownloadSuccessFragment.class.getSimpleName();

    @Bind(R.id.recycler_view)
    RecyclerView mRecyclerView;

    AbsDownloadManagerRecyclerAdapter mAdapter;
    private DownloadManager downloadManager;

    public static DownloadSuccessFragment newInstance() {
        return new DownloadSuccessFragment();
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
                holder.bind(downloadManager.getDownloadedBooks().get(position));
            }

            @Override
            public int getItemCount() {
                return downloadManager.getDownloadedBooks().size();
            }
        };
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener((position, viewHolder) -> DetailsActivity.launch(getActivity(), ((AbsDownloadManagerRecyclerAdapter.ViewHolder) viewHolder).item));
    }

    @Override
    public void onDownloadBookChanged(Book book, boolean success) {
        Log.d(TAG, "onDownloadBookChanged 刷新列表");
        mAdapter.notifyDataSetChanged();
    }
}
