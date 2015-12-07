package com.fanhl.doujinMoe.rest.service;

import com.fanhl.doujinMoe.rest.model.FolderResponse;
import com.squareup.okhttp.Response;

import retrofit.Call;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Path;
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

    // FIXME: 15/12/7 未完成
    //    @Multipart
    @GET("{url}")
    Call<Response> downloadPage(@Path("url") String pagePath);
}
