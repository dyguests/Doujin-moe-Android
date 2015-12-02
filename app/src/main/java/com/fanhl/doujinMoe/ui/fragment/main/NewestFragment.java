package com.fanhl.doujinMoe.ui.fragment.main;

import android.support.annotation.NonNull;

import com.fanhl.doujinMoe.R;

/**
 * Created by fanhl on 15/11/8.
 */
public class NewestFragment extends AbsHomeFragment {

    public static NewestFragment newInstance() {
        return new NewestFragment();
    }

    @Override
    @NonNull
    protected String getToken() {
        return "newest";
    }

    @Override
    @NonNull
    protected String getSort() {
        return "name";
    }

    @Override
    protected int getLoadFailMsgResId() {
        return R.string.text_newest_get_fail;
    }
}
