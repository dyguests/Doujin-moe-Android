package com.fanhl.doujinMoe.ui.adapter.downloadManager;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.fanhl.doujinMoe.R;
import com.fanhl.doujinMoe.ui.common.AbsRecyclerViewAdapter;
import com.fanhl.doujinMoe.util.DownloadManager;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by fanhl on 15/11/24.
 */
public abstract class DownloadManagerRecyclerAdapter extends AbsRecyclerViewAdapter<DownloadManagerRecyclerAdapter.ViewHolder> {
    protected final DownloadManager downloadManager;

    public DownloadManagerRecyclerAdapter(Context context, RecyclerView mRecyclerView, DownloadManager downloadManager) {
        super(context, mRecyclerView);
        this.context = context;
        this.downloadManager = downloadManager;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_item_download_card, parent, false);
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
        @Bind(R.id.preview)
        ImageView   mPreview;
        @Bind(R.id.title)
        TextView    mTitle;
        @Bind(R.id.download_container)
        FrameLayout mDownloadContainer;
        @Bind(R.id.progress)
        TextView    mProgress;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
