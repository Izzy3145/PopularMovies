package com.example.android.popularmoviesstage1;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Movie;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.popularmoviesstage1.favouritesData.Contract;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by izzystannett on 24/02/2018.
 */

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.MyViewHolder> {

    private final ImageAdapterClickHandler mClickHandler;
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
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rowlayout, parent,
                false);
        //pass the view to the ViewHolder
        MyViewHolder viewHolder = new MyViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        //find the movie item in that position
        //TODO: this might be wrong
        if (mCursor == null) {
            final MovieItem currentMovieItem = mMovieItems.get(position);
            // bind data to the holder (and therefore items)
            Picasso.with(mContext).load(currentMovieItem.getmImageUrl()).into(holder.image);
        } else {
            //return if there is no data in the cursor
            if (!mCursor.moveToPosition(position)) {
                return;
            }
            String image = mCursor.getString(mCursor.getColumnIndex(Contract.favouritesEntry.COLUMN_MOVIE_IMAGE));
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

    //method to set data
    public void setMovieData(ArrayList<MovieItem> movieItems) {
        mMovieItems = movieItems;
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
        //create ViewHolder that links the image attribute of rowlayout.xml
        //and set a click listener to it
        private MyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        //define the onClick method to be assigned to the listener
        @Override
        public void onClick(View view) {
            int adapterPosition = getAdapterPosition();
            MovieItem movieItem = mMovieItems.get(adapterPosition);
            mClickHandler.onClickMethod(movieItem);
        }
    }
}