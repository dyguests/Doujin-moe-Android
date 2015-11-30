package com.fanhl.doujinMoe.api;

import android.util.Log;

import com.fanhl.doujinMoe.api.common.DouJinMoeUrl;
import com.fanhl.doujinMoe.api.form.NewestForm;
import com.fanhl.doujinMoe.exception.GetDataFailException;
import com.fanhl.doujinMoe.model.Book;
import com.fanhl.doujinMoe.model.Page;
import com.fanhl.util.GsonUtil;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by fanhl on 15/11/8.
 */
public class HomeApi extends BaseApi {
    public static final String TAG = HomeApi.class.getSimpleName();

    public static NewestForm newest(int pageIndex) throws IOException, GetDataFailException {
        Log.d(TAG, "取得最新书籍.");
        String token = token();
        String result = Jsoup.connect(DouJinMoeUrl.NEWEST)
                .cookie(TOKEN_KEY, token)
                .data("get", String.valueOf(pageIndex))
                .timeout(TIME_OUT)
                .post().text();
        Log.d(TAG, "取得最新书箱的Json:" + result);
        NewestForm newestForm = GsonUtil.obj(result, NewestForm.class);

        if (newestForm == null || !newestForm.success || newestForm.newest == null) {
            throw new GetDataFailException("取得 最新书籍 的数据失败.");
        }

        return newestForm;
    }

    public static List<Book> best(int pageIndex, String sortType) throws IOException, GetDataFailException {
        Log.d(TAG, "取得最新书籍.");
        String token = token();
        Document document = Jsoup.connect(DouJinMoeUrl.BEST)
                .cookie(TOKEN_KEY, token)
                .data("page", String.valueOf(pageIndex))
                .data("sort", sortType)// FIXME: 15/11/10 之后再改
                .timeout(TIME_OUT)
                .post();

        if (document == null) throw new GetDataFailException("取不到排行榜的数据");

        List<Book> books = new ArrayList<>();

        Element  dircontent = document.getElementById("dircontent");
        Elements djms       = dircontent.select("djm");

        for (Element djm : djms) {
            Page page = new Page();
            page.preview = djm.attr("thumb");
            page.href = djm.attr("file");

            Log.d(TAG, page.toString());
        }

        return books;
    }
}
