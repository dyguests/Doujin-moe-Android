package com.fanhl.doujinMoe.ui.fragment.main;

import android.support.annotation.NonNull;

import com.fanhl.doujinMoe.R;

/**
 * Created by fanhl on 15/11/8.
 */
public class ArtistFragment extends AbsHomeFragment {

    public static ArtistFragment newInstance() {
        return new ArtistFragment();
    }

    @Override
    @NonNull
    protected String getToken() {
        return "artist";
    }

    @Override
    @NonNull
    protected String getSort() {
        return "name";
    }

    @Override
    protected int getLoadFailMsgResId() {
        return R.string.text_artist_get_fail;
    }
}
