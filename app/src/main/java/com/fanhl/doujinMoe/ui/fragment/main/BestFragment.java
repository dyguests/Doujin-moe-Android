package com.fanhl.doujinMoe.ui.fragment.main;

import android.support.annotation.NonNull;

import com.fanhl.doujinMoe.R;

/**
 * Top Rated
 */
public class BestFragment extends AbsHomeFragment {
    public static BestFragment newInstance() {
        return new BestFragment();
    }

    @Override
    @NonNull
    protected String getToken() {
        return "best";
    }

    /**
     * <li>ever</li>
     * <li>year</li>
     * <li>mouth</li>
     *
     * @return
     */
    @Override
    @NonNull
    protected String getSort() {
        return "mouth";
    }

    @Override
    protected int getLoadFailMsgResId() {
        return R.string.text_best_get_fail;
    }
}
