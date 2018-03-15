package com.example.android.popularmoviesstage1;

import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.android.popularmoviesstage1.favouritesData.Contract;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by izzystannett on 24/02/2018.
 */

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.MyViewHolder> {

    public static final int CURSOR_TAG = 50;
    public static final int ARRAY_TAG = 55;
    private static final String BASE_IMAGE_URL = "http://image.tmdb.org/t/p/";
    private static final String IMAGE_SIZE_URL = "w185";
    private final ImageAdapterClickHandler mClickHandler;
    private String LOG_TAG = ImageAdapter.class.getSimpleName();
    private Context mContext;
    private ArrayList<MovieItem> mMovieItems;
    private Cursor mCursor;

    //first constructor to take in an array of movie items
    public ImageAdapter(Context c, ArrayList<MovieItem> movieItems, ImageAdapterClickHandler
            imageAdapterClickHandler) {
        mContext = c;
        mMovieItems = movieItems;
        mClickHandler = imageAdapterClickHandler;
    }

    //second constructor to take in Cursor instead of array list for Favourites table querying
    public ImageAdapter(Context c, Cursor cursor, ImageAdapterClickHandler
            imageAdapterClickHandler) {
        mContext = c;
        mCursor = cursor;
        mClickHandler = imageAdapterClickHandler;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // inflate the item Layout
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.grid_layout, parent,
                false);
        //pass the view to the ViewHolder
        MyViewHolder viewHolder = new MyViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        //if cursor is empty, return movie images from the ArrayList
        if (mCursor == null) {
            //find the movie item in that position
            final MovieItem currentMovieItem = mMovieItems.get(position);
            // bind data to the holder (and therefore items)
            Picasso.with(mContext).load(currentMovieItem.getmImageUrl()).into(holder.image);
        } else {
            //if a Cursor is present, return movie images from the favourites db
            //return if there is no data in the cursor
            if (!mCursor.moveToPosition(position)) {
                return;
            }
            String image = mCursor.getString(
                    mCursor.getColumnIndex(Contract.favouritesEntry.COLUMN_MOVIE_IMAGE));
            Picasso.with(mContext).load(image).into(holder.image);
        }
    }

    @Override
    public int getItemCount() {
        if (mCursor != null) {
            return mCursor.getCount();
        } else if (mMovieItems != null) {
            return mMovieItems.size();
        } else {
            return 0;
        }
    }

    //method to set data from array
    public void setMovieArrayData(ArrayList<MovieItem> movieItems) {
        mMovieItems = movieItems;
        notifyDataSetChanged();
    }

    public void setMovieCursorData(Cursor movieItems) {
        mCursor = movieItems;
        notifyDataSetChanged();
    }

    //create interface for clickHandler
    public interface ImageAdapterClickHandler {
        void onClickMethod(MovieItem movieItem);
    }

    //define the ViewHolder that implements the click handler interface
    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.image)
        ImageView image;

        //create ViewHolder that links the image attribute of grid_layout.xml
        //and set a click listener to it
        private MyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        //define the onClick method to be assigned to the listener
        @Override
        public void onClick(View view) {
            MovieItem movieItem;
            int adapterPosition = getAdapterPosition();

            //if cursor is present, form a movieItem from it
            if (mCursor != null) {
                mCursor.moveToPosition(adapterPosition);
                String originalTitle = mCursor.getString(
                        mCursor.getColumnIndexOrThrow(Contract.favouritesEntry.COLUMN_MOVIE_TITLE));
                String imageUrl = mCursor.getString(
                        mCursor.getColumnIndexOrThrow(Contract.favouritesEntry.COLUMN_MOVIE_IMAGE));
                String releaseDate = mCursor.getString(
                        mCursor.getColumnIndexOrThrow(Contract.favouritesEntry.COLUMN_MOVIE_RELEASE_DATE));
                int userRating = mCursor.getInt(
                        mCursor.getColumnIndexOrThrow(Contract.favouritesEntry.COLUMN_MOVIE_RATING));
                String plotSynopsis = mCursor.getString(
                        mCursor.getColumnIndexOrThrow(Contract.favouritesEntry.COLUMN_MOVIE_SYNOPSIS));
                int id = mCursor.getInt(
                        mCursor.getColumnIndexOrThrow(Contract.favouritesEntry.COLUMN_MOVIE_ID_FROM_JSON));
                Uri uri = ContentUris.withAppendedId(Contract.favouritesEntry.CONTENT_URI, mCursor.getInt(
                        mCursor.getColumnIndex(Contract.favouritesEntry._ID)));
                movieItem = new MovieItem(originalTitle, imageUrl, plotSynopsis, userRating, releaseDate,
                        id, uri);
                //set appropriate tag to movieItem for use in DetailActivity
                movieItem.setTag(CURSOR_TAG);
            } else {
                //else select the appropriate movie from the array using getAdapterPosition
                movieItem = mMovieItems.get(adapterPosition);
                movieItem.setTag(ARRAY_TAG);
            }
            //pass movieItem into the recyclerView's onClickMethod
            mClickHandler.onClickMethod(movieItem);
        }
    }
}