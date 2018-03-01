package com.example.android.popularmoviesstage1;

import android.content.Context;
import android.content.Intent;
import android.graphics.Movie;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by izzystannett on 24/02/2018.
 */

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.MyViewHolder> {

    private Context mContext;
    private ArrayList<MovieItem> mMovieItems;

    public ImageAdapter(Context c, ArrayList<MovieItem> movieItems) {
        this.mContext = c;
        this.mMovieItems = movieItems;
    }
    //TODO: set just image to the constructor, using Picasso?
    //TODO: notify changes

    //define the ViewHolder
    class MyViewHolder extends RecyclerView.ViewHolder {
        // link the item(elements in rowlayout.xml) to the ViewHolder
        ImageView image;

        public MyViewHolder(View itemView) {
            super(itemView);
            image = (ImageView) itemView.findViewById(R.id.image);
        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // inflate the item Layout
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rowlayout, parent, false);
        //pass the view to the ViewHolder
        MyViewHolder viewHolder = new MyViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        //find the movie item in that position
        final MovieItem currentMovieItem = mMovieItems.get(position);
        // bind data to the holder (and therefore items)
        Picasso.with(mContext).load(currentMovieItem.getmImageUrl()).into(holder.image);

        //TODO: Sort this out

        // implement setOnClickListener event on item view.
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // open the detail activity on item click
                Intent intent = new Intent(mContext, DetailActivity.class);
                intent.putExtra("position", position); // put position in Intent
                mContext.startActivity(intent);
            }
        });
    }

   @Override
    public int getItemCount() {
        if(mMovieItems != null) {
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

}