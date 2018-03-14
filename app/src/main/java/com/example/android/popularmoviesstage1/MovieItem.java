package com.example.android.popularmoviesstage1;

import android.graphics.Movie;
import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

import java.net.URL;

/**
 * Created by izzystannett on 25/02/2018.
 */

public class MovieItem implements Parcelable {

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
    private static final String BASE_IMAGE_URL = "http://image.tmdb.org/t/p/";
    private static final String IMAGE_SIZE_URL = "w185";
    private String mOriginalTitle;
    private String mImageUrl;
    private String mPlotSynopsis;
    private int mUserRating;
    private String mReleaseDate;
    private int mTag;
    private String mUriString;


    public MovieItem(String originalTitle, String imageUrl, String plotSynopsis,
                     int userRating, String releaseDate) {
        mOriginalTitle = originalTitle;
        mImageUrl = imageUrl;
        mPlotSynopsis = plotSynopsis;
        mUserRating = userRating;
        mReleaseDate = releaseDate;
    }

    public MovieItem(String originalTitle, String imageUrl, String plotSynopsis,
                     int userRating, String releaseDate, Uri uri) {
        mOriginalTitle = originalTitle;
        mImageUrl = imageUrl;
        mPlotSynopsis = plotSynopsis;
        mUserRating = userRating;
        mReleaseDate = releaseDate;
        mUriString = uri.toString();
    }

    //override Parcelable methods
    protected MovieItem(Parcel in) {
        mOriginalTitle = in.readString();
        mImageUrl = in.readString();
        mPlotSynopsis = in.readString();
        mUserRating = in.readInt();
        mReleaseDate = in.readString();
        mUriString = in.readString();
        mTag = in.readInt();
    }

    //set getter methods
    public String getmOriginalTitle() {
        return mOriginalTitle;
    }

    public String getmImageUrl() {
        return BASE_IMAGE_URL + IMAGE_SIZE_URL + mImageUrl;
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

    public String getmUriString() {
        return mUriString;
    }

    public int getTag() {
        return mTag;
    }

    public void setTag(final int tag) {
        mTag = tag;
    }

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
        parcel.writeString(mUriString);
        parcel.writeInt(mTag);
    }
}
