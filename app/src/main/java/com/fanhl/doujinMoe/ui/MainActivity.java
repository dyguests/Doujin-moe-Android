package com.fanhl.doujinMoe.ui;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.fanhl.doujinMoe.R;
import com.fanhl.doujinMoe.model.Book;
import com.fanhl.doujinMoe.ui.adapter.MainPagerAdapter;
import com.fanhl.doujinMoe.ui.common.AbsActivity;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends AbsActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    @Bind(R.id.drawer_layout)
    DrawerLayout         drawer;
    @Bind(R.id.nav_view)
    NavigationView       navigationView;
    @Bind(R.id.toolbar)
    Toolbar              toolbar;
    @Bind(R.id.fab)
    FloatingActionButton fab;
    @Bind(R.id.pager)
    ViewPager            mViewpager;

    //custom

    MainPagerAdapter mPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);

        fab.setOnClickListener(view -> Snackbar.make(view, "这个按钮没啥用", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show());

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);

        //custom

        mPagerAdapter = new MainPagerAdapter(getFragmentManager());
        mViewpager.setAdapter(mPagerAdapter);
        mViewpager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                mPagerAdapter.pageSelected(MainActivity.this, navigationView, position);
            }
        });

        //执行一次,初始化 显示标题, 抽屉选中项
//        mViewpager.setCurrentItem(0);//默认为0时不用写
        mPagerAdapter.pageSelected(this, navigationView, 0);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_download) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_newest) {
            mViewpager.setCurrentItem(MainPagerAdapter.NEWEST_INDEX);
        } else if (id == R.id.nav_best) {
            mViewpager.setCurrentItem(MainPagerAdapter.BEST_INDEX);
        } else if (id == R.id.nav_downloaded) {
            mViewpager.setCurrentItem(MainPagerAdapter.DOWNLOADED_INDEX);
        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    @Override
    public void onDMDownloadSuccess(Book book) {
        Snackbar.make(mViewpager, String.format(getString(R.string.download_book_success), book.name), Snackbar.LENGTH_LONG).setAction(R.string.action_check, v -> {
            // FIXME: 15/11/20 跳转到下载列表页面.
        }).show();
    }

    @Override
    public void onDMDownloadFail(Book book) {
        Snackbar.make(mViewpager, String.format(getString(R.string.download_book_success), book.name), Snackbar.LENGTH_LONG).show();
    }
}
