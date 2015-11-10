package com.fanhl.doujinMoe.ui.adapter;

import android.app.Fragment;
import android.app.FragmentManager;
import android.support.v13.app.FragmentPagerAdapter;

import com.fanhl.doujinMoe.model.Book;
import com.fanhl.doujinMoe.ui.fragment.BookPageFragment;

public class GalleryPagerAdapter extends FragmentPagerAdapter {

    private BookPageFragment[] fragments;
    private Book               book;

    public GalleryPagerAdapter(FragmentManager fm, Book book) {
        super(fm);
        this.fragments = new BookPageFragment[book.count];
        this.book = book;
    }

    @Override
    public Fragment getItem(int position) {
        if (fragments[position] == null) fragments[position] = BookPageFragment.newInstance(book, position);
        return fragments[position];
    }

    @Override
    public int getCount() {
        return book.count;
    }
}
