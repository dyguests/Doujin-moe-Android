package com.fanhl.doujinMoe.api;

import android.util.Log;

import com.fanhl.doujinMoe.api.common.DouJinMoeUrl;
import com.fanhl.doujinMoe.exception.GetDataFailException;
import com.fanhl.doujinMoe.model.Book;
import com.fanhl.doujinMoe.model.Page;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

/**
 * Created by fanhl on 15/11/8.
 */
public class PageApi extends BaseApi {
    private static final String TAG = PageApi.class.getSimpleName();

    public static Void pages(Book book) throws GetDataFailException, IOException {
        Log.d(TAG, "取得书籍详细内容.");
        String token = "mm1qk0kqrf6hs6r82dbkli6fb4";// token();
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
}
