package com.fanhl.doujinMoe.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fanhl.doujinMoe.R;
import com.fanhl.doujinMoe.model.Book;
import com.fanhl.doujinMoe.ui.DetailsActivity;
import com.fanhl.doujinMoe.ui.adapter.BookGridRecyclerAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by fanhl on 15/11/10.
 */
public abstract class AbsBookRecyclerFragment extends AbsFragment {
    @Bind(R.id.swipe_refresh_layout)
    SwipeRefreshLayout mSwipeRefreshLayout;
    @Bind(R.id.recycler_view)
    RecyclerView       mRecyclerView;

    protected BookGridRecyclerAdapter mAdapter;
    protected List<Book>              mBooks;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_swipe_recycle, container, false);
        ButterKnife.bind(this, view);
        assignViews();
        initData();
        refreshData();
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    protected void assignViews() {
        mSwipeRefreshLayout.setColorSchemeColors(getResources().getIntArray(R.array.refresh_array));
        mSwipeRefreshLayout.setOnRefreshListener(this::refreshData);

        //流式布局
        StaggeredGridLayoutManager mLayoutManager = new StaggeredGridLayoutManager(getResources().getInteger(R.integer.span_count_book), StaggeredGridLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setHasFixedSize(true);

        //mRecyclerView
        mBooks = new ArrayList<>();
        mAdapter = new BookGridRecyclerAdapter(getActivity(), mRecyclerView, mBooks);
        mRecyclerView.setAdapter(mAdapter);

        mAdapter.setOnItemClickListener((position, viewHolder) -> {
            BookGridRecyclerAdapter.ViewHolder holder = (BookGridRecyclerAdapter.ViewHolder) viewHolder;
            DetailsActivity.launch(getActivity(), holder.item);
        });
    }

    protected void initData() {

    }

    protected abstract void refreshData();
}
