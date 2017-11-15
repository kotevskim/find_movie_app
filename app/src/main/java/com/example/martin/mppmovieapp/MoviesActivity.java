package com.example.martin.mppmovieapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.martin.mppmovieapp.adapters.MovieAdapter;
import com.example.martin.mppmovieapp.tasks.MovieSearchTask;

public class MoviesActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private MovieAdapter adapter;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movies);

        initRecyclerView();


        MovieSearchTask task = new MovieSearchTask(this.adapter);
        task.execute();
//        Intent intent = new Intent(this, MovieDetailsActivity.class);
//        this.startActivity(intent);
    }

    private void initRecyclerView() {
        mRecyclerView = (RecyclerView) findViewById(R.id.rv_movies);
        mRecyclerView.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(this);
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

                    }
                }));
    }
}
