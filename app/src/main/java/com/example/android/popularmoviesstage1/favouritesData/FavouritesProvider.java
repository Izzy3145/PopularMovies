package com.example.android.popularmoviesstage1.favouritesData;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import static com.example.android.popularmoviesstage1.favouritesData.Contract.favouritesEntry.TABLE_NAME;

/**
 * Created by izzystannett on 10/03/2018.
 */

public class FavouritesProvider extends ContentProvider {

    private FavouritesDbHelper mFavouritesDbHelper;

    @Override
    public boolean onCreate() {
        Context context = getContext();
        mFavouritesDbHelper = new FavouritesDbHelper(context);
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection,
                        @Nullable String[] selectionArgs, @Nullable String sortOrder) {

        final SQLiteDatabase db = mFavouritesDbHelper.getReadableDatabase();

        Cursor retCursor = db.query(Contract.favouritesEntry.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder);

        retCursor.setNotificationUri(getContext().getContentResolver(), uri);

        return retCursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        final SQLiteDatabase db = mFavouritesDbHelper.getWritableDatabase();

        Uri returnUri; // URI to be returned

        // Inserting values into favourites table
        long id = db.insert(TABLE_NAME, null, contentValues);
        if (id > 0) {
            returnUri = ContentUris.withAppendedId(Contract.favouritesEntry.CONTENT_URI, id);
        } else {
            throw new android.database.SQLException("Failed to insert row into " + uri);
        }
        //notify content resolver of changes
        getContext().getContentResolver().notifyChange(uri, null);
        return returnUri;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String s, @Nullable String[] strings) {
        final SQLiteDatabase db = mFavouritesDbHelper.getWritableDatabase();
        int moviesDeleted;
        String id = uri.getPathSegments().get(1);
        moviesDeleted = db.delete(TABLE_NAME, "_id=?", new String[]{id});

        if (moviesDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return moviesDeleted;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String s, @Nullable String[] strings) {
        return 0;
    }
}
