package com.fanhl.doujinMoe.api;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;

import com.fanhl.doujinMoe.api.common.DouJinMoeUrl;
import com.fanhl.doujinMoe.exception.GetDataFailException;
import com.fanhl.doujinMoe.model.Book;
import com.fanhl.doujinMoe.model.Page;
import com.fanhl.doujinMoe.util.FileCacheManager;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.IOException;

import okio.BufferedSink;
import okio.Okio;

/**
 * Created by fanhl on 15/11/8.
 */
public class PageApi extends BaseApi {
    private static final String TAG = PageApi.class.getSimpleName();

    public static Void pages(Book book) throws GetDataFailException, IOException {
        Log.d(TAG, "取得书籍详细内容.");
        String token = /*"mm1qk0kqrf6hs6r82dbkli6fb4";//*/ token();
        Document document = Jsoup.connect(DouJinMoeUrl.detailUrl(book.token))
                .cookie(TOKEN_KEY, token)
                .data("action", "get")
                .timeout(TIME_OUT)
                .post();

        if (document == null) throw new GetDataFailException("取不到书籍详细页面的数据");

        Element  foldercontent = document.getElementById("foldercontent");
        Elements djms          = foldercontent.select("djm");

        for (Element djm : djms) {
            Page page = new Page();
            page.preview = djm.attr("thumb");
            page.href = djm.attr("file");

            Log.d(TAG, page.toString());
            book.pages.add(page);
        }

        return null;
    }

    public static boolean isCached(Context context, String url) {
        return FileCacheManager.getInstance(context).isCached(url);
    }

    public static Drawable getCachedDrawable(Context context, String url) {
        return FileCacheManager.getInstance(context).getCachedDrawable(url);
    }

    public static boolean isPageDownloaded(Context context, Book book, int index) {
        return FileCacheManager.getInstance(context).isPageDownloaded(book, index);
    }

    public static boolean downloadPage(Context context, Book book, int index) {
        FileCacheManager m = FileCacheManager.getInstance(context);

        File pageFile = m.createPageFile(book, index);

        if (pageFile == null) return false;

        Page page = book.pages.get(index);

        //download file
        OkHttpClient client  = new OkHttpClient();
        Request      request = new Request.Builder().url(page.href).build();
        Response     response;
        BufferedSink sink    = null;
        try {
            response = client.newCall(request).execute();
            sink = Okio.buffer(Okio.sink(pageFile));
            sink.writeAll(response.body().source());

            return true;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (sink != null) {
                try {
                    sink.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return false;
    }

    /**
     * 生成page名字
     *
     * @param book
     * @param index
     */
    public static String getPageName(Book book, int index) {
        Page page = book.pages.get(index);
        return (index + 1) + "." + getExtension(page);
    }

    /**
     * 取得书籍扩展名
     *
     * @param page
     * @return
     */
    public static String getExtension(Page page) {
        if (page == null || page.preview == null) return "";// FIXME: 15/11/20 preview 和 href 的文件 扩展名是一致的么???

        String[] parts = page.preview.split("\\.");

        if (parts.length < 2) return "";

        return parts[parts.length - 1];
    }
}
