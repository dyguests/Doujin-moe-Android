package com.fanhl.doujinMoe.api;

import android.util.Log;

import com.fanhl.doujinMoe.api.common.DouJinMoeUrl;
import com.fanhl.doujinMoe.api.form.NewestForm;
import com.fanhl.doujinMoe.exception.GetDataFailException;
import com.fanhl.util.GsonUtil;

import org.jsoup.Jsoup;

import java.io.IOException;

/**
 * Created by fanhl on 15/11/8.
 */
public class HomeApi extends BaseApi {
    public static final String TAG = HomeApi.class.getSimpleName();

    public static NewestForm newest(int pageIndex) throws IOException, GetDataFailException {
        Log.d(TAG, "取得最新书籍.");
        String token = "mm1qk0kqrf6hs6r82dbkli6fb4";// token();
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
}
