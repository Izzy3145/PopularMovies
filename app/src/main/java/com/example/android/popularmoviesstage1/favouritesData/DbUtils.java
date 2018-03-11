package com.example.android.popularmoviesstage1.favouritesData;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.widget.Toast;

import com.example.android.popularmoviesstage1.MovieItem;
import com.example.android.popularmoviesstage1.favouritesData.Contract;

/**
 * Created by izzystannett on 08/03/2018.
 */

public class DbUtils {

    private Context mContext;

    public DbUtils(Context context) {
        this.mContext = context;
    }

    //method to add an item to the database, assuming writable database has already been initialised
    public void addFavouriteMovie(MovieItem movieItem) {
        ContentValues cv = new ContentValues();
        cv.put(Contract.favouritesEntry.COLUMN_MOVIE_TITLE, movieItem.getmOriginalTitle());
        cv.put(Contract.favouritesEntry.COLUMN_MOVIE_IMAGE, movieItem.getmImageUrl());
        cv.put(Contract.favouritesEntry.COLUMN_MOVIE_RATING, movieItem.getmUserRating());
        cv.put(Contract.favouritesEntry.COLUMN_MOVIE_RELEASE_DATE, movieItem.getmReleaseDate());
        cv.put(Contract.favouritesEntry.COLUMN_MOVIE_SYNOPSIS, movieItem.getmPlotSynopsis());

        //add movieItem to the database via the content resolver
        Uri uri = mContext.getContentResolver().insert(Contract.favouritesEntry.CONTENT_URI, cv);

        if (uri != null) {
            Toast.makeText(mContext, uri.toString(), Toast.LENGTH_LONG).show();
        }
    }
}
