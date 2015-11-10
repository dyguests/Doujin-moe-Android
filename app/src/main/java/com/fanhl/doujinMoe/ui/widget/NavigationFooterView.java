package com.fanhl.doujinMoe.ui.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import com.fanhl.doujinMoe.util.Utility;


public class NavigationFooterView extends View {

    public NavigationFooterView(Context context) {
        super(context);
    }

    public NavigationFooterView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public NavigationFooterView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        ViewGroup.LayoutParams params = getLayoutParams();

        if (isInEditMode()) {
            params.height = 0;
            return;
        }

        params.height = Utility.getNavigationBarHeight(getContext());
    }
}
