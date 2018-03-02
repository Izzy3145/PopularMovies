package com.example.android.popularmoviesstage1;

import android.graphics.Movie;
import android.os.Parcel;
import android.os.Parcelable;

import java.net.URL;

/**
 * Created by izzystannett on 25/02/2018.
 */

public class MovieItem implements Parcelable{
    //should implement Parcelable interface? see BookListing

    //to include and set data to original title, movie poster, a plot synopsis(overview),
    // user rating (vote_average), release date

    private String mOriginalTitle;
    private String mImageUrl;
    private String mPlotSynopsis;
    private int mUserRating;
    private String mReleaseDate;
    private static final String BASE_IMAGE_URL = "http://image.tmdb.org/t/p/";
    private static final String IMAGE_SIZE_URL = "w185";

    public MovieItem(String originalTitle, String imageUrl, String plotSynopsis,
                     int userRating, String releaseDate) {
        mOriginalTitle = originalTitle;
        mImageUrl = imageUrl;
        mPlotSynopsis = plotSynopsis;
        mUserRating = userRating;
        mReleaseDate = releaseDate;
    }

    //set getter methods
    public String getmOriginalTitle() {
        return mOriginalTitle;
    }

    public String getmImageUrl() {
        String fullImageUrl = BASE_IMAGE_URL + IMAGE_SIZE_URL + mImageUrl;
        return fullImageUrl;
    }

    public String getmPlotSynopsis() {
        return mPlotSynopsis;
    }

    public int getmUserRating() {
        return mUserRating;
    }

    public String getmReleaseDate() {
        return mReleaseDate;
    }

    //override Parcelable methods
    protected MovieItem(Parcel in){
        mOriginalTitle = in.readString();
        mImageUrl = in.readString();
        mPlotSynopsis = in.readString();
        mUserRating = in.readInt();
        mReleaseDate = in.readString();
    }

    public static final Creator CREATOR = new Creator() {
        @Override
        public MovieItem createFromParcel(Parcel parcel) {
            return new MovieItem(parcel);
        }

        @Override
        public MovieItem[] newArray(int i) {
            return new MovieItem[i];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(mOriginalTitle);
        parcel.writeString(mImageUrl);
        parcel.writeString(mPlotSynopsis);
        parcel.writeInt(mUserRating);
        parcel.writeString(mReleaseDate);
    }
}
