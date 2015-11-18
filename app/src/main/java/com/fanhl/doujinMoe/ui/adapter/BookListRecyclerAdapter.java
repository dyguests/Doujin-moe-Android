package com.fanhl.doujinMoe.ui.adapter;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.fanhl.doujinMoe.R;
import com.fanhl.doujinMoe.api.common.DouJinMoeUrl;
import com.fanhl.doujinMoe.model.Book;
import com.fanhl.doujinMoe.ui.common.AbsRecyclerViewAdapter;
import com.fanhl.doujinMoe.ui.widget.TextDrawable;
import com.fanhl.doujinMoe.util.ColorGenerator;
import com.fanhl.doujinMoe.util.Utility;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by fanhl on 15/10/29.
 */
public class BookListRecyclerAdapter extends AbsRecyclerViewAdapter<BookListRecyclerAdapter.ViewHolder> {
    public static final String TAG = BookListRecyclerAdapter.class.getSimpleName();

    private final List<Book>     list;
    private final ColorGenerator mColorGenerator;

    public BookListRecyclerAdapter(Context context, RecyclerView mRecyclerView, List<Book> list) {
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
        SimpleDraweeView mPreview;
        @Bind(R.id.title)
        TextView         mTitle;


        Book item;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        private void bind(Context context, Book item, int color) {
            mTitle.setText(item.name);

            TextDrawable drawablePlaceHolder = TextDrawable.builder().buildRect(Utility.getFirstCharacter(item.name), color);
            mPreview.setImageDrawable(drawablePlaceHolder);

            if (!item.downloaded) {
                mPreview.setImageURI(Uri.parse(DouJinMoeUrl.previewUrl(item.token)));
            } else {
                //加载预览图
//                String pagePath = PageApi.getPreviewPath(context, item);
//                if (pagePath != null) {
//                    Picasso.with(context)
//                            .load("file:" + pagePath)
//                            .placeholder(drawablePlaceHolder)
//                            .into(mPreview);
//                }

//                mLabelView.setVisibility(View.VISIBLE);
            }

            this.item = item;
        }

        public Book getItem() {
            return item;
        }

    }
}
