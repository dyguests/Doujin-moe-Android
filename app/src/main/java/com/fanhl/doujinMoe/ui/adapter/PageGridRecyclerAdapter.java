package com.fanhl.doujinMoe.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.fanhl.doujinMoe.R;
import com.fanhl.doujinMoe.api.PageApi;
import com.fanhl.doujinMoe.model.Book;
import com.fanhl.doujinMoe.ui.common.AbsRecyclerViewAdapter;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by fanhl on 15/11/6.
 */
public class PageGridRecyclerAdapter extends AbsRecyclerViewAdapter<PageGridRecyclerAdapter.ViewHolder> {
    private final Book book;

    public PageGridRecyclerAdapter(Context context, RecyclerView mRecyclerView, Book book) {
        super(context, mRecyclerView);
        this.book = book;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_item_page_card, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        holder.bind(this.context, book, position);
    }

    @Override
    public int getItemCount() {
        return book.pages.size();
    }

    public class ViewHolder extends AbsRecyclerViewAdapter.ClickableViewHolder {
        @Bind(R.id.preview)
        ImageView mPreview;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bind(Context context, Book book, int position) {
            if (book.isDownloaded()) {
                Picasso.with(context)
                        .load(PageApi.getPageFile(context, book, position))
                        .memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE)//不缓存大图到内存
//                        .resize(DisplayUtil.dip2px(context, 250), DisplayUtil.dip2px(context, 250))
                        .fit()
                        .centerInside()
//                        .centerCrop()
                        .into(mPreview);
            } else {
                Picasso.with(context)
                        .load(book.pages.get(position).preview)
                        .fit()
                        .centerInside()
                        .into(mPreview);
            }
        }

    }
}
