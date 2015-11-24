package com.fanhl.doujinMoe.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.fanhl.doujinMoe.R;
import com.fanhl.doujinMoe.api.PageApi;
import com.fanhl.doujinMoe.api.common.DouJinMoeUrl;
import com.fanhl.doujinMoe.model.Book;
import com.fanhl.doujinMoe.ui.common.AbsRecyclerViewAdapter;
import com.fanhl.doujinMoe.ui.widget.TextDrawable;
import com.fanhl.doujinMoe.util.ColorGenerator;
import com.fanhl.doujinMoe.util.Utility;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by fanhl on 15/10/29.
 */
public class BookGridRecyclerAdapter extends AbsRecyclerViewAdapter<BookGridRecyclerAdapter.ViewHolder> {
    public static final String TAG = BookGridRecyclerAdapter.class.getSimpleName();

    private final List<Book>     list;
    private final ColorGenerator mColorGenerator;

    public BookGridRecyclerAdapter(Context context, RecyclerView mRecyclerView, List<Book> list) {
        super(context, mRecyclerView);
        this.list = list;

        mColorGenerator = ColorGenerator.MATERIAL;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_item_book_card, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        Book item  = list.get(position);
        int  color = this.mColorGenerator.getColor(item.name);
        holder.bind(this.context, item, color);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public void onViewRecycled(ViewHolder holder) {
        super.onViewRecycled(holder);
    }

    public class ViewHolder extends AbsRecyclerViewAdapter.ClickableViewHolder {
        @Bind(R.id.preview)
        ImageView mPreview;
        @Bind(R.id.title)
        TextView  mTitle;


        Book item;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        private void bind(Context context, Book item, int color) {
            mTitle.setText(item.name);

            if (item.downloaded) {
                Picasso.with(context)
                        .load(PageApi.getPageFile(context, item, 0))
                        .into(mPreview);
            } else {
                TextDrawable drawablePlaceHolder = TextDrawable.builder().buildRect(Utility.getFirstCharacter(item.name), color);
                Picasso.with(context)
                        .load(DouJinMoeUrl.previewUrl(item.token))
                        .placeholder(drawablePlaceHolder)
                        .into(mPreview);
            }

            this.item = item;
        }

        public Book getItem() {
            return item;
        }

    }
}
