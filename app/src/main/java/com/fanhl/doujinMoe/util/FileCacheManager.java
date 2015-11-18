package com.fanhl.doujinMoe.util;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.util.Log;

import com.facebook.binaryresource.BinaryResource;
import com.facebook.cache.common.CacheKey;
import com.facebook.imagepipeline.cache.DefaultCacheKeyFactory;
import com.facebook.imagepipeline.core.ImagePipelineFactory;
import com.facebook.imagepipeline.request.ImageRequest;
import com.fanhl.doujinMoe.model.Book;
import com.fanhl.util.GsonUtil;

import java.io.File;
import java.io.IOException;

public class FileCacheManager {
    private static final String TAG            = FileCacheManager.class.getSimpleName();
    public static final  String PROJECT_FOLDER = "Doujin-Moe";

    private static FileCacheManager mInstance;

    private File mCacheDir, mExternalDir, mBookDir;

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
        if (loadUri == null) return false;
        ImageRequest imageRequest = ImageRequest.fromUri(loadUri);
        CacheKey cacheKey = DefaultCacheKeyFactory.getInstance()
                .getEncodedCacheKey(imageRequest);
        return ImagePipelineFactory.getInstance()
                .getMainDiskStorageCache().hasKey(cacheKey);
    }

    public File getmCacheDir() {
        return mCacheDir;
    }

    private File getBookDir(Book book) {
        Log.d(TAG, "获取书籍存放路径(若无则创建):" + book);
        File bookDir = new File(mBookDir, book.name);// FIXME: 15/11/17 是否需要 replace 特殊字符

        if (bookDir.exists() && bookDir.isDirectory()) {
            return bookDir;
        }
        if (bookDir.mkdirs()) {
            return bookDir;
        }
        Log.d(TAG, "生成书籍存放路径失败:" + book);
        return null;
    }

    public Book getBookFormJson(Book book) {
        Log.i(TAG, "从json中取得book信息:" + book.name);
        File bookDir  = getBookDir(book);
        File bookFile = new File(bookDir, "book.json");
        if (bookFile.exists() && bookFile.isFile()) {
            String bookJson = FileUtil.readFile(bookFile);
            return GsonUtil.obj(bookJson, Book.class);
        }
        Log.e(TAG, "取得json失败");
        return book;
    }

    public boolean saveBookJson(Book book) {
        Log.d(TAG, "保存书籍:" + book.name);
        File bookDir = getBookDir(book);

        File f = new File(bookDir, "book.json");

        if (f.exists() && !f.delete()) {
            Log.d(TAG, "删除旧的book.json失败");
            return false;
        }


        String json = GsonUtil.json(book);

        return FileUtil.writeFile(f, json);
    }

    public Drawable getCachedDrawable(String url) {
        if (!isCached(url)) {
            return null;
        }

        ImageRequest imageRequest = ImageRequest.fromUri(url);
        CacheKey cacheKey = DefaultCacheKeyFactory.getInstance()
                .getEncodedCacheKey(imageRequest);
        BinaryResource resource = ImagePipelineFactory.getInstance()
                .getMainDiskStorageCache().getResource(cacheKey);
//        File file = ((FileBinaryResource) resource).getFile();

        try {
            return Drawable.createFromStream(resource.openStream(), "");
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}
