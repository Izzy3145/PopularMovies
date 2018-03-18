package com.example.android.popularmoviesstage1;

import android.app.LoaderManager;
import android.content.AsyncTaskLoader;
import android.content.Context;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;

import com.example.android.popularmoviesstage1.favouritesData.Contract;
import com.example.android.popularmoviesstage1.movies.ImageAdapter;


/**
 * Created by izzystannett on 14/03/2018.
 */

public class CursorLoader implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final String LOG_TAG = CursorLoader.class.getSimpleName();
    private Context mContext;
    private ImageAdapter mAdapter;

    public CursorLoader(Context context, ImageAdapter adapter) {
        this.mContext = context;
        mAdapter = adapter;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, final Bundle bundle) {
        return new AsyncTaskLoader<Cursor>(mContext) {

            Cursor mFavouritesData = null;

            @Override
            protected void onStartLoading() {
                super.onStartLoading();
                forceLoad();
            }

            @Override
            public Cursor loadInBackground() {
                try {
                    return getContext().getContentResolver().query(Contract.favouritesEntry.CONTENT_URI,
                            null, null, null,
                            null);

                } catch (Exception e) {
                    Log.e(LOG_TAG, "Failed to asynchronously load data.");
                    e.printStackTrace();
                    return null;
                }
            }

            public void deliverResult(Cursor data) {
                mFavouritesData = data;
                super.deliverResult(mFavouritesData);
            }
        };
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mAdapter.setMovieCursorData(data);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
    }
}
