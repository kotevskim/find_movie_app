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
 * @author martin
 */

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MyViewHolder> {

    public List<Movie> movieList;
    private Context context;

    public MovieAdapter(Context context) {
        this.context = context;
        movieList = new ArrayList<>();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.rv_movie_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Movie m = movieList.get(position);
        holder.tvMovieTitle.setText(m.getTitle());
        holder.tvMovieYear.setText(m.getYear());
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
        notifyDataSetChanged();
    }

    // View holder class
    class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView ivMoviePoster;
        TextView tvMovieTitle;
        TextView tvMovieYear;

        MyViewHolder(View itemView) {
            super(itemView);
            ivMoviePoster = itemView.findViewById(R.id.item_movie_poster);
            tvMovieTitle = itemView.findViewById(R.id.item_movie_title);
            tvMovieYear = itemView.findViewById(R.id.item_movie_year);
        }

    }

}
