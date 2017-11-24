package com.example.martin.mppmovieapp.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by martin on 11/13/17.
 */
public class Movie implements Parcelable {

    private String Title;
    private String Year;
    private String Plot;
    private String Poster;
    private String imdbID;

    // For Parcelling
    public Movie(Parcel in) {
        String[] data = new String[5];
        in.readStringArray(data);
        // The order needs to be the same as in writeToParcel method
        this.Title = data[0];
        this.Year = data[1];
        this.Plot = data[2];
        this.Poster = data[3];
        this.imdbID = data[4];
    }

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeStringArray(new String[] {
                this.Title, this.Year, this.Plot, this.Poster, this.imdbID
        });
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {

        @Override
        public Object createFromParcel(Parcel source) {
            return new Movie(source);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };
}
