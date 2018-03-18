package com.example.android.popularmoviesstage1.reviews;

/**
 * Created by izzystannett on 15/03/2018.
 */

public class ReviewItem {

    private String mAuthor;
    private String mBody;

    public ReviewItem(String author, String body) {
        mAuthor = author;
        mBody = body;
    }

    public String getmAuthor() {
        return mAuthor;
    }

    public String getmBody() {
        return mBody;
    }
}
