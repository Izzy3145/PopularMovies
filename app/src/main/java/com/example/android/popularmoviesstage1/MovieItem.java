package com.example.android.popularmoviesstage1;

import java.net.URL;

/**
 * Created by izzystannett on 25/02/2018.
 */

public class MovieItem {
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
}
