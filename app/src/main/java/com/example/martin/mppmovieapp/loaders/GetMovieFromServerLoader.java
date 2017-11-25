package com.example.martin.mppmovieapp.loaders;


import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import com.example.martin.mppmovieapp.web.OmdbAPI;
import com.example.martin.mppmovieapp.R;
import com.example.martin.mppmovieapp.model.Movie;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by martin on 11/25/17.
 */

public class GetMovieFromServerLoader extends AsyncTaskLoader<Movie> {

    private String movieId;

    public GetMovieFromServerLoader(Context context, String movieId) {
        super(context);
        this.movieId = movieId;
    }

    @Override
    public Movie loadInBackground() {
        String apiBaseUrl = getContext().getString(R.string.omdb_api_base_url);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(apiBaseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        OmdbAPI webApi = retrofit.create(OmdbAPI.class);
        String apiKey = getContext().getString(R.string.omdb_api_key);
        Call<Movie> call = webApi.getMovieById(movieId, apiKey, "full");
        try {
            Movie result = call.execute().body();
            return result;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new Movie();
    }
}
