package com.fanhl.doujinMoe.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.fanhl.doujinMoe.R;
import com.fanhl.doujinMoe.model.Book;
import com.fanhl.doujinMoe.model.Page;
import com.fanhl.doujinMoe.ui.common.AbsRecyclerViewAdapter;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by fanhl on 15/11/6.
 */
public class PageListRecyclerAdapter extends AbsRecyclerViewAdapter<PageListRecyclerAdapter.ViewHolder> {
    private final Book       book;
    /**
     * 来自于book {@link #book}
     */
    private final List<Page> list;

    public PageListRecyclerAdapter(Context context, RecyclerView mRecyclerView, Book book) {
        super(context, mRecyclerView);
        this.book = book;
        this.list = book.pages;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_item_page_card, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        Page item = list.get(position);
        holder.bind(this.context, item);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends AbsRecyclerViewAdapter.ClickableViewHolder {
        @Bind(R.id.preview)
        ImageView mPreview;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bind(Context context, Page item) {
            Picasso.with(context)
                    .load(item.preview)
                    .into(mPreview);
        }
    }
}
