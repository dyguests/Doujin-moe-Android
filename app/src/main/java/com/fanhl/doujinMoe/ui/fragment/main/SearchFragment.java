package com.fanhl.doujinMoe.ui.fragment.main;

import android.support.annotation.NonNull;

/**
 * http://m.doujin-moe.us/search?search=lolicon&sort=-created
 * <p>
 * Created by fanhl on 15/12/1.
 */
public class SearchFragment extends AbsHomeFragment {
    public static SearchFragment newInstance() {
        return new SearchFragment();
    }

    @Override
    @NonNull
    protected String getToken() {
        return "search";
    }

    /**
     * @return
     */
    @Override
    @NonNull
    protected String getSort() {
        return "name";
    }

    /**
     *
     * @return
     */
    @NonNull
    @Override
    protected String getParam() {
        return "futanari";
    }

    // FIXME: 15/12/2 过滤条件
}

