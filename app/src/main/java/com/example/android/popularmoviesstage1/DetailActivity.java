package com.example.android.popularmoviesstage1;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

public class DetailActivity extends AppCompatActivity {

    ImageView selectedImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        selectedImage = (ImageView) findViewById(R.id.selectedImage);
        Intent intent = getIntent();
        // get image from Intent and set it in ImageView
        selectedImage.setImageResource(intent.getIntExtra("image", 0));

    }

    //to include and set data to original title, movie poster, a plot synopsis(overview),
    // user rating (vote_average), release date
    //TODO: find position and match up the rest of the MovieItem
}
