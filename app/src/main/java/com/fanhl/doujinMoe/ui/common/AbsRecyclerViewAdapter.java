package com.fanhl.doujinMoe.ui.common;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

public abstract class AbsRecyclerViewAdapter<CVH extends AbsRecyclerViewAdapter.ClickableViewHolder> extends RecyclerView.Adapter<CVH> {

    protected Context context;

    protected RecyclerView mRecyclerView;
    protected List<RecyclerView.OnScrollListener> mListeners = new ArrayList<>();

    public AbsRecyclerViewAdapter(Context context, RecyclerView recyclerView) {
        this.context = context;

        this.mRecyclerView = recyclerView;
        this.mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView rv, int newState) {
                for (RecyclerView.OnScrollListener listener : mListeners) {
                    listener.onScrollStateChanged(rv, newState);
                }
            }

            @Override
            public void onScrolled(RecyclerView rv, int dx, int dy) {
                for (RecyclerView.OnScrollListener listener : mListeners) {
                    listener.onScrolled(rv, dx, dy);
                }
            }
        });
    }

    public void addOnScrollListener(RecyclerView.OnScrollListener listener) {
        mListeners.add(listener);
    }

    public interface OnItemClickListener {
        void onItemClick(int position, AbsRecyclerViewAdapter.ClickableViewHolder holder);
    }

    public interface OnItemLongClickListener {
        boolean onItemLongClick(int position, AbsRecyclerViewAdapter.ClickableViewHolder holder);
    }

    private OnItemClickListener     itemClickListener;
    private OnItemLongClickListener itemLongClickListener;

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.itemClickListener = listener;
    }

    public void setOnItemLongClickListener(OnItemLongClickListener listener) {
        this.itemLongClickListener = listener;
    }

    @Override
    public void onBindViewHolder(final CVH holder, final int position) {
        holder.itemView.setOnClickListener(v -> {
            if (itemClickListener != null) {
                itemClickListener.onItemClick(position, holder);
            }
        });
        holder.itemView.setOnLongClickListener(v -> itemLongClickListener != null && itemLongClickListener.onItemLongClick(position, holder));
    }

    public class ClickableViewHolder extends RecyclerView.ViewHolder {
        public ClickableViewHolder(View itemView) {
            super(itemView);
        }
    }

}