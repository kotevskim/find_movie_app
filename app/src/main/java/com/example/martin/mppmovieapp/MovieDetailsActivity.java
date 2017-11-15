package com.example.martin.mppmovieapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.martin.mppmovieapp.tasks.MovieGetTask;

public class MovieDetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);

        String movieId = null;
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            movieId = extras.getString("id");
        }

        TextView tv_movie_title = (TextView) findViewById(R.id.tv_movie_title);
        TextView tv_movie_year = (TextView) findViewById(R.id.tv_movie_year);
        TextView tv_movie_full_plot = (TextView) findViewById(R.id.tv_movie_plot_full);
        ImageView iv_movie_poster = (ImageView)  findViewById(R.id.iv_movie_poster);

        //        TODO implemet this
//        LayoutInflater inflater = (LayoutInflater)getApplicationContext().getSystemService
//                (this.LAYOUT_INFLATER_SERVICE);
//        View view = inflater.inflate(R.layout.activity_movie_details,null);

        MovieGetTask task = new MovieGetTask(this, movieId ,tv_movie_title, tv_movie_year, tv_movie_full_plot, iv_movie_poster);
        task.execute();
    }
}
