package com.doniapriano.myapplication.API;

import com.doniapriano.myapplication.Model.ResponseModel;

import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;

public interface APIRequestData {

    @GET("retrive.php")
    Call<ResponseModel> apiRetriveData();

    @FormUrlEncoded
    @POST("create.php")
    Call<ResponseModel>  apiCreateData(
            @Field("nama") String nama,
            @Field("alamat") String alamat,
            @Field("telephone") String telephone
    );

    @FormUrlEncoded
    @POST("delete.php")
    Call<ResponseModel> apiDeleteData(
            @Field("id") int id
    );

    @FormUrlEncoded
    @POST("get.php")
    Call<ResponseModel> apiGetData(
            @Field("id") int id
    );

    @FormUrlEncoded
    @POST("update.php")
    Call<ResponseModel>  apiUpdateData(
            @Field("id") int id,
            @Field("nama") String nama,
            @Field("alamat") String alamat,
            @Field("telephone") String telephone
    );

}
