package com.example.martin.mppmovieapp;

import java.util.ArrayList;
import java.util.List;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.Loader;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.martin.mppmovieapp.listeners.RecyclerItemListener;
import com.example.martin.mppmovieapp.loaders.DeleteMovieFromDbLoader;
import com.example.martin.mppmovieapp.loaders.MoviesLoader;
import com.example.martin.mppmovieapp.model.Movie;
import com.example.martin.mppmovieapp.adapters.MovieAdapter;
import com.example.martin.mppmovieapp.services.FetchMoviesService;

import static com.example.martin.mppmovieapp.services.FetchMoviesService.DATA_LOADED;

public class MoviesActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private MovieAdapter adapter;
    private MoviesLoadedReceiver receiver;

    private static final int MOVIES_LOADER_ID = 1;
    private static final int DELETE_MOVIE_FROM_DB_LOADER_ID = 2;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movies);
        receiver = new MoviesLoadedReceiver();
        initRecyclerView();
        loadDataFromDatabase(); // async operation, must be done on a separate thread

        Button btn = (Button) findViewById(R.id.btn_search);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText et = (EditText) findViewById(R.id.et_input_movie);
                String searchQuery = et.getText().toString();
                Intent intent = new Intent(getApplicationContext(), FetchMoviesService.class);
                intent.putExtra("searchQuery", searchQuery);
                startService(intent);
            }
        });

    }

    private void initRecyclerView() {
        mRecyclerView = (RecyclerView) findViewById(R.id.rv_movies);
        mRecyclerView.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(getApplicationContext());
        mRecyclerView.setLayoutManager(llm);
        this.adapter = new MovieAdapter(getApplicationContext());
        mRecyclerView.setAdapter(adapter);
        mRecyclerView.addOnItemTouchListener(new RecyclerItemListener(
                getApplicationContext(),
                mRecyclerView,
                new RecyclerItemListener.RecyclerTouchListener() {

                    @Override
                    public void onClickItem(View v, int position) {
                        Movie m = adapter.movieList.get(position);
                        String movieId = m.getImdbID();
                        Intent intent = new Intent(getApplicationContext(), MovieDetailsActivity.class);
                        intent.putExtra("movie_id", movieId);
                        startActivity(intent);
                    }

                    @Override
                    public void onLongClickItem(View v, int position) {
                        Movie m = adapter.movieList.get(position);
                        adapter.movieList.remove(position);
                        adapter.notifyItemRemoved(position);
                        adapter.notifyItemRangeChanged(position, adapter.getItemCount());
                        deleteMovieFromDatabase(m);
                    }
                }));
    }

    private void loadDataFromDatabase() {
        this.getSupportLoaderManager().initLoader(MOVIES_LOADER_ID, null, moviesLoaderListener).forceLoad();
    }

    private void deleteMovieFromDatabase(Movie m) {
        Bundle bundle = new Bundle();
        bundle.putParcelable("movie", m);
        this.getSupportLoaderManager().initLoader(
                DELETE_MOVIE_FROM_DB_LOADER_ID,
                bundle,
                deleteMovieFromDbLoaderListener)
                .forceLoad();
    }

    @Override
    protected void onStart() {
        super.onStart();
        IntentFilter filter = new IntentFilter(DATA_LOADED);
        LocalBroadcastManager.getInstance(getApplicationContext())
                .registerReceiver(receiver, filter);
    }

    @Override
    protected void onStop() {
        super.onStop();
        LocalBroadcastManager.getInstance(getApplicationContext())
                .unregisterReceiver(receiver);
    }

    public class MoviesLoadedReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            List<Movie> moviesFromServer
                    = intent.getParcelableArrayListExtra("movies_from_server");
            adapter.setData(moviesFromServer);
            adapter.notifyDataSetChanged();
        }
    }

    private LoaderManager.LoaderCallbacks<List<Movie>> moviesLoaderListener
            = new LoaderManager.LoaderCallbacks<List<Movie>>() {
        @Override
        public Loader<List<Movie>> onCreateLoader(int id, Bundle args) {
            return new MoviesLoader(getApplicationContext());
        }

        @Override
        public void onLoadFinished(Loader<List<Movie>> loader, List<Movie> data) {
            adapter.setData(data);
        }

        @Override
        public void onLoaderReset(Loader loader) {
            adapter.setData(new ArrayList<Movie>());
        }
    };

    private LoaderManager.LoaderCallbacks<Void> deleteMovieFromDbLoaderListener =
            new LoaderManager.LoaderCallbacks<Void>() {
                @Override
                public Loader<Void> onCreateLoader(int id, Bundle args) {
                    Movie movie = args.getParcelable("movie");
                    return new DeleteMovieFromDbLoader(getApplicationContext(), movie);
                }

                @Override
                public void onLoadFinished(Loader<Void> loader, Void data) {
                    // TODO make propper logging
                    System.out.println("deleted");
                }

                @Override
                public void onLoaderReset(Loader<Void> loader) {

                }
            };

}
