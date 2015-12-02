package com.fanhl.doujinMoe.rest.service;

import com.fanhl.doujinMoe.rest.model.FolderResponse;

import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.POST;
import rx.Observable;

/**
 * 移动端 数据传输服务 (好像所有服务全走的这一个接口)
 * <p>
 * Created by fanhl on 15/12/1.
 */
public interface HomeService {
    @FormUrlEncoded
    @POST("/ajax/folder.php")
    Observable<FolderResponse> bookList(
            @Field("token") String token,
            @Field("offset") int offset,
            @Field("max") int max,
            @Field("sort") String sort,
            @Field("param") String param);
}
