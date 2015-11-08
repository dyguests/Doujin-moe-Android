package com.fanhl.doujinMoe.api.form;

import com.fanhl.doujinMoe.model.Book;

import java.util.List;

/**
 * 取得数据的样子
 * {
 * "success": true,
 * "message": "",
 * "top": {
 * "token": "2zwqhjkv",
 * "name": "Goddesses of anonymity"
 * },
 * "artist": {
 * "token": "cp6gxvyg",
 * "name": "Kikurage-ya"
 * },
 * "newest": [
 * {
 * "token": "q6muejvz",
 * "name": "Best Position",
 * "count": "24",
 * "rating": "4.35",
 * "date": "Nov 6th 2015"
 * },
 * {
 * "token": "gyme9rx1",
 * "name": "Locker Girl Nanami-chan",
 * "count": "16",
 * "rating": "4.13",
 * "date": "Nov 6th 2015"
 * }
 * ]
 * }
 * <p>
 * Created by fanhl on 15/11/8.
 */
public class NewestForm {
    public boolean    success;
    public String     message;
    public TokenName  top;
    public TokenName  artist;
    public List<Book> newest;

    public static class TokenName {
        public String token;
        public String name;
    }
}
