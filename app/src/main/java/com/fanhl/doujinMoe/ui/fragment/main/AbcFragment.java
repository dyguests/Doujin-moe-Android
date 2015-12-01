package com.fanhl.doujinMoe.ui.fragment.main;

import android.support.annotation.NonNull;

/**
 * http://m.doujin-moe.us/abc
 * Created by fanhl on 15/12/1.
 */
public class AbcFragment extends AbsHomeFragment {
    public static AbcFragment newInstance() {
        return new AbcFragment();
    }

    @Override
    @NonNull
    protected String getSection() {
        return "abc";
    }

    /**
     * @return
     */
    @Override
    @NonNull
    protected String getSortType() {
        return "name";
    }

    /**
     * <li>Non-series</li>
     * <li>0-9</li>
     * <li>A</li>
     * <li>A</li>
     * <li>B</li>
     * <li>...</li>
     * <li>Z</li>
     *
     * @return
     */
    @NonNull
    @Override
    protected String getParam() {
        return "Non-series";
    }
}
