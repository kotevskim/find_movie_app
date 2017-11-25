package com.example.martin.mppmovieapp.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

/**
 * Created by martin on 11/13/17.
 */
@Entity
public class Movie implements Parcelable {

    @ColumnInfo(name = "title")
    private String Title;
    @ColumnInfo(name = "year")
    private String Year;
    @ColumnInfo(name = "plot")
    private String Plot;
    @ColumnInfo(name = "poster_url")
    private String Poster;
    @PrimaryKey
    @NonNull
    private String imdbID;

    public Movie() {}

    // For Parcelling
    public Movie(Parcel in) {
        String[] data = new String[5];
        in.readStringArray(data);
        // The order needs to be the same as in writeToParcel method
        this.setTitle(data[0]);
        this.setYear(data[1]);
        this.setPlot(data[2]);
        this.setPoster(data[3]);
        this.setImdbID(data[4]);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeStringArray(new String[]{
                this.getTitle(), this.getYear(), this.getPlot(), this.getPoster(), this.getImdbID()
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

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getYear() {
        return Year;
    }

    public void setYear(String year) {
        Year = year;
    }

    public String getPlot() {
        return Plot;
    }

    public void setPlot(String plot) {
        Plot = plot;
    }

    public String getPoster() {
        return Poster;
    }

    public void setPoster(String poster) {
        Poster = poster;
    }

    public String getImdbID() {
        return imdbID;
    }

    public void setImdbID(String imdbID) {
        this.imdbID = imdbID;
    }
}
