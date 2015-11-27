package com.fanhl.doujinMoe.ui.adapter.downloadManager;

import android.content.Context;
import android.os.Handler;
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
}
