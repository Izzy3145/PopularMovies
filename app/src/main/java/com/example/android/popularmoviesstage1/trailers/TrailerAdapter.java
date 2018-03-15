package com.example.android.popularmoviesstage1.trailers;

import android.content.Context;
import android.content.res.Resources;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.popularmoviesstage1.R;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by izzystannett on 14/03/2018.
 */

public class TrailerAdapter extends RecyclerView.Adapter<TrailerAdapter.MyViewHolder> {

    private Context mContext;
    private ArrayList<String> mTrailers;
    private TrailerAdapterClickHandler mClickHandler;

    //first constructor to take in an array of movie items
    public TrailerAdapter(Context c, ArrayList<String> trailers, TrailerAdapterClickHandler
            trailerAdapterClickHandler) {
        mContext = c;
        mTrailers = trailers;
        mClickHandler = trailerAdapterClickHandler;
    }

    @Override
    public TrailerAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // inflate the item Layout
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.trailer_row_layout, parent,
                false);
        //pass the view to the ViewHolder
        MyViewHolder viewHolder = new MyViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(TrailerAdapter.MyViewHolder holder, int position) {
        //set the item view to the trailer label, i.e. "Trailer 1"
        //TODO: remove this hardcoded string
        String trailerNumber = Integer.toString(position + 1);
        String trailerLabelText = "Trailer " + trailerNumber;
        holder.trailerLabel.setText(trailerLabelText);
    }

    @Override
    public int getItemCount() {
        if (mTrailers == null) {
            return 0;
        } else {
            return mTrailers.size();
        }
    }

    public void setTrailerData(ArrayList<String> trailerStrings) {
        mTrailers = trailerStrings;
        notifyDataSetChanged();
    }

    //create interface for clickHandler
    public interface TrailerAdapterClickHandler {
        void onClickMethod(String trailerString);
    }

    //define the ViewHolder that implements the click handler interface
    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.trailer_label)
        TextView trailerLabel;

        //set a click listener to view
        private MyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        //define the onClick method to be assigned to the listener
        @Override
        public void onClick(View view) {
            String selectedTrailer;
            int adapterPosition = getAdapterPosition();
            selectedTrailer = mTrailers.get(adapterPosition);

            mClickHandler.onClickMethod(selectedTrailer);
        }
    }
}
