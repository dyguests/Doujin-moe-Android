package com.fanhl.doujinMoe.interfaceX;

import com.fanhl.doujinMoe.model.Book;

/**
 * 用于在父onAttach时绑定activity,并在DownloadManager下载成功/失败时通知Fragment
 * <p>
 * Created by fanhl on 15/11/27.
 */
public interface OnDownloadManagerBookChangeListener {
    /**
     * 用来响应 DownloadManager 下载书籍成功/失败
     *
     * @param book
     * @param success
     */
    void onDownloadBookChanged(Book book, boolean success);
}
