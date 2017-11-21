package com.example.martin.mppmovieapp.tasks;

import android.os.AsyncTask;

import com.example.martin.mppmovieapp.OmdbAPI;
import com.example.martin.mppmovieapp.adapters.MovieAdapter;
import com.example.martin.mppmovieapp.model.ApiSearchResult;
import com.example.martin.mppmovieapp.model.Movie;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by martin on 11/14/17.
 */

public class MovieSearchTask extends AsyncTask<Void, Void, List<Movie>> {

    private OmdbAPI service;
    private MovieAdapter adapter;
    private String searchQuery;

    public MovieSearchTask(MovieAdapter adapter, String searchQuery) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://www.omdbapi.com/?")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        this.service = retrofit.create(OmdbAPI.class);
        this.adapter = adapter;
        this.searchQuery = searchQuery;
    }


    @Override
    protected void onPreExecute() {
        // Common scenario: Start progress dialog
        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(List<Movie> movies) {
        super.onPostExecute(movies);
        adapter.setData(movies);
        adapter.notifyDataSetChanged();
    }

    @Override
    protected List<Movie> doInBackground(Void... voids) {
        Call<ApiSearchResult> call = service.searchMovieByName(searchQuery, "c24fc4e1");
        try {
            ApiSearchResult res = call.execute().body();
            return res.Search;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }
}
