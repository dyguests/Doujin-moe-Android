package com.fanhl.doujinMoe.ui.fragment.main;

import android.support.annotation.NonNull;

import com.fanhl.doujinMoe.R;

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
    protected String getToken() {
        return "abc";
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

    @Override
    protected int getLoadFailMsgResId() {
        return R.string.text_abc_get_fail;
    }

    // FIXME: 15/12/2 过滤条件
}
