package com.example.martin.mppmovieapp.loaders;


import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import com.example.martin.mppmovieapp.model.Movie;
import com.example.martin.mppmovieapp.persistence.AppDatabase;

import java.util.List;

/**
 * @author martin
 */

public class MoviesLoader extends AsyncTaskLoader<List<Movie>> {


    public MoviesLoader(Context context) {
        super(context);
    }

    @Override
    public List<Movie> loadInBackground() {
        AppDatabase db = AppDatabase.getAppDatabase(getContext());
        return db.movieDao().getAll();
    }
}
