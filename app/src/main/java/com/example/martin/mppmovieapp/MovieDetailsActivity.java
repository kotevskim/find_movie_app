package com.example.martin.mppmovieapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.martin.mppmovieapp.tasks.MovieGetTask;

public class MovieDetailsActivity extends AppCompatActivity {

    private MovieGetTask task;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);

        String movieId = null;
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            movieId = extras.getString("id");
        }

        final TextView tv_movie_title = (TextView) findViewById(R.id.tv_movie_title);
        TextView tv_movie_year = (TextView) findViewById(R.id.tv_movie_year);
        TextView tv_movie_full_plot = (TextView) findViewById(R.id.tv_movie_plot_full);
        ImageView iv_movie_poster = (ImageView)  findViewById(R.id.iv_movie_poster);

        Button btn = (Button) findViewById(R.id.btn_share);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                final String movieTitle = tv_movie_title.getText().toString();
                shareIntent.putExtra(Intent.EXTRA_TEXT, "Hey, this movie is good: " +  movieTitle);
                startActivity(Intent.createChooser(shareIntent, "Spodeli go filmot so..."));
            }
        });

        btn = (Button) findViewById(R.id.btn_back1);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        //        TODO implemet this
//        LayoutInflater inflater = (LayoutInflater)getApplicationContext().getSystemService
//                (this.LAYOUT_INFLATER_SERVICE);
//        View view = inflater.inflate(R.layout.activity_movie_details,null);

        task = new MovieGetTask(getApplicationContext(), movieId ,tv_movie_title, tv_movie_year, tv_movie_full_plot, iv_movie_poster);
        task.execute();
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
