package com.fanhl.doujinMoe.ui.adapter;

import android.app.Fragment;
import android.app.FragmentManager;
import android.support.design.widget.NavigationView;
import android.support.v13.app.FragmentPagerAdapter;

import com.fanhl.doujinMoe.R;
import com.fanhl.doujinMoe.ui.MainActivity;
import com.fanhl.doujinMoe.ui.fragment.main.ArtistFragment;
import com.fanhl.doujinMoe.ui.fragment.main.BestFragment;
import com.fanhl.doujinMoe.ui.fragment.main.DownloadedFragment;
import com.fanhl.doujinMoe.ui.fragment.main.NewestFragment;

/**
 * Created by fanhl on 15/11/5.
 */
public class MainPagerAdapter extends FragmentPagerAdapter {

    public static final int NEWEST_INDEX     = 0;
    public static final int BEST_INDEX       = 1;
    public static final int ARTIST_INDEX     = 2;
    public static final int DOWNLOADED_INDEX = 3;

    public static final int PAGE_COUNT = 4;

    private final NewestFragment newestFragment;
    private final BestFragment   bestFragment;
    private final ArtistFragment artistFragment;

    private final DownloadedFragment downloadedFragment;

    public MainPagerAdapter(FragmentManager fm) {
        super(fm);

        newestFragment = NewestFragment.newInstance();
        bestFragment = BestFragment.newInstance();
        artistFragment = ArtistFragment.newInstance();

        downloadedFragment = DownloadedFragment.newInstance();
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case NEWEST_INDEX:
                return newestFragment;
            case BEST_INDEX:
                return bestFragment;
            case ARTIST_INDEX:
                return artistFragment;
            case DOWNLOADED_INDEX:
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
