package com.example.android.popularmoviesstage1.favouritesData;

import android.provider.BaseColumns;

/**
 * Created by izzystannett on 06/03/2018.
 */

public class Contract {

    //inner class for the table of favourite movies
    public static final class favouritesEntry implements BaseColumns {
        //static strings for the table name and all columns
        public static final String TABLE_NAME = "favourites";
        public static final String COLUMN_MOVIE_TITLE = "title";
        public static final String COLUMN_MOVIE_IMAGE = "image";
        public static final String COLUMN_MOVIE_RELEASE_DATE = "releaseDate";
        public static final String COLUMN_MOVIE_RATING = "userRating";
        public static final String COLUMN_MOVIE_SYNOPSIS = "plotSynopsis";


    }
}
