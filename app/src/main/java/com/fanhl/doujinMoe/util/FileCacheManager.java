package com.fanhl.doujinMoe.util;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.util.Log;

import com.fanhl.doujinMoe.api.PageApi;
import com.fanhl.doujinMoe.model.Book;
import com.fanhl.doujinMoe.model.Page;
import com.fanhl.util.GsonUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FileCacheManager {
    private static final String TAG            = FileCacheManager.class.getSimpleName();
    public static final  String PROJECT_FOLDER = "Doujin-Moe";
    public static final  String IMAGE_FOLDER   = "images";

    public static final String BOOK_JSON_FILENAME = "book.json";

    private static FileCacheManager mInstance;

    private File mCacheDir, mExternalDir;

    public static FileCacheManager getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new FileCacheManager(context);
        }

        return mInstance;
    }

    private FileCacheManager(Context context) {
        try {
            mCacheDir = context.getExternalCacheDir();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (mCacheDir == null) {
            String cacheAbsDir = "/Android/data" + context.getPackageName() + "/cache/";
            mCacheDir = new File(Environment.getExternalStorageDirectory().getPath() + cacheAbsDir);
        }
        if (mExternalDir == null) {
            mExternalDir = new File(Environment.getExternalStorageDirectory().getPath(), PROJECT_FOLDER);
        }
    }

    /**
     * uri的图片是否已缓存
     *
     * @param loadUri
     * @return
     */
    public boolean isCached(String loadUri) {
        // FIXME: 15/11/19
        return false;
    }

    public Drawable getCachedDrawable(String url) {
        if (!isCached(url)) {
            return null;
        }

        // FIXME: 15/11/19

        return null;
    }

    public File createCacheDir() {
        if (mCacheDir.exists() && mCacheDir.isDirectory()) {
            return mCacheDir;
        }
        if (mCacheDir.mkdirs()) {
            return mCacheDir;
        }
        return null;
    }

    public File getCacheDir() {
        return mCacheDir;
    }

    public File createBookDir(Book book) {
//        Log.d(TAG, "生成书籍存放路径(若有则直接返回):" + book);
        File bookDir = new File(mExternalDir, book.name);// FIXME: 15/11/17 是否需要 replace 特殊字符

        if (bookDir.exists() && bookDir.isDirectory()) {
            return bookDir;
        }
        if (bookDir.mkdirs()) {
            return bookDir;
        }
        Log.e(TAG, "生成书籍存放路径失败:" + book);
        return null;
    }

    private File getBookDir(Book book) {
//        Log.d(TAG, "获取书籍存放路径(若无则返回null):" + book);
        File bookDir = new File(mExternalDir, book.name);// FIXME: 15/11/17 是否需要 replace 特殊字符

        if (bookDir.exists() && bookDir.isDirectory()) {
            return bookDir;
        }

        return null;
    }

    public File createBookImagesDir(Book book) {
//        Log.d(TAG, "生成书籍图片存放路径(若有则直接返回):" + book);

        File bookDir = createBookDir(book);

        if (bookDir == null) {
            Log.e(TAG, "生成书籍图片存放路径失败:" + book);
            return null;
        }

        File bookImagesDir = new File(bookDir, IMAGE_FOLDER);

        if (bookImagesDir.exists() && bookImagesDir.isDirectory()) {
            return bookImagesDir;
        }
        if (bookImagesDir.mkdirs()) {
            return bookImagesDir;
        }
        Log.e(TAG, "生成书籍图片存放路径失败:" + book);
        return null;
    }

    public File getBookImagesDir(Book book) {
//        Log.d(TAG, "获取书籍图片存放路径(若无则返回null):" + book);

        File bookDir = getBookDir(book);

        if (bookDir == null) return null;

        File bookImagesDir = new File(bookDir, IMAGE_FOLDER);

        if (bookImagesDir.exists() && bookImagesDir.isDirectory()) return bookImagesDir;

        return null;
    }

    public Book getBookFormJson(Book book) {
        Log.d(TAG, "从json中取得book信息:" + book.name);
        File bookDir  = getBookDir(book);
        File bookFile = new File(bookDir, BOOK_JSON_FILENAME);
        if (bookFile.exists() && bookFile.isFile()) {
            String bookJson = FileUtil.readFile(bookFile);
            Book   book1    = GsonUtil.obj(bookJson, Book.class);
            Log.d(TAG, "取得json成功");
            return book1;
        }
        Log.d(TAG, "取得json失败");
        return book;
    }

    public boolean saveBookJson(Book book) {
        Log.d(TAG, "保存书籍:" + book.name);
        File bookDir = getBookDir(book);

        if (bookDir == null) {
            Log.d(TAG, "书籍所在文件夹不存在.");
            return false;
        }

        File f = new File(bookDir, BOOK_JSON_FILENAME);

        if (f.exists() && !f.delete()) {
            Log.d(TAG, "删除旧的book.json失败");
            return false;
        }


        String json = GsonUtil.json(book);

        return FileUtil.writeFile(f, json);
    }

    public boolean isPageDownloaded(Book book, int index) {
        File bookImagesDir = getBookImagesDir(book);
        if (bookImagesDir == null) return false;

        List<Page> pages = book.pages;
        if (pages == null || index >= pages.size()) return false;
        Page page = pages.get(index);
        if (page == null || page.href == null) return false;
        //page: http://static.doujin-moe.us/t-p9grmmtq.jpg
        //1.jpg
        String pageName = PageApi.getPageName(book, index);

        File pageFile = new File(bookImagesDir, pageName);
        return pageFile.exists() && pageFile.isFile();

    }

    /**
     * 下载前先生成file
     *
     * @param book
     * @param index
     * @return
     */
    public File createPageFile(Book book, int index) {
        if (book == null) return null;

        File bookImagesDir = createBookImagesDir(book);

        if (bookImagesDir == null) return null;

        String pageName = PageApi.getPageName(book, index);

        File pageFile = new File(bookImagesDir, pageName);

        return pageFile;
    }

    /**
     * 取得本地书籍(已下载,喜爱...)
     *
     * @return
     */
    public List<Book> getLocalBooks() {
        List<Book> list = new ArrayList<>();

        File[] files = mExternalDir.listFiles(File::isDirectory);
        if (files == null) return list;
        Arrays.sort(files, (lhs, rhs) -> lhs.getName().compareTo(rhs.getName()));// FIXME: 15/11/20 不排序?

        for (File file : files) {
            File bookJsonFile = new File(file, BOOK_JSON_FILENAME);
            if (bookJsonFile.exists()) {
                String bookJson = FileUtil.readFile(bookJsonFile);
                list.add(GsonUtil.obj(bookJson, Book.class));
            }
        }
        return list;
    }

    public boolean deleteBook(Book book) {
        File bookDir = getBookDir(book);
        return bookDir == null || FileUtil.deleteDirectory(bookDir);

    }

    public File createCacheFile() {
        File cacheDir = createCacheDir();

        File file = new File(cacheDir, "cache.cache");

        if (file.exists()) {
            file.delete();
        }
        return file;
    }
}
