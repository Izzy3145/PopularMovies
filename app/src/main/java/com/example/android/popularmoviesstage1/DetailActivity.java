package com.example.android.popularmoviesstage1;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class DetailActivity extends AppCompatActivity {

    ImageView selectedImage;
    TextView selectedTitle;
    TextView selectedSynopsis;
    TextView selectedRating;
    TextView selectedDate;
    Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        selectedImage = (ImageView) findViewById(R.id.selectedImage);
        selectedTitle = (TextView) findViewById(R.id.selectedTitle);
        selectedSynopsis = (TextView) findViewById(R.id.selectedSynopsis);
        selectedRating = (TextView) findViewById(R.id.selectedRating);
        selectedDate = (TextView) findViewById(R.id.selectedDate);

        Intent intent = getIntent();
        // get image from Intent and set it in ImageView
        //Picasso.with(mContext).load(intent.getIntExtra("image", 0))
         //       .into(selectedImage);
        selectedTitle.setText(intent.getIntExtra("title",0));
        selectedSynopsis.setText(intent.getIntExtra("synopsis",0));
        selectedRating.setText(intent.getIntExtra("rating",0));
        selectedDate.setText(intent.getIntExtra("date",0));
    }

    //to include and set data to original title, movie poster, a plot synopsis(overview),
    // user rating (vote_average), release date
    //TODO: find position and match up the rest of the MovieItem
}
