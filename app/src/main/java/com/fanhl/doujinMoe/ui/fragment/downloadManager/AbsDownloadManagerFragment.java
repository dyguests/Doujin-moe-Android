package com.fanhl.doujinMoe.ui.fragment.downloadManager;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import com.fanhl.doujinMoe.interfaceX.OnDownloadManagerBookChangeListener;
import com.fanhl.doujinMoe.ui.DownloadManagerActivity;
import com.fanhl.doujinMoe.ui.fragment.AbsFragment;

/**
 * Created by fanhl on 15/11/27.
 */
public abstract class AbsDownloadManagerFragment extends AbsFragment implements OnDownloadManagerBookChangeListener {
    public static final String TAG = AbsDownloadManagerFragment.class.getSimpleName();

    /**
     * 这个方法实际没有调用!!!,所以我还是用 onAttach(Activity activity) 了!!?
     * fixme 两个方法都实现的话,会不会重复?
     * @param context
     */
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.d(TAG, "onAttach(context) addOnDownloadManagerBookChangeListener");
        ((DownloadManagerActivity) context).addOnDownloadManagerBookChangeListener(this);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        Log.d(TAG, "onAttach(activity) addOnDownloadManagerBookChangeListener");
        ((DownloadManagerActivity) activity).addOnDownloadManagerBookChangeListener(this);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.d(TAG, "onDetach removeOnDownloadManagerBookChangeListener");
        ((DownloadManagerActivity) getActivity()).removeOnDownloadManagerBookChangeListener(this);
    }
}