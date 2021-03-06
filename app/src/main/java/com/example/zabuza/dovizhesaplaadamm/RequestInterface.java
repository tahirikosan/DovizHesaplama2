package com.example.zabuza.dovizhesaplaadamm;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface RequestInterface {

    @FormUrlEncoded
    @POST("register.php")
    Call<JsonResponse> create(@Field("name") String name, @Field("password") String password);

    @FormUrlEncoded
    @POST("login.php")
    Call<JsonResponse> login(@Field("name") String name, @Field("password") String password);

    @FormUrlEncoded
    @POST("update.php")
    Call<JsonResponse> update(@Field("name") String name, @Field("TL") float TL, @Field("EU") float EU, @Field("DL") float DL, @Field("ANG") float ANG);

}
