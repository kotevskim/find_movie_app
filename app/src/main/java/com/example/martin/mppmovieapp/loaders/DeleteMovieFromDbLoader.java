package com.example.martin.mppmovieapp.loaders;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import com.example.martin.mppmovieapp.model.Movie;
import com.example.martin.mppmovieapp.persistence.AppDatabase;


/**
 * Created by martin on 11/25/17.
 */

public class DeleteMovieFromDbLoader extends AsyncTaskLoader<Void> {

    private Movie movie;

    public DeleteMovieFromDbLoader(Context context, Movie movie) {
        super(context);
        this.movie = movie;
    }

    @Override
    public Void loadInBackground() {
        AppDatabase db = AppDatabase.getAppDatabase(getContext());
        db.movieDao().delete(movie);
        return null;
    }
}
