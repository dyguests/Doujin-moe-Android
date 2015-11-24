package com.fanhl.doujinMoe.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;

import com.fanhl.doujinMoe.ui.common.AbsRecyclerViewAdapter;

/**
 * 好像不好能用?
 * Created by fanhl on 15/11/24.
 */
@Deprecated
public abstract class AbsBookRecyclerAdapter<CVH extends AbsRecyclerViewAdapter.ClickableViewHolder> extends AbsRecyclerViewAdapter<CVH> {
    public AbsBookRecyclerAdapter(Context context, RecyclerView recyclerView) {
        super(context, recyclerView);
    }
}
