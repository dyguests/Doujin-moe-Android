package com.fanhl.doujinMoe.rest.service;

import com.fanhl.doujinMoe.rest.model.BookListResponse;

import retrofit.Call;
import retrofit.http.POST;

/**
 * Created by fanhl on 15/12/1.
 */
public interface HomeService {
    @POST("/ajax/folder.php")
    Call<BookListResponse> bookList();
}
