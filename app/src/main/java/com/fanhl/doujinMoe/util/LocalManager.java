package com.fanhl.doujinMoe.util;

import android.content.Context;
import android.os.Handler;
import android.util.Log;

import com.fanhl.doujinMoe.api.BookApi;
import com.fanhl.doujinMoe.exception.GetBookFailException;
import com.fanhl.doujinMoe.model.Book;
import com.fanhl.util.ThreadUtil;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.android.schedulers.HandlerScheduler;

/**
 * 本地管理(最近阅读,喜爱,已下载)
 * Created by fanhl on 15/11/20.
 */
public class LocalManager {
    public static final String TAG = LocalManager.class.getSimpleName();

    private static LocalManager mInstance;

    private final Handler localHandler;

    private final Context context;

    List<Book> recentBooks;
    List<Book> loveBooks;
    List<Book> downloadedBooks;

    public static LocalManager getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new LocalManager(context);
        }

        return mInstance;
    }

    private LocalManager(Context context) {
        localHandler = ThreadUtil.createBackgroundHandler("DownloadThread");

        this.context = context;

        recentBooks = new ArrayList<>();
        loveBooks = new ArrayList<>();
        downloadedBooks = new ArrayList<>();

        Observable.<List<Book>>create(subscriber -> {
            try {
                subscriber.onNext(BookApi.getLocalBooks(context));
                subscriber.onCompleted();
            } catch (GetBookFailException e) {
                subscriber.onError(e);
            }
        }).subscribeOn(HandlerScheduler.from(localHandler))
                .flatMap(Observable::<Book>from)
                .subscribe(book -> {
                    if (book.recent != null) {
                        recentBooks.add(book);
                    }
                    if (book.downloaded) {
                        downloadedBooks.add(book);
                    }
                }, throwable -> Log.e(TAG, Log.getStackTraceString(throwable)), () -> {
                    // FIXME: 15/11/20 recentBooks.sort desc
                });
    }

    public List<Book> getRecentBooks() {
        return recentBooks;
    }

    public List<Book> getLoveBooks() {
        return loveBooks;
    }

    public List<Book> getDownloadedBooks() {
        return downloadedBooks;
    }
}
