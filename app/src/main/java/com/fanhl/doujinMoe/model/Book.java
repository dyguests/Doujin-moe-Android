package com.fanhl.doujinMoe.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by fanhl on 15/11/5.
 */
public class Book {

    //--------------------- Gson 解析取得-----------------------

    public String  token;
    @SerializedName("title")
    public String  name;
    @SerializedName("objectcount")
    public int     count;
    public boolean main;
    public String  rating;
    public String  date;

    //---------------------- 本地信息 ---------------------------

    public List<Page> pages;
    /*当前看到的页码*/
    public int        position;
    /*最新阅读日期*/
    public Date       recent;
    /*下载状态*/
    public Status     status;

    //--------------其它相关-----------------

    /*当 status=.DOWNLOADING 时记录下载到的页码*/
    public int downloadedPosition = -1;

    public Book() {
        pages = new ArrayList<>();
    }

    @Override
    public String toString() {
        return "Book(" + name + "," + token + ")";
    }

    public boolean isDownloading() {
        return status == Status.DOWNLOADING;
    }

    public boolean isDownloaded() {
        return status == Status.DOWNLOADED;
    }

    public boolean isWaitDownload() {
        return status == Status.WAIT_DOWNLOAD;
    }

    public enum Status {
        NONE, WAIT_DOWNLOAD, DOWNLOADING, DOWNLOADED
    }
}
