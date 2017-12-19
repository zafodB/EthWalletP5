package com.zafodB.ethwalletp5.API;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;


public interface APIInterface {

    @Headers("Content-Type:application/json")
    @POST("/users/")
    Call<Models.Backup> createBackup(@Body Models.Backup backup);

    @Headers("Content-Type:application/json")
    @POST("/users/restore")
    Call<Models.Backup> restoreWallet(@Body Models.Backup backup);
}

