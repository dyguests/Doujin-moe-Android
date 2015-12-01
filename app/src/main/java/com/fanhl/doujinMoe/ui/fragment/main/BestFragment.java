package com.fanhl.doujinMoe.ui.fragment.main;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.NonNull;

/**
 * Top Rated
 */
public class BestFragment extends AbsHomeFragment {
    public static BestFragment newInstance() {
        return new BestFragment();
    }

    @Override
    @NonNull
    protected String getSection() {
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
    protected String getSortType() {
        return "mouth";
    }
}
