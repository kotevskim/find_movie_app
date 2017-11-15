package com.example.martin.mppmovieapp;

import com.example.martin.mppmovieapp.model.ApiSearchResult;
import com.example.martin.mppmovieapp.model.Movie;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by martin on 11/13/17.
 */

public interface OmdbAPI {

    @GET("/")
    Call<ApiSearchResult> searchMovieByName(@Query("s") String name,
                                            @Query("apikey") String apiKey);

    @GET("/")
    Call<Movie> getMovieById(@Query("i") String id,
                             @Query("apikey") String apiKey,
                             @Query("plot") String plotType);

}
