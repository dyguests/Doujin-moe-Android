package com.fanhl.doujinMoe.api;

import android.util.Log;

import com.fanhl.doujinMoe.api.common.DouJinMoeUrl;
import com.fanhl.doujinMoe.api.form.NewestForm;
import com.fanhl.doujinMoe.exception.GetDataFailException;
import com.fanhl.doujinMoe.model.Book;
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
        Log.d(TAG, "取得排行榜书籍.");
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
        Elements elements   = dircontent.select("a[href]");

        //<a href="/c6zjrm5v">
        //    <div class="holder">
        //        <div class="thumb">
        //            <img src="http://static.doujin-moe.us/f-c6zjrm5v.jpg"/>
        //            <div class="title">
        //                <div class="text">Koufukuron</div>
        //                <div class="info">
        //                    <div class="thumbinfo">
        //                        <div class="label">IMAGES</div>
        //                        <div class="value">35</div>
        //                    </div>
        //                    <div class="thumbinfo">
        //                        <div class="label">RATING</div>
        //                        <div class="value">4.70</div>
        //                    </div>
        //                    <div class="thumbinfo date">
        //                        <div class="label">DATE</div>
        //                        <div class="value">Oct 19th 2015</div>
        //                    </div>
        //                </div>
        //            </div>
        //        </div>
        //    </div>
        //</a>
        for (Element element : elements) {
            Elements holder1 = element.getElementsByClass("holder");
            if (holder1 == null || holder1.isEmpty()) continue;
            Element holder = holder1.get(0);
            if (holder == null) continue;

            Book book = new Book();

            book.token = getBookToken(element);

            Element titleEle = element.getElementsByClass("title").get(0);
            book.name = titleEle.getElementsByClass("text").text();

            Element info = titleEle.getElementsByClass("info").get(0);

            Elements infos = info.select("div[class^=thumbinfo]");

            book.count = Integer.valueOf(infos.get(0).getElementsByClass("value").text());
            book.rating = infos.get(1).getElementsByClass("value").text();
            book.date = infos.get(2).getElementsByClass("value").text();

            Log.d(TAG, book.toString());
            books.add(book);
        }

        return books;
    }

    private static String getBookToken(Element element) {
        String href = element.attr("href");
        if (href.startsWith("/")) return href.substring(1);
        return href;
    }
}
