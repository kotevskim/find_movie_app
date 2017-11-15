package com.example.martin.mppmovieapp.tasks;

import android.content.Context;
import android.os.AsyncTask;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.martin.mppmovieapp.OmdbAPI;
import com.example.martin.mppmovieapp.R;
import com.example.martin.mppmovieapp.model.Movie;
import com.squareup.picasso.Picasso;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by martin on 11/15/17.
 */

public class MovieGetTask extends AsyncTask<Void, Void, Movie> {

    private OmdbAPI service;
    private TextView tv_movie_title;
    private TextView tv_movie_year;
    private TextView tv_movie_full_plot;
    private ImageView iv_movie_poster;
    private Context context;
    private String movieId;

    public MovieGetTask(Context context, String movieId, TextView tv_movie_title, TextView tv_movie_year, TextView tv_movie_plot_full, ImageView iv_movie_poster) {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://www.omdbapi.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        this.service = retrofit.create(OmdbAPI.class);
        this.tv_movie_title = tv_movie_title;
        this.tv_movie_year = tv_movie_year;
        this.tv_movie_full_plot = tv_movie_plot_full;
        this.iv_movie_poster = iv_movie_poster;
        this.context = context;
        this.movieId = movieId;
    }


    @Override
    protected void onPreExecute() {
        // Common scenario: Start progress dialog
        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(Movie movie) {
        super.onPostExecute(movie);
        this.tv_movie_title.setText(movie.getName());
        this.tv_movie_year.setText(movie.getYear());
        this.tv_movie_full_plot.setText(movie.getPlot());
        Picasso
                .with(context)
                .load(movie.getPoster())
                .placeholder(R.mipmap.ic_launcher)
                .into(this.iv_movie_poster);


    }

    @Override
    protected Movie doInBackground(Void... voids) {
        Call<Movie> call = service.getMovieById(movieId, "c24fc4e1", "full");
        try {
            Movie result = call.execute().body();
            return result;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}

