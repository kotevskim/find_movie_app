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
import android.widget.TextView;

import com.example.martin.mppmovieapp.loaders.MoviesLoader;
import com.example.martin.mppmovieapp.model.Movie;
import com.example.martin.mppmovieapp.adapters.MovieAdapter;
import com.example.martin.mppmovieapp.persistence.AppDatabase;
import com.example.martin.mppmovieapp.services.FetchMoviesService;

import static com.example.martin.mppmovieapp.services.FetchMoviesService.DATA_LOADED;

public class MoviesActivity extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<List<Movie>> {

    private RecyclerView mRecyclerView;
    private MovieAdapter adapter;
    private MoviesLoadedReceiver receiver;

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
        mRecyclerView.addOnItemTouchListener(new RecyclerItemListener(getApplicationContext(), mRecyclerView,
                new RecyclerItemListener.RecyclerTouchListener() {

                    @Override
                    public void onClickItem(View v, int position) {
                        TextView tv = (TextView) v.findViewById(R.id.item_movie_id);
                        String movieId = tv.getText().toString();
                        Intent intent = new Intent(getApplicationContext(), MovieDetailsActivity.class);
                        intent.putExtra("id", movieId);
                        startActivity(intent);
                    }

                    @Override
                    public void onLongClickItem(View v, int position) {
                        Movie m = adapter.movieList.get(position);
                        adapter.movieList.remove(position);
                        adapter.notifyItemRemoved(position);
                        adapter.notifyItemRangeChanged(position, adapter.getItemCount());
                        // TODO should be done on a separate thread
//                        AppDatabase db = AppDatabase.getAppDatabase(getApplicationContext());
//                        db.movieDao().delete(m);
                    }
                }));
    }

    private void loadDataFromDatabase() {
        this.getSupportLoaderManager().initLoader(0, null, this).forceLoad();
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
            List<Movie> moviesFromServer = intent.getParcelableArrayListExtra("movies_from_server");
            adapter.setData(moviesFromServer);
            adapter.notifyDataSetChanged();
        }
    }

    // Methods from LoaderManager.LoaderCallbacks interface
    @Override
    public Loader<List<Movie>> onCreateLoader(int id, Bundle args) {
        return new MoviesLoader(getApplicationContext());
    }

    @Override
    public void onLoadFinished(Loader<List<Movie>> loader, List<Movie> data) {
        this.adapter.setData(data);
    }

    @Override
    public void onLoaderReset(Loader<List<Movie>> loader) {
        this.adapter.setData(new ArrayList<Movie>());
    }

}
