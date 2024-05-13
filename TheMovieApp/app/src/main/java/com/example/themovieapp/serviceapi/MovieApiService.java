package com.example.themovieapp.serviceapi;

import com.example.themovieapp.model.Result;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface MovieApiService
{
    @GET("movie/popular")
    Call<Result> getPopularMovies(@Query("api_key") String apiKey);
}
