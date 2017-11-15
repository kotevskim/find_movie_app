package com.example.martin.mppmovieapp.model;

/**
 * Created by martin on 11/13/17.
 */

public class Movie {

    private String Title;
    private String Year;
    private String Plot;
    private String Poster;
    private String imdbID;

    public String getName() {
        return Title;
    }

    public void setName(String name) {
        this.Title = name;
    }

    public String getYear() {
        return Year;
    }

    public void setYear(String year) {
        this.Year = year;
    }

    public String getPlot() {
        return Plot;
    }

    public String getPoster() {
        return Poster;
    }

    public String getImdbID() {
        return imdbID;
    }
}
