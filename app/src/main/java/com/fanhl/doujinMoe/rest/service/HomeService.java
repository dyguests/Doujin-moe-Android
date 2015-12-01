package com.fanhl.doujinMoe.rest.service;

import com.fanhl.doujinMoe.rest.model.FolderResponse;

import retrofit.Call;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.Headers;
import retrofit.http.POST;

/**
 * Created by fanhl on 15/12/1.
 */
public interface HomeService {
    @FormUrlEncoded
    @POST("/ajax/folder.php")
    @Headers("Content-Type: application/x-www-form-urlencoded; charset=UTF-8")
    Call<FolderResponse> bookList(
            @Field("token") String token,
            @Field("offset") int offset,
            @Field("max") int max,
            @Field("sort") String sort,
            @Field("param") String param);
}
