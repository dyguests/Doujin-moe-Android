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

    public AbsDownloadManagerRecyclerAdapter(Context context, RecyclerView mRecyclerView, DownloadManager downloadManager) {
        super(context, mRecyclerView);
        this.context = context;
        this.downloadManager = downloadManager;
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
            if (item.downloaded) {
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
                mProgress.setText(R.string.wait_for_download);
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

        /**
         * 修改进度
         *
         * @param index
         * @param count
         */
        public void progress(int index, int count) {
            if (index + 1 < count) mDownloadContainer.setVisibility(View.VISIBLE);
            mProgress.setText(String.format(context.getResources().getString(R.string.info_total_pages), index + 1, count));
            if (index + 1 == count) mDownloadContainer.setVisibility(View.GONE);
        }

        private int getColor(Book item) {
            return ColorGenerator.MATERIAL.getColor(item.name);
        }

        @Override
        public void onDownloadProgressChanged(Book book, int progress) {
            if (book == null || !book.name.equals(item.name)) return;

            //ui Thread
            mProgress.getHandler().post(() -> mProgress.setText(String.format(context.getResources().getString(R.string.info_total_pages), progress + 1, book.count)));
            if (progress + 1 == book.count) {
                mDownloadContainer.getHandler().post(() -> mDownloadContainer.setVisibility(View.GONE));
            }
        }
    }
}
