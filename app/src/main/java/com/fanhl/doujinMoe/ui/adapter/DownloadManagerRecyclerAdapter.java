package com.fanhl.doujinMoe.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fanhl.doujinMoe.R;
import com.fanhl.doujinMoe.ui.common.AbsRecyclerViewAdapter;
import com.fanhl.doujinMoe.util.DownloadManager;

/**
 * Created by fanhl on 15/11/24.
 */
public class DownloadManagerRecyclerAdapter extends AbsRecyclerViewAdapter<DownloadManagerRecyclerAdapter.ViewHolder> {
    private final DownloadManager downloadManager;

    public DownloadManagerRecyclerAdapter(Context context, RecyclerView mRecyclerView, DownloadManager downloadManager) {
        super(context, mRecyclerView);
        this.context = context;
        this.downloadManager = downloadManager;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_item_book_card, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class ViewHolder extends AbsRecyclerViewAdapter.ClickableViewHolder {
        public ViewHolder(View itemView) {
            super(itemView);
        }
    }
}
