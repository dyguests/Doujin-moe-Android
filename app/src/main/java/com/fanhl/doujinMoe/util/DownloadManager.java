package com.fanhl.doujinMoe.util;

import android.content.Context;
import android.os.Handler;
import android.os.HandlerThread;

import com.fanhl.doujinMoe.api.BookApi;
import com.fanhl.doujinMoe.api.PageApi;
import com.fanhl.doujinMoe.model.Book;
import com.fanhl.doujinMoe.model.IndexItem;

import java.util.Queue;

import rx.Observable;
import rx.Scheduler;
import rx.android.schedulers.HandlerScheduler;
import rx.functions.Action0;

import static android.os.Process.THREAD_PRIORITY_BACKGROUND;

/**
 * Created by fanhl on 15/11/19.
 */
public class DownloadManager {

    private static DownloadManager  mInstance;
    private final  Scheduler.Worker worker;

    /**
     * 下载用handler
     */
    protected Handler downloadHandler;

    private final Context context;

    /*要下载的书*/
    Queue<Book> waitBooks;
    /*正在下载的书*/
    Book        downloadingBook;
    /*下载完成的书*/
    Queue<Book> downloadedBooks;
    /*下载失败的书*/
    Queue<Book> failBooks;

    public static DownloadManager getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new DownloadManager(context);
        }

        return mInstance;
    }

    public DownloadManager(Context context) {
        HandlerThread downloadThread = new HandlerThread("SchedulerSample-BackgroundThread", THREAD_PRIORITY_BACKGROUND);
        downloadThread.start();
        downloadHandler = new Handler(downloadThread.getLooper());

        this.context = context;

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
                        }, () -> {
                            failBooks.offer(downloadingBook);
                            downloadingBook = null;
                        });
                    }
                }

                // recurse until unsubscribed (schedule will do nothing if unsubscribed)
                worker.schedule(this);
            }

        });

        // some time later...
        //worker.unsubscribe();

//        new Thread(() -> {
//            while (true) {
//                if (downloadingBook == null) {
//                    Book book = waitBooks.poll();
//                    if (book != null) {
//                        downloadingBook = book;
//                        download(book, () -> {
//                            downloadedBooks.offer(downloadingBook);
//                            downloadingBook = null;
//                        }, () -> {
//                            failBooks.offer(downloadingBook);
//                            downloadingBook = null;
//                        });
//                    }
//                }
//            }
//        }, "下载线程").run();
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
        //创建文件夹
        BookApi.createBookDir(context, book);

        //下载图片
        final boolean[] isAllDownloaded = {true};
        Observable.<IndexItem<Book>>create(subscriber -> {
            for (int i = 0; i < book.pages.size(); i++) {
                subscriber.onNext(new IndexItem<>(book, i));
            }
            subscriber.onCompleted();
        }).filter(bookIndexItem -> PageApi.isPageDownloaded(context, bookIndexItem.item, bookIndexItem.index))
                .subscribe(bookIndexItem -> {
                    if (!PageApi.downloadPage(context, bookIndexItem.item, bookIndexItem.index))
                        isAllDownloaded[0] = false;
                }, throwable -> onDownloadFailListener.onDownloadFail(), () -> {
                    if (isAllDownloaded[0]) onDownloadSuccessListener.onDownloadSuccess();
                    else onDownloadFailListener.onDownloadFail();
                });
    }

    public interface OnDownloadSuccessListener {
        void onDownloadSuccess();
    }

    public interface OnDownloadFailListener {
        void onDownloadFail();
    }
}