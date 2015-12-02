package com.fanhl.doujinMoe.ui.adapter;

import android.app.Fragment;
import android.app.FragmentManager;
import android.support.design.widget.NavigationView;
import android.support.v13.app.FragmentPagerAdapter;

import com.fanhl.doujinMoe.R;
import com.fanhl.doujinMoe.ui.MainActivity;
import com.fanhl.doujinMoe.ui.fragment.main.AbcFragment;
import com.fanhl.doujinMoe.ui.fragment.main.ArtistFragment;
import com.fanhl.doujinMoe.ui.fragment.main.BestFragment;
import com.fanhl.doujinMoe.ui.fragment.main.CategoryFragment;
import com.fanhl.doujinMoe.ui.fragment.main.DownloadedFragment;
import com.fanhl.doujinMoe.ui.fragment.main.NewestFragment;
import com.fanhl.doujinMoe.ui.fragment.main.SearchFragment;

/**
 * Created by fanhl on 15/11/5.
 */
public class MainPagerAdapter extends FragmentPagerAdapter {

    public static final int NEWEST_INDEX     = 0;
    public static final int BEST_INDEX       = 1;
    public static final int ARTIST_INDEX     = 2;
    public static final int CATEGORY_INDEX   = 3;
    public static final int ABC_INDEX        = 4;
    public static final int SEARCH_INDEX     = 5;
    public static final int DOWNLOADED_INDEX = 6;

    public static final int PAGE_COUNT = 7;

    private NewestFragment     newestFragment;
    private BestFragment       bestFragment;
    private ArtistFragment     artistFragment;
    private CategoryFragment   categoryFragment;
    private AbcFragment        abcFragment;
    private SearchFragment     searchFragment;
    private DownloadedFragment downloadedFragment;

    public MainPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case NEWEST_INDEX:
                if (newestFragment == null) newestFragment = NewestFragment.newInstance();
                return newestFragment;
            case BEST_INDEX:
                if (bestFragment == null) bestFragment = BestFragment.newInstance();
                return bestFragment;
            case ARTIST_INDEX:
                if (artistFragment == null) artistFragment = ArtistFragment.newInstance();
                return artistFragment;
            case CATEGORY_INDEX:
                if (categoryFragment == null) categoryFragment = CategoryFragment.newInstance();
                return categoryFragment;
            case ABC_INDEX:
                if (abcFragment == null) abcFragment = AbcFragment.newInstance();
                return abcFragment;
            case SEARCH_INDEX:
                if (searchFragment == null) searchFragment = SearchFragment.newInstance();
                return abcFragment;
            case DOWNLOADED_INDEX:
                if (downloadedFragment == null) downloadedFragment = DownloadedFragment.newInstance();
                return downloadedFragment;
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
            case NEWEST_INDEX:
                activity.setTitle(activity.getString(R.string.title_newest));
                navigationView.setCheckedItem(R.id.nav_newest);
                break;
            case BEST_INDEX:
                activity.setTitle(activity.getString(R.string.title_best));
                navigationView.setCheckedItem(R.id.nav_best);
                break;
            case ARTIST_INDEX:
                activity.setTitle(activity.getString(R.string.title_artist));
                navigationView.setCheckedItem(R.id.nav_artist);
                break;
            case CATEGORY_INDEX:
                activity.setTitle(activity.getString(R.string.title_category));
                navigationView.setCheckedItem(R.id.nav_category);
                break;
            case ABC_INDEX:
                activity.setTitle(activity.getString(R.string.title_abc));
                navigationView.setCheckedItem(R.id.nav_abc);
                break;
            case SEARCH_INDEX:
                activity.setTitle(activity.getString(R.string.title_abc));
                navigationView.setCheckedItem(R.id.nav_abc);
                break;
            case DOWNLOADED_INDEX:
                activity.setTitle(activity.getString(R.string.title_downloaded));
                navigationView.setCheckedItem(R.id.nav_downloaded);
                break;
            default:
                break;
        }
    }

    @Override
    public int getCount() {
        return PAGE_COUNT;
    }
}
