package com.fanhl.doujinMoe.api;

import android.util.Log;

import com.fanhl.doujinMoe.api.common.DouJinMoeUrl;

import org.jsoup.Jsoup;

import java.io.IOException;
import java.util.Date;
import java.util.Map;

/**
 * Created by fanhl on 15/11/8.
 */
public class BaseApi {
    public static final String TAG              = BaseApi.class.getSimpleName();
    public static final int    TIME_OUT         = 30 * 1000;
    public static final long   TOKEN_TIME_OUT   = 5 * 60 * 1000;
    public static final String TOKEN_KEY        = "PHPSESSID";
    public static       String token            = null;
    public static       Date   tokenRefreshDate = null;

    /**
     * 服务器端token是写死的?
     *
     * @return
     * @throws IOException
     */
    public static String token() throws IOException {
//        return "mm1qk0kqrf6hs6r82dbkli6fb4";
        if (token == null || isTokenTimeOut()) {
            Map<String, String> cookies = Jsoup.connect(DouJinMoeUrl.HOME).timeout(TIME_OUT).execute().cookies();
            Log.d(TAG, "Cookies:" + cookies);
            token = cookies.get(TOKEN_KEY);
        }
        tokenRefreshDate = new Date();
        return token;
    }

    private static boolean isTokenTimeOut() {
        return tokenRefreshDate == null || tokenRefreshDate.getTime() + TOKEN_TIME_OUT < new Date().getTime();
    }

    public static String header() {
        return null;
    }
}
