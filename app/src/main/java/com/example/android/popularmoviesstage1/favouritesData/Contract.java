package com.example.android.popularmoviesstage1.favouritesData;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by izzystannett on 06/03/2018.
 */

public class Contract {

    public static final String AUTHORITY = "com.example.android.popularmoviesstage1";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);
    public static final String PATH_FAVOURITES = "favourites";

    //inner class for the table of favourite movies
    public static final class favouritesEntry implements BaseColumns {
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon()
                .appendPath(PATH_FAVOURITES).build();
        //static strings for the table name and all columns
        public static final String TABLE_NAME = "favourites";
        public static final String COLUMN_MOVIE_TITLE = "title";
        public static final String COLUMN_MOVIE_IMAGE = "image";
        public static final String COLUMN_MOVIE_RELEASE_DATE = "releaseDate";
        public static final String COLUMN_MOVIE_RATING = "userRating";
        public static final String COLUMN_MOVIE_SYNOPSIS = "plotSynopsis";


    }
}
