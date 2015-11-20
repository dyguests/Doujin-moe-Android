package com.fanhl.doujinMoe.ui.adapter;

import android.app.Fragment;
import android.app.FragmentManager;
import android.support.design.widget.NavigationView;
import android.support.v13.app.FragmentPagerAdapter;

import com.fanhl.doujinMoe.R;
import com.fanhl.doujinMoe.ui.MainActivity;
import com.fanhl.doujinMoe.ui.fragment.NewestFragment;

/**
 * Created by fanhl on 15/11/5.
 */
public class MainPagerAdapter extends FragmentPagerAdapter {

    private final NewestFragment newestFragment;
    private final NewestFragment contentFragment;

    public MainPagerAdapter(FragmentManager fm) {
        super(fm);

        newestFragment = NewestFragment.newInstance();
        contentFragment = NewestFragment.newInstance();
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return newestFragment;
            case 1:
                return contentFragment;
            default:
                return null;
        }
    }

    /**
     * 方法为title改名等
     *
     * @param activity
     * @param navigationView
     * @param position
     */
    public void pageSelected(MainActivity activity, NavigationView navigationView, int position) {
        switch (position) {
            case 0:
                activity.setTitle(activity.getString(R.string.title_newest));
                navigationView.setCheckedItem(R.id.nav_newest);
                break;
            case 1:
                activity.setTitle(activity.getString(R.string.text_best_get_fail));
                navigationView.setCheckedItem(R.id.nav_best);
                break;
            default:
                break;
        }
    }

    @Override
    public int getCount() {
        return 2;
    }
}
