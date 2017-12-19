package com.zafodB.ethwalletp5.API;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class APIClient {
    public static final String SERVER_URL = "https://eth-wallet-api.herokuapp.com";
    public static APIInterface myService;

    public static APIInterface getInstance() {
        if (myService == null) {
            Retrofit myRetrofit = new Retrofit.Builder()
                    .baseUrl(SERVER_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            myService = myRetrofit.create(APIInterface.class);
            return myService;
        }
        else{
            return myService;
        }
    }
}
