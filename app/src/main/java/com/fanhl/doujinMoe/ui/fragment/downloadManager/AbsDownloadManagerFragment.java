package com.fanhl.doujinMoe.ui.fragment.downloadManager;

import android.content.Context;

import com.fanhl.doujinMoe.interfaceX.OnDownloadManagerBookChangeListener;
import com.fanhl.doujinMoe.ui.DownloadManagerActivity;
import com.fanhl.doujinMoe.ui.fragment.AbsFragment;

/**
 * Created by fanhl on 15/11/27.
 */
public abstract class AbsDownloadManagerFragment extends AbsFragment implements OnDownloadManagerBookChangeListener {
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        ((DownloadManagerActivity) context).addOnDownloadManagerBookChangeListener(this);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        ((DownloadManagerActivity) getActivity()).removeOnDownloadManagerBookChangeListener(this);
    }
}