package com.fanhl.doujinMoe.ui.adapter.downloadManager;

import android.content.Context;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.fanhl.doujinMoe.R;
import com.fanhl.doujinMoe.api.PageApi;
import com.fanhl.doujinMoe.api.common.DouJinMoeUrl;
import com.fanhl.doujinMoe.model.Book;
import com.fanhl.doujinMoe.ui.common.AbsRecyclerViewAdapter;
import com.fanhl.doujinMoe.ui.widget.TextDrawable;
import com.fanhl.doujinMoe.util.ColorGenerator;
import com.fanhl.doujinMoe.util.DownloadManager;
import com.fanhl.doujinMoe.util.Utility;
import com.squareup.picasso.Picasso;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by fanhl on 15/11/24.
 */
public abstract class AbsDownloadManagerRecyclerAdapter extends AbsRecyclerViewAdapter<AbsDownloadManagerRecyclerAdapter.ViewHolder> {
    protected final DownloadManager downloadManager;
    private final   Handler         uihandler;

    public AbsDownloadManagerRecyclerAdapter(Context context, RecyclerView mRecyclerView, DownloadManager downloadManager) {
        super(context, mRecyclerView);
        this.context = context;
        this.downloadManager = downloadManager;
        uihandler = new Handler();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_item_download_card, parent, false);
        return new ViewHolder(context, view);
    }

    @Override
    public void onViewRecycled(ViewHolder holder) {
        super.onViewRecycled(holder);
        holder.unbind(downloadManager);
    }


    public class ViewHolder extends AbsRecyclerViewAdapter.ClickableViewHolder implements DownloadManager.OnDownloadProgressChangeListener {
        private final Context context;

        @Bind(R.id.preview)
        ImageView   mPreview;
        @Bind(R.id.title)
        TextView    mTitle;
        @Bind(R.id.download_container)
        FrameLayout mDownloadContainer;
        @Bind(R.id.progress)
        TextView    mProgress;

        public Book item;

        public ViewHolder(Context context, View itemView) {
            super(itemView);
            this.context = context;
            ButterKnife.bind(this, itemView);
        }

        public void bind(Book item) {
            mTitle.setText(item.name);
            if (item.isDownloaded()) {
                Picasso.with(AbsDownloadManagerRecyclerAdapter.this.context)
                        .load(PageApi.getPageFile(AbsDownloadManagerRecyclerAdapter.this.context, item, 0))
                        .into(mPreview);
                mDownloadContainer.setVisibility(View.GONE);
            } else {
                TextDrawable drawablePlaceHolder = TextDrawable.builder().buildRect(Utility.getFirstCharacter(item.name), getColor(item));
                Picasso.with(AbsDownloadManagerRecyclerAdapter.this.context)
                        .load(DouJinMoeUrl.previewUrl(item.token))
                        .placeholder(drawablePlaceHolder)
                        .into(mPreview);

                if (item.isWaitDownload()) {
                    mProgress.setText(R.string.wait_for_download);
                } else if (item.isDownloading()) {
                    mProgress.setText(context.getResources().getString(R.string.info_total_pages, item.downloadedPosition + 1, item.count));
                } else {
                    mDownloadContainer.setVisibility(View.GONE);
                }
            }

            this.item = item;
        }

        public void bind(Book book, DownloadManager downloadManager) {
            bind(book);
            downloadManager.addOnDownloadProgressChangeListener(this);
        }

        public void unbind(DownloadManager downloadManager) {
            downloadManager.removeOnDownloadProgressChangeListener(this);
        }

        private int getColor(Book item) {
            return ColorGenerator.MATERIAL.getColor(item.name);
        }

        @Override
        public void onDownloadProgressChanged(Book book) {
            if (book == null || !book.name.equals(item.name)) return;

            //ui Thread
            uihandler.post(() -> mProgress.setText(context.getResources().getString(R.string.info_total_pages, book.downloadedPosition + 1, book.count)));
            // FIXME: 15/11/28 以下无用删除
//            if (book.downloadedPosition + 1 == book.count) {
//                uihandler.post(() -> mDownloadContainer.setVisibility(View.GONE));
//            }
        }
    }
}
