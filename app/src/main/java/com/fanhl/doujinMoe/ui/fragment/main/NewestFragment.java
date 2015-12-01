package com.fanhl.doujinMoe.ui.fragment.main;

import android.support.annotation.NonNull;

/**
 * Created by fanhl on 15/11/8.
 */
public class NewestFragment extends AbsHomeFragment {

    public static NewestFragment newInstance() {
        return new NewestFragment();
    }

    @Override
    @NonNull
    protected String getSection() {
        return "newest";
    }

    @Override
    @NonNull
    protected String getSortType() {
        return "name";
    }
}
