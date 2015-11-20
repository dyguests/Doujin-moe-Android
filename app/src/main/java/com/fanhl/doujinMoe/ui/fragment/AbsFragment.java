package com.fanhl.doujinMoe.ui.fragment;

import android.app.Fragment;

import com.fanhl.doujinMoe.App;

/**
 * Created by fanhl on 15/11/20.
 */
public abstract class AbsFragment extends Fragment {

    public App app() {
        return (App) getActivity().getApplication();
    }
}
