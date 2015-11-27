package com.fanhl.doujinMoe.util;

import android.content.Context;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.util.Log;

import com.fanhl.doujinMoe.api.BookApi;
import com.fanhl.doujinMoe.api.PageApi;
import com.fanhl.doujinMoe.model.Book;
import com.fanhl.doujinMoe.model.IndexItem;
import com.fanhl.util.ThreadUtil;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import rx.Observable;
import rx.Scheduler;
import rx.android.schedulers.HandlerScheduler;
import rx.functions.Action0;

/**
 * 下载处理管理
 * Created by fanhl on 15/11/19.
 */
public class DownloadManager {
    public static final String TAG = DownloadManager.class.getSimpleName();

    private static DownloadManager  mInstance;
    private final  Scheduler.Worker worker;

    /**
     * 下载用handler
     */
    protected Handler downloadHandler;

    private final Context context;

    /*要下载的书 注:当Queue用*/
    LinkedList<Book> waitBooks;
    /*正在下载的书*/
    Book             downloadingBook;
    /*下载完成的书*/
    LinkedList<Book> downloadedBooks;
    /*下载失败的书*/
    LinkedList<Book> failBooks;

    /*用于回调,下载完成后在activity中显示*/
    private OnDownloadManagerInteractionListener interactionListener;

    private List<OnDownloadProgressChangeListener> mOnDownloadProgressChangeListeners;

    public static DownloadManager getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new DownloadManager(context);
        }

        return mInstance;
    }

    private DownloadManager(Context context) {
        downloadHandler = ThreadUtil.createBackgroundHandler("DownloadThread");

        this.context = context;

        waitBooks = new LinkedList<>();
        //downloadingBook = null;
        downloadedBooks = new LinkedList<>();
        failBooks = new LinkedList<>();

        worker = HandlerScheduler.from(downloadHandler).createWorker();
        worker.schedule(new Action0() {

            @Override
            public void call() {
                if (downloadingBook == null) {
                    Book book = waitBooks.poll();
                    if (book != null) {
                        downloadingBook = book;
                        download(book, () -> {
                            downloadedBooks.offer(downloadingBook);
                            downloadingBook = null;
                            if (interactionListener != null) interactionListener.onDMDownloadSuccess(book);
                            Log.i(TAG, "下载完成:" + book.name);
                        }, () -> {
                            failBooks.offer(downloadingBook);
                            downloadingBook = null;
                            if (interactionListener != null) interactionListener.onDMDownloadFail(book);
                            Log.e(TAG, "下载失败:" + book.name);
                        });
                    }
                }

                ThreadUtil.sleep(1000);
                // recurse until unsubscribed (schedule will do nothing if unsubscribed)
                worker.schedule(this);
            }
        });

        // some time later...
        //worker.unsubscribe();
    }

    @NonNull
    private String getString(int id) {
        return context.getResources().getString(id);
    }

    /**
     * 增加要下载的book
     *
     * @param book
     */
    public void accept(Book book) {
        waitBooks.offer(book);
    }

    private void download(Book book, OnDownloadSuccessListener onDownloadSuccessListener, OnDownloadFailListener onDownloadFailListener) {
        Log.d(TAG, "下载书籍:" + book.name);
        //创建文件夹
        BookApi.createBookDir(context, book);

        //下载图片
        final boolean[] isAllDownloaded = {true};
        Observable.<IndexItem<Book>>create(subscriber -> {
            for (int i = 0; i < book.pages.size(); i++) {
                subscriber.onNext(new IndexItem<>(book, i));
            }
            subscriber.onCompleted();
        }).filter(bookIndexItem -> !PageApi.isPageDownloaded(context, bookIndexItem.item, bookIndexItem.index))
                .subscribe(bookIndexItem -> {
                    if (PageApi.downloadPage(context, bookIndexItem.item, bookIndexItem.index)) {
                        dispatchOnDownloadProgressChanged(bookIndexItem.item, bookIndexItem.index);
                    } else {
                        isAllDownloaded[0] = false;
                    }
                }, throwable -> onDownloadFailListener.onDownloadFail(), () -> {
                    if (isAllDownloaded[0]) {
                        book.downloaded = true;
                        BookApi.saveBookJson(context, book);
                        onDownloadSuccessListener.onDownloadSuccess();
                    } else onDownloadFailListener.onDownloadFail();
                });
    }

    private void dispatchOnDownloadProgressChanged(Book book, int index) {
        if (mOnDownloadProgressChangeListeners != null) {
            for (OnDownloadProgressChangeListener mOnDownloadProgressChangeListener : mOnDownloadProgressChangeListeners) {
                if (mOnDownloadProgressChangeListener != null) {
                    mOnDownloadProgressChangeListener.onDownloadProgressChanged(book, index);
                }
            }
        }
    }


    public void registerOnDownloadManagerInteractionListener(OnDownloadManagerInteractionListener onDownloadManagerInteractionListener) {
        this.interactionListener = onDownloadManagerInteractionListener;
    }

    public void unregisterOnDownloadManagerInteractionListener(OnDownloadManagerInteractionListener onDownloadManagerInteractionListener) {
        if (this.interactionListener == onDownloadManagerInteractionListener) {
            this.interactionListener = null;
        }
    }

    public void addOnDownloadProgressChangeListener(OnDownloadProgressChangeListener listener) {
        if (mOnDownloadProgressChangeListeners == null) {
            mOnDownloadProgressChangeListeners = new ArrayList<>();
        }
        mOnDownloadProgressChangeListeners.add(listener);
    }

    public void removeOnDownloadProgressChangeListener(OnDownloadProgressChangeListener listener) {
        if (mOnDownloadProgressChangeListeners != null) {
            mOnDownloadProgressChangeListeners.remove(listener);
        }
    }

    public void clearOnDownloadProgressChangeListener() {
        if (mOnDownloadProgressChangeListeners != null) {
            mOnDownloadProgressChangeListeners.clear();
        }
    }


    /*判断当前书籍是否 加入 要下载列表 或者 正在下载中*/
    public boolean isAccepted(Book book) {// FIXME: 15/11/20 加锁?
        for (Book waitBook : waitBooks) {
            if (waitBook.name.equals(book.name)) {
                return true;
            }
        }

        if (downloadingBook != null) {
            if (downloadingBook.name.equals(book.name)) {
                return true;
            }
        }

        return false;
    }


    public LinkedList<Book> getWaitBooks() {
        return waitBooks;
    }

    public Book getDownloadingBook() {
        return downloadingBook;
    }

    public LinkedList<Book> getDownloadedBooks() {
        return downloadedBooks;
    }

    public LinkedList<Book> getFailBooks() {
        return failBooks;
    }

    public interface OnDownloadSuccessListener {
        void onDownloadSuccess();
    }

    public interface OnDownloadFailListener {
        void onDownloadFail();
    }

    public interface OnDownloadManagerInteractionListener {
        void onDMDownloadSuccess(Book book);

        void onDMDownloadFail(Book book);
    }

    /**
     * 用于通知书籍的下载进度变更
     */
    public interface OnDownloadProgressChangeListener {
        void onDownloadProgressChanged(Book book, int index);
    }
}
