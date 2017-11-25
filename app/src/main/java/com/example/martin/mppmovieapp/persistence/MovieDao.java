package com.example.martin.mppmovieapp.persistence;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.example.martin.mppmovieapp.model.Movie;

import java.util.List;

/**
 * Created by martin on 11/25/17.
 */

@Dao
public interface MovieDao {

    @Query("SELECT * FROM movie ORDER BY title")
    List<Movie> getAll();

    @Query("SELECT * FROM movie WHERE title LIKE :title")
    List<Movie> findByTitle(String title);

    @Query("SELECT * FROM movie WHERE imdbID LIKE :id LIMIT 1")
    Movie findById(String id);

    @Insert
    void insert(Movie movie);

    @Insert
    void insertAll(Movie... movies);

    @Update
    void update(Movie movie);

    @Delete
    void delete(Movie movie);
}


