package com.fanhl.doujinMoe.api.common;

/**
 * Created by fanhl on 15/11/8.
 */
public class DouJinMoeUrl {
    public static final String HOME   = "http://www.doujinmoe.us/";
    public static final String NEWEST = HOME + "ajax/newest.php";

    public static String previewUrl(String token) {
        return "http://static.doujin-moe.us/f-" + token + ".jpg";
    }

    public static String detailUrl(String token) {
        return HOME + token;
    }
}
