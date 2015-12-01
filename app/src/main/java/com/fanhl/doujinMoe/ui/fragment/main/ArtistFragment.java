package com.fanhl.doujinMoe.ui.fragment.main;

import android.support.annotation.NonNull;

/**
 * Created by fanhl on 15/11/8.
 */
public class ArtistFragment extends AbsHomeFragment {

    public static ArtistFragment newInstance() {
        return new ArtistFragment();
    }

    @Override
    @NonNull
    protected String getSection() {
        return "artist";
    }

    @Override
    @NonNull
    protected String getSortType() {
        return "name";
    }
}
