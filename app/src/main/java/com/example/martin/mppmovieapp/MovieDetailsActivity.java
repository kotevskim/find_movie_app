package com.example.martin.mppmovieapp;

import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.content.Loader;
import android.support.v4.app.LoaderManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.martin.mppmovieapp.loaders.GetMovieFromDatabaseLoader;
import com.example.martin.mppmovieapp.loaders.GetMovieFromServerLoader;
import com.example.martin.mppmovieapp.model.Movie;
import com.squareup.picasso.Picasso;

public class MovieDetailsActivity extends AppCompatActivity {

    private static final int GET_MOVIE_FROM_SERVER_LOADER_ID = 1;
    private static final int GET_MOVIE_FROM_DATABASE_LOADER_ID = 2;
    TextView tv_movie_title;
    TextView tv_movie_year;
    TextView tv_movie_plot;
    ImageView iv_movie_poster;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);
        assignViews();
        assignButtonListeners();

        String movieId = "";
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            movieId = extras.getString("movie_id");
            if (isNetworkAvailable())
                getFromServer(movieId); // async operation, must be done ona separate thread
            else
                getFromDatabase(movieId); // async operation, must be done ona separate thread
        }
    }

    private void assignViews() {
        tv_movie_title = (TextView) findViewById(R.id.tv_movie_title);
        tv_movie_year = (TextView) findViewById(R.id.tv_movie_year);
        tv_movie_plot = (TextView) findViewById(R.id.tv_movie_plot_full);
        iv_movie_poster = (ImageView) findViewById(R.id.iv_movie_poster);
    }

    private void assignButtonListeners() {
        Button btn = (Button) findViewById(R.id.btn_share);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                final String movieTitle = tv_movie_title.getText().toString();
                String shareMsg = getString(R.string.share_movie_message) + " " + movieTitle;
                shareIntent.putExtra(Intent.EXTRA_TEXT, shareMsg);
                String chooserTitle = getString(R.string.share_movie_chooser_title);
                startActivity(Intent.createChooser(shareIntent, chooserTitle));
            }
        });

        btn = (Button) findViewById(R.id.btn_back1);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private LoaderManager.LoaderCallbacks<Movie> getMovieFromServerLoaderListener
            = new LoaderManager.LoaderCallbacks<Movie>() {
        @Override
        public Loader<Movie> onCreateLoader(int id, Bundle args) {
            String movieId = args.getString("movie_id");
            return new GetMovieFromServerLoader(getApplicationContext(), movieId);
        }

        @Override
        public void onLoadFinished(Loader<Movie> loader, Movie movie) {
            fillView(movie);
        }

        @Override
        public void onLoaderReset(Loader<Movie> loader) {

        }
    };

    private LoaderManager.LoaderCallbacks<Movie> getMovieFromDatabaseLoaderListener
            = new LoaderManager.LoaderCallbacks<Movie>() {
        @Override
        public Loader<Movie> onCreateLoader(int id, Bundle args) {
            String movieId = args.getString("movie_id");
            return new GetMovieFromDatabaseLoader(getApplicationContext(), movieId);
        }

        @Override
        public void onLoadFinished(Loader<Movie> loader, Movie movie) {
            movie.setPlot(getString(R.string.plot_not_available));
            fillView(movie);
        }

        @Override
        public void onLoaderReset(Loader<Movie> loader) {

        }
    };

    private void getFromServer(String movieId) {
        Bundle bundle = new Bundle();
        bundle.putString("movie_id", movieId);
        this.getSupportLoaderManager().initLoader(
                GET_MOVIE_FROM_SERVER_LOADER_ID,
                bundle,
                getMovieFromServerLoaderListener).
                forceLoad();
    }

    private void getFromDatabase(String movieId) {
        Bundle bundle = new Bundle();
        bundle.putString("movie_id", movieId);
        this.getSupportLoaderManager().initLoader(
                GET_MOVIE_FROM_DATABASE_LOADER_ID,
                bundle,
                getMovieFromDatabaseLoaderListener).
                forceLoad();
    }

    private void fillView(Movie movie) {
        tv_movie_title.setText(movie.getTitle());
        tv_movie_year.setText(movie.getYear());
        tv_movie_plot.setText(movie.getPlot());
        Picasso
                .with(getApplicationContext())
                .load(movie.getPoster())
                .placeholder(R.mipmap.ic_launcher)
                .into(iv_movie_poster);
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(getApplicationContext().CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
