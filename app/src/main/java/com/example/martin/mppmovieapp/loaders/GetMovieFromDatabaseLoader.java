package com.example.martin.mppmovieapp.loaders;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import com.example.martin.mppmovieapp.persistence.AppDatabase;

/**
 * Created by martin on 11/25/17.
 */

public class GetMovieFromDatabaseLoader extends AsyncTaskLoader {

    private String movieId;

    public GetMovieFromDatabaseLoader(Context context, String movieId) {
        super(context);
        this.movieId = movieId;
    }

    @Override
    public Object loadInBackground() {
        AppDatabase db = AppDatabase.getAppDatabase(getContext());
        return db.movieDao().findById(movieId);
    }
}
