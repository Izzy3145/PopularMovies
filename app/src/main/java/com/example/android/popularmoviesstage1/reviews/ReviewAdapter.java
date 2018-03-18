package com.example.android.popularmoviesstage1.reviews;

import android.content.Context;
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
 * Created by izzystannett on 15/03/2018.
 */

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.MyViewHolder> {

    private Context mContext;
    private ArrayList<ReviewItem> mReviewItems;

    //first constructor to take in an array of movie items
    public ReviewAdapter(Context c, ArrayList<ReviewItem> reviewItems) {
        mContext = c;
        mReviewItems = reviewItems;
    }

    @Override
    public ReviewAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // inflate the item Layout
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.review_row_layout, parent,
                false);
        //pass the view to the ViewHolder
        MyViewHolder viewHolder = new MyViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ReviewAdapter.MyViewHolder holder, int position) {
        //get strings to assign to holder
        ReviewItem reviewItem = mReviewItems.get(position);
        String reviewAuthor = reviewItem.getmAuthor();
        String reviewBody = reviewItem.getmBody();
        //bind strings to holder
        holder.reviewAuthor.setText(reviewAuthor);
        holder.reviewBody.setText(reviewBody);
    }

    @Override
    public int getItemCount() {
        if (mReviewItems == null) {
            return 0;
        } else {
            return mReviewItems.size();
        }
    }

    public void setReviewData(ArrayList<ReviewItem> reviewItems) {
        mReviewItems = reviewItems;
        notifyDataSetChanged();
    }

    //define the ViewHolder that implements the click handler interface
    class MyViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.review_author)
        TextView reviewAuthor;
        @BindView(R.id.review_body)
        TextView reviewBody;

        //bind text views to itemView
        private MyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
