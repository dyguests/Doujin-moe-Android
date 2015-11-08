package com.fanhl.doujinMoe.ui;

import com.fanhl.doujinMoe.exception.GetDataFailException;
import com.fanhl.doujinMoe.model.Book;
import com.fanhl.doujinMoe.model.Page;

import java.util.List;

/**
 * Created by fanhl on 15/11/8.
 */
public class PageApi {
    public static List<Page> pages(Book book) throws GetDataFailException {
        throw new GetDataFailException("取得书箱详细信息失败.");
    }
}
