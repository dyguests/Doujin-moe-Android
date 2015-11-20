package com.fanhl.doujinMoe.api;

import android.content.Context;
import android.util.Log;

import com.fanhl.doujinMoe.exception.GetBookFailException;
import com.fanhl.doujinMoe.model.Book;
import com.fanhl.doujinMoe.util.FileCacheManager;

import java.io.File;
import java.util.List;

/**
 * Created by fanhl on 15/11/17.
 */
public class BookApi extends BaseApi {
    public static File createBookDir(Context context, Book book) {
        Log.d(TAG, "创建书籍目录 book:" + book.name);
        FileCacheManager m = FileCacheManager.getInstance(context);
        return m.createBookDir(book);
    }

    public static Book getBookFormJson(Context context, Book book) {
        Log.d(TAG, "从本地json中取得book信息:" + book.name);
        FileCacheManager m = FileCacheManager.getInstance(context);
        return m.getBookFormJson(book);
    }

    public static boolean saveBookJson(Context context, Book book) {
        Log.d(TAG, "保存书籍Json:" + book.name);
        FileCacheManager m = FileCacheManager.getInstance(context);
        return m.saveBookJson(book);
    }

    public static List<Book> getLocalBooks(Context context) throws GetBookFailException {
        Log.d(TAG, "取得本地的书籍.");

        FileCacheManager m = FileCacheManager.getInstance(context);
        return m.getLocalBooks();
    }
}
