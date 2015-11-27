package com.fanhl.doujinMoe.ui.adapter.downloadManager;

import android.content.Context;
import android.support.v7.widget.RecyclerView;

import com.fanhl.doujinMoe.util.DownloadManager;

/**
 * Created by fanhl on 15/11/27.
 */
public class DownloadingRecyclerAdapter extends AbsDownloadManagerRecyclerAdapter {
    public DownloadingRecyclerAdapter(Context context, RecyclerView mRecyclerView, DownloadManager downloadManager) {
        super(context, mRecyclerView, downloadManager);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        if (downloadManager.getDownloadingBook() != null) {
            if (position == 0) {
                holder.bind(downloadManager.getDownloadingBook());
            } else {
                holder.bind(downloadManager.getWaitBooks().get(position - 1));
            }
        } else {
            holder.bind(downloadManager.getWaitBooks().get(position));
        }

    }

    @Override
    public int getItemCount() {
        return downloadManager.getWaitBooks().size() + (downloadManager.getDownloadingBook() == null ? 0 : 1);
    }
}
