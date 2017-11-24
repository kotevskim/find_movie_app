package com.example.martin.mppmovieapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.martin.mppmovieapp.adapters.MovieAdapter;
import com.example.martin.mppmovieapp.tasks.MovieSearchTask;

public class MoviesActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private MovieAdapter adapter;
    private MovieSearchTask task;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movies);

        initRecyclerView();

        Button btn = (Button) findViewById(R.id.btn_search);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText et = (EditText) findViewById(R.id.et_input_movie);
                String searchQuery = et.getText().toString();
                task = new MovieSearchTask(adapter, searchQuery);
                task.execute();
            }
        });



//        Intent intent = new Intent(this, MovieDetailsActivity.class);
//        this.startActivity(intent);
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
                        adapter.movieList.remove(position);
                        adapter.notifyItemRemoved(position);
                        adapter.notifyItemRangeChanged(position, adapter.getItemCount());
                    }
                }));
    }

    @Override
    protected  void onDestroy() {
        cancelTask(); // we have to manage the lyfecycle of the AsycTask
        super.onDestroy();
    }

    private void cancelTask() {
        if (task != null) {
            task.cancel(true);
        }
    }
}
