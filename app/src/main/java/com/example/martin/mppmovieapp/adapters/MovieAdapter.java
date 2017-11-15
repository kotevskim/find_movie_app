package com.example.martin.mppmovieapp.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import com.example.martin.mppmovieapp.R;
import com.example.martin.mppmovieapp.model.Movie;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by martin on 11/13/17.
 */

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MyViewHolder> {

    private List<Movie> movieList;
    private Context context;

    public MovieAdapter(Context context) {
        this.context = context;
        movieList = new ArrayList<>();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View veiw = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.rv_movie_item, parent, false);
        return new MyViewHolder(veiw);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Movie m = movieList.get(position);
        holder.tvMovieTitle.setText(m.getName());
        holder.tvMovieYear.setText(m.getYear());
        holder.tvMovieId.setText(m.getImdbID());
        Picasso
                .with(context)
                .load(m.getPoster())
                .placeholder(R.mipmap.ic_launcher)
                .into(holder.ivMoviePoster);
    }

    @Override
    public int getItemCount() {
        return movieList.size();
    }

    public void setData(List<Movie> data) {
        this.movieList = data;
    }

    // View holder class
    public class MyViewHolder extends RecyclerView.ViewHolder {

        public ImageView ivMoviePoster;
        public TextView tvMovieTitle;
        public TextView tvMovieYear;
        public TextView tvMovieId;

        public MyViewHolder(View itemView) {
            super(itemView);
            ivMoviePoster = (ImageView) itemView.findViewById(R.id.item_movie_poster);
            tvMovieTitle = (TextView) itemView.findViewById(R.id.item_movie_title);
            tvMovieYear = (TextView) itemView.findViewById(R.id.item_movie_year);
            tvMovieId = (TextView) itemView.findViewById(R.id.item_movie_id);
        }
    }

}
