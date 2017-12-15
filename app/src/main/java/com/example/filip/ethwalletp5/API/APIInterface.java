package com.example.filip.ethwalletp5.API;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;


public interface APIInterface {

    @Headers("Content-Type:application/json")
    @POST("/users/")
    Call<Models.Backup> createBackup(@Body Models.Backup backup);

    // TODO: implement restore backup endpoint
}

