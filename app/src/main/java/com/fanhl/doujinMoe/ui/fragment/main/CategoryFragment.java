package com.fanhl.doujinMoe.ui.fragment.main;

import android.support.annotation.NonNull;

import com.fanhl.doujinMoe.R;

/**
 * http://m.doujin-moe.us/category
 * Created by fanhl on 15/12/1.
 */
public class CategoryFragment extends AbsHomeFragment {
    public static CategoryFragment newInstance() {
        return new CategoryFragment();
    }

    // FIXME: 15/12/1 注,这里点item进去不是书籍,而是另一个列表了

    @Override
    @NonNull
    protected String getToken() {
        return "category";
    }

    /**
     * @return
     */
    @Override
    @NonNull
    protected String getSort() {
        return "name";
    }

    @Override
    protected int getLoadFailMsgResId() {
        return R.string.text_category_get_fail;
    }
}
