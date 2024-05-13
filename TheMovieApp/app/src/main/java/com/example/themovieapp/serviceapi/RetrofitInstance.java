package com.example.themovieapp.serviceapi;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitInstance
{
    private static Retrofit retrofit = null;
    private static final String BASE_URL = "https://api.themoviedb.org/3/";

    public static MovieApiService getService()
    {
        if (retrofit == null)
            retrofit = new Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create()).build();
        return retrofit.create(MovieApiService.class);
    }

}
