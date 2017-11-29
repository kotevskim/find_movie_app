package com.example.martin.mppmovieapp.services;

import android.app.IntentService;
import android.content.Intent;
import android.os.Parcelable;
import android.support.v4.content.LocalBroadcastManager;
import android.database.sqlite.SQLiteConstraintException;
import android.util.Log;

import com.example.martin.mppmovieapp.web.OmdbAPI;
import com.example.martin.mppmovieapp.R;
import com.example.martin.mppmovieapp.model.ApiSearchResult;
import com.example.martin.mppmovieapp.model.Movie;
import com.example.martin.mppmovieapp.persistence.AppDatabase;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class FetchMoviesService extends IntentService {

    public static final String DATA_LOADED = "com.example.martin.mppmovieapp.DATA_LOADED";

    public FetchMoviesService() {
        super("Fetch Movies Service");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            String baseUrl = getString(R.string.omdb_api_base_url);
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(baseUrl)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            OmdbAPI webApi = retrofit.create(OmdbAPI.class);
            String query = intent.getStringExtra("searchQuery");
            String apiKey = getString(R.string.omdb_api_key);
            Call<ApiSearchResult> call = webApi.searchMovieByName(query, apiKey);
            try {
                ApiSearchResult apiRes = call.execute().body();
                List<Movie> movies = (apiRes != null) ?
                        apiRes.Search : Collections.<Movie>emptyList();
                notifyDataFetched(movies); // send broadcast
                storeInDatabase(movies);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void notifyDataFetched(List<Movie> movies) {
        Intent i = new Intent(DATA_LOADED);
        i.putParcelableArrayListExtra("movies_from_server", new ArrayList<Parcelable>(movies));
        LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(i);
    }

    private void storeInDatabase(List<Movie> movies) {
        AppDatabase db = AppDatabase.getAppDatabase(getApplicationContext());
        for (Movie m : movies) {
            try {
                db.movieDao().insert(m);
            } catch (SQLiteConstraintException e) {
                Log.i("DB_OP", m.getTitle() + " already exist in the Database");
            }
        }

    }

}
