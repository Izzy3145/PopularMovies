package com.example.android.popularmoviesstage1.favouritesData;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.android.popularmoviesstage1.favouritesData.Contract.favouritesEntry;

/**
 * Created by izzystannett on 06/03/2018.
 */

public class FavouritesDbHelper extends SQLiteOpenHelper {
    //create database name and version
    private static final String DATABASE_NAME = "favourites.db";
    private static final int DATABASE_VERSION = 1;

    //create constructor
    public FavouritesDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        final String SQL_CREATE_FAVOURITES_TABLE = "CREATE TABLE " +
                favouritesEntry.TABLE_NAME + " (" +
                favouritesEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                favouritesEntry.COLUMN_MOVIE_TITLE + " TEXT NOT NULL, " +
                favouritesEntry.COLUMN_MOVIE_IMAGE + " TEXT NOT NULL, " +
                favouritesEntry.COLUMN_MOVIE_RELEASE_DATE + " TEXT NOT NULL, " +
                favouritesEntry.COLUMN_MOVIE_RATING + " INTEGER NOT NULL, " +
                favouritesEntry.COLUMN_MOVIE_SYNOPSIS + " TEXT NOT NULL" + ");";

        sqLiteDatabase.execSQL(SQL_CREATE_FAVOURITES_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + favouritesEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}
